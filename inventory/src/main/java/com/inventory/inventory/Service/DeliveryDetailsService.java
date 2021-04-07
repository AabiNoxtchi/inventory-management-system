package com.inventory.inventory.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import com.inventory.inventory.Model.QDelivery;
import com.inventory.inventory.Model.QDeliveryDetail;
import com.inventory.inventory.Repository.Interfaces.BaseRepository;
import com.inventory.inventory.Repository.Interfaces.DeliveryDetailRepository;
import com.inventory.inventory.Repository.Interfaces.DeliveryRepository;
import com.inventory.inventory.Repository.Interfaces.ProductDetailsRepository;
import com.inventory.inventory.ViewModels.DeliveryDetail.EditVM;
import com.inventory.inventory.ViewModels.DeliveryDetail.FilterVM;
import com.inventory.inventory.ViewModels.DeliveryDetail.IndexVM;
import com.inventory.inventory.ViewModels.DeliveryDetail.OrderBy;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

@Service
public class DeliveryDetailsService extends BaseService<DeliveryDetail, FilterVM, OrderBy, IndexVM, EditVM>{

	@Autowired
	DeliveryDetailRepository ddRepo;
	
	@Autowired
	DeliveryRepository dRepo;
	
	@Autowired
	ProductDetailsService pdService;
	
	@Autowired
	ProductDetailsRepository productDtsRepo;
	
	@Override
	protected BaseRepository<DeliveryDetail> repo() {		
		return ddRepo;
	}

	@Override
	protected DeliveryDetail newItem() {		
		return new DeliveryDetail();
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
	protected void populateModel(IndexVM model) {
		model.getFilter().setUserId(getLoggedUser().getId());
		
//		List<Delivery> ds = (List<Delivery>) 
//				dRepo.findAll(QDelivery.delivery.supplier.user.id.eq((long) 12));
//		System.out.println("ds size = "+ds.size());
//		List<DeliveryDetail> dds = (List<DeliveryDetail>) 
//				ddRepo.findAll(QDeliveryDetail.deliveryDetail.delivery.supplier.id.eq((long) 12));
//		System.out.println("dds.size = "+dds.size());
//		List<DeliveryDetail> dds2 = (List<DeliveryDetail>) 
//				ddRepo.findAll(QDeliveryDetail.deliveryDetail.delivery.supplier.user.id.eq((long) 12));
//		System.out.println("dds2.size = "+dds2.size());
		
	}

	@Override
	protected void populateEditGetModel(EditVM model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void populateEditPostModel(@Valid EditVM model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleDeletingChilds(List<DeliveryDetail> items) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean handleDeletingChilds(DeliveryDetail e) {
		
	/*	JPQLQuery<Long> parentId = JPAExpressions
	    .selectFrom(QDeliveryDetail.deliveryDetail)
	    .where(QDeliveryDetail.deliveryDetail.id.eq(e.getId()))
	    .select(QDeliveryDetail.deliveryDetail.delivery.id);
		
		Long childrenCount = 
				ddRepo.count(
						QDeliveryDetail.deliveryDetail.delivery.id.eq(parentId));
		
		//if(childrenCount > 1) ddRepo.deleteById(id);
		if(childrenCount < 2) {
			/************ in need of event ??????????????   **************////////////////
			//Delivery parent = dRepo.findOne(QDelivery.delivery.id.eq(parentId)).get();
			//dRepo.delete(parent);
			//return ResponseEntity.ok(e.getId());
		//}
		//stem.out.println("deleted child with id = "+id);*/
		return false;
	}
	
	@Transactional(propagation = Propagation.MANDATORY)
	protected void handleAfterSave(EditVM model, DeliveryDetail item) 
			throws DuplicateNumbersException, NoChildrensFoundException, NoParentFoundException {
		
		if((model.getId() == null || model.getId() < 1) &&
				(model.getProductNums() == null || model.getProductNums().size()<1))
			throw new NoChildrensFoundException();
		
		boolean processPds = pdService.saveAll(model.getProductNums(), model, item);
		if(!processPds) throw new DuplicateNumbersException();
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public boolean saveAll(List<EditVM> ddVMs, Delivery item) throws NoParentFoundException {
		System.out.println("**********************in saveAll deliverydetail service *****************");
		boolean isOk = true;
		
		List<Long> deletedProductDts = new ArrayList<>();
		
		for(int i = 0; i < ddVMs.size(); i++) {
			com.inventory.inventory.ViewModels.DeliveryDetail.EditVM ddVM = ddVMs.get(i);
			ddVM.setNumErrors(null);
			ddVM.setDelivery(item);
			DeliveryDetail dd = new DeliveryDetail();	               
	        ddVM.populateEntity(dd);	       
	        dd = ddRepo.save(dd);// save dd
	        
	       // if(true)return false;
	        
	        Long ddVMId = ddVM.getId();
	        if(ddVMId != null && ddVMId > 0 && ddVM.getDeletedNums() != null)// gather pds for delete
	        	 deletedProductDts.addAll(ddVM.getDeletedNums());///////////////////// check needed ???
	        
	        List<SelectItem> pdNums = null;// get pds for process
	        if(ddVMId != null && ddVMId > 0) {
	        	pdNums = ddVM.getUpdatedProductNums();
	        }else 
	        	pdNums = ddVM.getProductNums();
	       
	        boolean processPds = pdService.saveAll(pdNums, ddVM, dd);
	        if(!processPds) isOk = false;
	        System.out.println("dd service is ok = "+isOk);
	        
	        if(deletedProductDts.size() > 0)
				productDtsRepo.deleteByIdIn(deletedProductDts);// delete pds
			
		}
		return isOk ;
	}
	
	public ResponseEntity<?> errorsResponse(EditVM model) {
		String[] numErrors = model.getNumErrors();	
		return ResponseEntity		
				.badRequest()
				.body(numErrors);
		
	}

	@Override
	protected Long setDAOItems(IndexVM model, Predicate predicate, Long offset, Long limit,
			OrderSpecifier<?> orderSpecifier) {
		// TODO Auto-generated method stub
		return null;
	}
}
	      
