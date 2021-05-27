package com.inventory.inventory.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.inventory.Exception.DuplicateNumbersException;
import com.inventory.inventory.Exception.NoChildrensFoundException;
import com.inventory.inventory.Exception.NoParentFoundException;
import com.inventory.inventory.Model.Delivery;
import com.inventory.inventory.Model.DeliveryDetail;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.QDelivery;
import com.inventory.inventory.Model.QDeliveryDetail;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.QProductDetail;
import com.inventory.inventory.Model.QSupplier;
import com.inventory.inventory.Model.Supplier;
import com.inventory.inventory.Repository.DeliveryRepositoryImpl;
import com.inventory.inventory.Repository.ProductDetailRepositoryImpl;
import com.inventory.inventory.Repository.Interfaces.BaseRepository;
import com.inventory.inventory.Repository.Interfaces.DeliveryDetailRepository;
import com.inventory.inventory.Repository.Interfaces.DeliveryRepository;
import com.inventory.inventory.Repository.Interfaces.SuppliersRepository;
import com.inventory.inventory.ViewModels.Delivery.DeliveryDAO;
import com.inventory.inventory.ViewModels.Delivery.EditVM;
import com.inventory.inventory.ViewModels.Delivery.FilterVM;
import com.inventory.inventory.ViewModels.Delivery.IndexVM;
import com.inventory.inventory.ViewModels.Delivery.OrderBy;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

@Service
public class DeliveryService extends BaseService<Delivery, FilterVM, OrderBy, IndexVM, EditVM>{

	@Autowired
	DeliveryRepository repo;
	
	@Autowired
	DeliveryRepositoryImpl repoImpl;
	
	@Autowired
	SuppliersRepository suppliersRepo;
	
	@Autowired
	DeliveryDetailsService ddService;
	
	@Autowired
	DeliveryDetailRepository ddRepo;
	
	@Autowired
	ProductDetailRepositoryImpl productDtsRepoImpl;
	
	@Override
	protected BaseRepository<Delivery> repo() {		
		return repo;
	}

	@Override
	protected Delivery newItem() {
		return new Delivery();
	}

	@Override
	protected FilterVM filter() {
		return new FilterVM();
	}

	@Override
	protected EditVM editVM() {
		return new EditVM();
	}

	@Override
	protected OrderBy orderBy() {
		return new OrderBy();
	}
	
	@Override
	protected void populateModel(IndexVM model) {
		model.getFilter().setUserId(getLoggedUser().getId());		
	}

	@Override
	public Boolean checkGetAuthorization() {
		ERole role = checkRole();
		return role.equals(ERole.ROLE_Mol) ;
	}

	@Override
	public Boolean checkSaveAuthorization() {
		ERole role = checkRole();
		return role.equals(ERole.ROLE_Mol) ;
	}

	@Override
	public Boolean checkDeleteAuthorization() {
		ERole role = checkRole();
		return role.equals(ERole.ROLE_Mol) ;
	}
	
	
	@Override
	protected void populateEditGetModel(EditVM model) { // delivery with children
		
		Long userId = getLoggedUser().getId();
		List<SelectItem> suppliers = getListItems(QSupplier.supplier.user.id.eq(userId),
				Delivery.class, "name", "id","supplier");		
		suppliers.remove(0);
		model.setSuppliers(suppliers);
		
		List<SelectItem> products = getListItems(QProduct.product.userCategory.user.id.eq(userId),Product.class, "name", "id",
				"product"); 
		products.remove(0);
		model.setProducts(products);
		
		List<DeliveryDetail> deliveryDetails = model.getDeliveryDetails();		
		if (deliveryDetails != null) {
			List<com.inventory.inventory.ViewModels.DeliveryDetail.EditVM> ddEditVMs = new ArrayList<>();
			for(DeliveryDetail dd : deliveryDetails) {				
				List<SelectItem> productNums = 
						productDtsRepoImpl.getInventoryNumbers(QProductDetail.productDetail.deliveryDetail.id.eq(dd.getId()));
				com.inventory.inventory.ViewModels.DeliveryDetail.EditVM ddVM = 
						new com.inventory.inventory.ViewModels.DeliveryDetail.EditVM();				
				ddVM.populateModel(dd);
				ddVM.setProductNums(productNums);
				ddEditVMs.add(ddVM);				
			}
			model.setDeliveryDetailEditVMs(ddEditVMs);
		}		
		
		if(model.getNumber() == null) {			
			
			Predicate main = QDelivery.delivery.supplier.user.id.eq(userId);			
			JPQLQuery<Long> max = JPAExpressions.select(QDelivery.delivery.number.max()).from(QDelivery.delivery).where(main);				
			Predicate p = QDelivery.delivery.number.eq(max).and(main);			
			
			Long number = repo.findOne(p).get().getNumber();				
			model.setNumber(number + 1);		
		}
	}

	@Override
	protected void populateEditPostModel(@Valid EditVM model) {
		if(model.getId() > 1 && model.getSupplierId() == null) return;
		Supplier supplier = suppliersRepo.findById(model.getSupplierId()).get();
		model.setSupplier(supplier);		
	}
	

	@Transactional(propagation = Propagation.MANDATORY)
	protected void handleAfterSave(EditVM model, Delivery item) throws DuplicateNumbersException, NoChildrensFoundException, NoParentFoundException, Exception {
		
		List<com.inventory.inventory.ViewModels.DeliveryDetail.EditVM> ddVMs = model.getDeliveryDetailEditVMs();
		
		if(model.getId() < 1) { // check for children exists in new deliveries or throw exception			
			if(ddVMs == null || ddVMs.size() < 1 ) {
				throw new NoChildrensFoundException();
			}else {				
				for(int i = 0 ; i < ddVMs.size(); i++) {					
					if(ddVMs.get(i).getProductNums() == null || ddVMs.get(i).getProductNums().size() < 1)
						throw new NoChildrensFoundException();					
				}
			}
		}

		boolean isOk = (model.getId() < 1 || (ddVMs != null && ddVMs.size() > 0)) ?
				ddService.saveAll(ddVMs, item) : true;
		
		List<Long> deleteddds = model.getDeletedDetailsIds();
		if(model.getId() > 1 && deleteddds  != null && deleteddds.size() > 0) { // delete dds
			List<DeliveryDetail> dds = ddService.getItems(deleteddds);//ddRepo.findAllById(deleteddds);
			ddRepo.deleteAll(dds);			
		}		
		if(!isOk)					
			throw new DuplicateNumbersException();
	}

	
	public ResponseEntity<?> errorsResponse(EditVM model) {
		
		String[][] numErrors = null;
		Long[][] deletionErrors = null;
		
		for(int i = 0; i < model.getDeliveryDetailEditVMs().size(); i++) {			
			String[] numerrors = model.getDeliveryDetailEditVMs().get(i).getNumErrors();			
			if(numerrors != null && numErrors == null) {
				numErrors = new String[model.getDeliveryDetailEditVMs().size()][];
				numErrors[i] = numerrors;
			}			
			
			List<Long> deletedErrors = model.getDeliveryDetailEditVMs().get(i).getDeletionErrors();
			if(deletedErrors != null) 
			{
				if(deletionErrors == null) {
					deletionErrors = new Long[model.getDeliveryDetailEditVMs().size()][];
			}
				
				Long[] blank = new Long[0];
				deletionErrors[i] =  deletedErrors.toArray(blank); 
			}
		}
		
		Map<String, Object> errorsToSend = new HashMap<>();
		errorsToSend.put("numErrors", numErrors);
		errorsToSend.put("deletionErrors", deletionErrors);
		return ResponseEntity		
				.badRequest()
				.body(errorsToSend);		
	}
	
	public ResponseEntity<?> deleteChild(Long id, Long childid) throws Exception {
		Long childrenCount = 
				ddRepo.count(
						QDeliveryDetail.deliveryDetail.delivery.id.eq(id));
		
		if(childrenCount > 1) ddRepo.deleteById(childid);		
		else if(childrenCount == 1) {
			throw new Exception("only child !!!");			
		}
		return ResponseEntity.ok(childid);
	}

	@Override
	protected Long setDAOItems(IndexVM model, Predicate predicate, Long offset, Long limit,
			OrderSpecifier<?> orderSpecifier) {
		List<DeliveryDAO> DAOs = 
				repoImpl.getDeliveryDAOs(predicate, offset, limit);
				model.setDAOItems(DAOs);				
				return repoImpl.DAOCount(predicate);
	}


}
