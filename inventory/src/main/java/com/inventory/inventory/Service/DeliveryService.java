package com.inventory.inventory.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
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
import com.inventory.inventory.ViewModels.Delivery.EDeliveryView;
import com.inventory.inventory.ViewModels.Delivery.EditVM;
import com.inventory.inventory.ViewModels.Delivery.FilterVM;
import com.inventory.inventory.ViewModels.Delivery.IndexVM;
import com.inventory.inventory.ViewModels.Delivery.OrderBy;
import com.inventory.inventory.ViewModels.Shared.PagerVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

@Service
public class DeliveryService extends BaseService<Delivery, FilterVM, OrderBy, IndexVM, EditVM>{

	@Autowired
	DeliveryRepository repo;
	
	@Autowired
	DeliveryRepositoryImpl repoImpl;
	
	@Autowired
	DeliveryDetailsService ddService;
	
	@Autowired
	DeliveryDetailRepository ddRepo;
	
	@Autowired
	SuppliersRepository suppliersRepo;
	
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
		
	}
	
	/*protected boolean setModel(IndexVM model, Predicate predicate, Sort sort) {
		
		if(model.getDeliveryView().equals(EDeliveryView.DeliveryView))
			return false;
		else if(model.getDeliveryView().equals(EDeliveryView.DeliveryDetailView)){
			PagerVM pager =  model.getPager();
			Long limit = (long) pager.getItemsPerPage();
			Long offset = pager.getPage() * limit;
			List<DeliveryDAO> DAOs = 
			repoImpl.getDeliveryDAOs(predicate, offset, limit);//, pager);
			model.setDAOItems(DAOs);
			
			Long totalCount = repoImpl.DAOCount(predicate);//.fetchCount();
			pager.setItemsCount(totalCount);
			pager.setPagesCount((int) (totalCount % limit > 0 ? (totalCount/limit) + 1 : totalCount / limit));
			return true;
		}
		else return false;		
	}*/
	
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
			FilterVM f = filter();
			f.setUserId(userId);			
			Predicate main =   f.mainPredicate();			
			JPQLQuery<Long> max = JPAExpressions.select(QDelivery.delivery.number.max()).from(QDelivery.delivery).where(main);				
			Predicate p = QDelivery.delivery.number.eq(max);
			List<Delivery> dlist = ((List<Delivery>) repo.findAll(((BooleanExpression) main).and(p)));
			Long number = dlist.size()>0 ? dlist.get(0).getNumber():0;
				
			model.setNumber(number+1);		
		}
	}

	@Override
	protected void populateEditPostModel(@Valid EditVM model) {
		System.out.println("**********************in save delivery service *****************");
		System.out.println("date = "+model.getDate());
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
		
//		System.out.println("(model.getId() < 1 || (ddVMs != null && ddVMs.size() > 0)) = "
//		+(model.getId() < 1 || (ddVMs != null && ddVMs.size() > 0)));
//		System.out.println("( (ddVMs != null && ddVMs.size() > 0)) = "
//				+ (ddVMs != null && ddVMs.size() > 0));
		boolean isOk = (model.getId() < 1 || (ddVMs != null && ddVMs.size() > 0)) ?
				ddService.saveAll(ddVMs, item) : true;
		System.out.println("*************** is ok d service = "+isOk);
		
		List<Long> deleteddds = model.getDeletedDetailsIds();
		if(model.getId() > 1 && deleteddds  != null && deleteddds.size() > 0) { // delete dds
			List<DeliveryDetail> dds= ddRepo.findAllById(deleteddds);
			ddRepo.deleteAll(dds);
			//System.out.println("dds size = "+dds.size());
			
			/*for(int i = 0; i < dds.size(); i++) {
				DeliveryDetail dd = dds.get(i);
				try {
					
					ddRepo.delete(dd);	/////////// check needed 	???	
					
				}catch(DataIntegrityViolationException e){
					System.out.println("catched exception ");
					String[] errors = model.getDdDeleteErrors();
					if (errors == null) errors = new String[dds.size()];
					errors[i] = "can't delete delivery with asociated profiles and owings !!!";				
					isOk = false;
				}
			}*/
		}
		
		if(!isOk)					
			throw new DuplicateNumbersException();//numErrors);
	}

	
	public ResponseEntity<?> errorsResponse(EditVM model) {
		
		String[][] numErrors = null;//new String[model.getDeliveryDetailEditVMs().size()][];	
		Long[][] deletionErrors = null;//new Long[model.getDeliveryDetailEditVMs().size()][];
		//String[] ddErrors = model.getDdDeleteErrors();
		//List<List<Long>> deletionErrors = new ArrayList<>();
		
		for(int i = 0; i < model.getDeliveryDetailEditVMs().size(); i++) {
			
			String[] numerrors = model.getDeliveryDetailEditVMs().get(i).getNumErrors() ;
			
			if(numerrors != null && numErrors == null) {
				numErrors = new String[model.getDeliveryDetailEditVMs().size()][];
				numErrors[i] = numerrors;
			}			
			
			List<Long> deletedErrors = model.getDeliveryDetailEditVMs().get(i).getDeletionErrors();
			if(deletedErrors != null)  //deletedErrors = new ArrayList<>();
			{
				if(deletionErrors == null) {
					deletionErrors = new Long[model.getDeliveryDetailEditVMs().size()][];
				}
				
				Long[] blank = new Long[0];
				deletionErrors[i] =  deletedErrors.toArray(blank); //add(deletedErrors);
			}
			
			//System.out.println("i = "+i+" array = "+Arrays.deepToString(numErrors[i]));
		}
		
		Map<String, Object> errorsToSend = new HashMap<>();
		errorsToSend.put("numErrors", numErrors);
		errorsToSend.put("deletionErrors", deletionErrors);
		//errorsToSend.put("ddDeletionErrors", ddErrors);
		
		System.out.println("errors to send = "+errorsToSend);
		return ResponseEntity		
				.badRequest()
				.body(errorsToSend);		
	}
	
	
	
	public ResponseEntity<?> deleteChild(Long id, Long childid) throws Exception {
		
		/*Optional<DeliveryDetail> existingChild = ddRepo.findById(id);
		if (!existingChild.isPresent())
			return ResponseEntity.badRequest().body("No record with that ID");*/
		
		/*Optional<Delivery> existingItem = repo.findById(id);
		if (!existingItem.isPresent())
			return ResponseEntity.badRequest().body("No record with that ID");*/
		//Delivery parent = repo.findById(id).get();
		DeliveryDetail item = ddRepo.findById(childid).get();
		/*JPQLQuery<Long> parentId = JPAExpressions
			    .selectFrom(QDeliveryDetail.deliveryDetail)
			    .where(QDeliveryDetail.deliveryDetail.id.eq(id))
			    .select(QDeliveryDetail.deliveryDetail.delivery.id);*/
		
		//Long parentId = parent.getId();	    
		Long childrenCount = 
				ddRepo.count(
						QDeliveryDetail.deliveryDetail.delivery.id.eq(id));
		
		System.out.println("children count = "+childrenCount);
		//ddRepo.deleteById(childid);
		if(childrenCount > 1) ddRepo.deleteById(childid);
		
		else if(childrenCount == 1) {
			throw new Exception("only child !!!");
			/************ in need of event to check parents children count ??????????????   **************////////////////
			//repo.delete(parent);
		}
		System.out.println("deleted child with id = "+childid);
		return ResponseEntity.ok(childid);

	}

	@Override
	protected Long setDAOItems(IndexVM model, Predicate predicate, Long offset, Long limit,
			OrderSpecifier<?> orderSpecifier) {
		List<DeliveryDAO> DAOs = 
				repoImpl.getDeliveryDAOs(predicate, offset, limit);//, pager);
				model.setDAOItems(DAOs);
				
				return repoImpl.DAOCount(predicate);//.fetchCount();
	}

	
	
	
//	protected void handleChildren(EditVM model, Delivery item) throws DuplicateNumbersException {
//	if(model.getId() < 1) return;
//	if(model.getDeletedDetailsIds() != null && model.getDeletedDetailsIds().size() > 0) {
//		List<DeliveryDetail> dds= ddRepo.findAllById(model.getDeletedDetailsIds()); 
//		ddRepo.deleteAll(dds);
//	}
//	fillItemChildren(model, item);
//}

//
//@Transactional(propagation = Propagation.MANDATORY)
//private void fillItemChildren(EditVM model, Delivery item) throws DuplicateNumbersException {
//	
//	boolean isOk = true; 
//	Predicate userP = QProductDetail.productDetail.deliveryDetail.product.user.id.eq(getLoggedUser().getId());
//	
//	List<com.inventory.inventory.ViewModels.DeliveryDetail.EditVM> deliveryDetailEditVMs =
//			model.getDeliveryDetailEditVMs();
//	
//	List<ProductDetail> productDts = new ArrayList<>();
//	List<Long> deletedProductDts = new ArrayList<>();
//	
//	System.out.println(" ddVM.size = "+deliveryDetailEditVMs.size());
//	
//	for(com.inventory.inventory.ViewModels.DeliveryDetail.EditVM ddVM : deliveryDetailEditVMs) {
//		
//		Long ddId = ddVM.getId();
//		ddId = ddId == null ? -1 : ddId; 
//		DeliveryDetail dd = new DeliveryDetail();
//		
//		if( ddId > 0 ) 
//			 dd = ddRepo.findById(ddId).get(); // catch null
//			 
//		 ddVM.populateEntity(dd);			
//		 item.addDeliveryDetail(dd);
//		 dd = ddRepo.save(dd);// ??
//			System.out.println("dd.productName = "+dd.getProductName()+" dd.id = "+dd.getId()); 
//		 if(ddVM.getDeletedNums() != null ) 
//			 deletedProductDts.addAll(ddVM.getDeletedNums());
//		 List<SelectItem> updatedNums = ddVM.getUpdatedProductNums();
//			
//		 
//		 if(updatedNums != null) {
//			 for(SelectItem pdSI : updatedNums) {
//				 
//				 Predicate pdP = QProductDetail.productDetail.inventoryNumber.eq(pdSI.getName()).and(userP);
//					if(productDtsRepo.exists(pdP)) {
//						
//						isOk = false;
//						
//						System.out.println("duplicate inventory number !!!");
//						
//						//List<ProductDetail> ddVMproductNums = ddVM.getProductNums();
//						List<SelectItem> ddVMproductNums = ddVM.getProductNums();
//						String[] productNumErrors = ddVM.getNumErrors();
//						if(productNumErrors == null) productNumErrors = new String[ddVMproductNums.size()];
//						int index = IntStream.range(0, ddVMproductNums.size())
//								.filter(i -> pdSI.getValue().equals(ddVMproductNums.get(i)))
//								.findFirst().orElse(-1);
//						
//						productNumErrors[index] =  "duplicate inventory number !!!";
//						
//						ddVM.setNumErrors(productNumErrors);
//					}else {
//						Long id = Long.parseLong(pdSI.getValue());
//						 if(id > 0) {
//								 
//							 ProductDetail existing = productDtsRepo.findById(id).get(); // catch null
//							 existing.setInventoryNumber(pdSI.getName());
//							 productDts.add(existing);
//								 
//						 }else {
//							 ProductDetail pd = new ProductDetail();
//							 pd.setInventoryNumber(pdSI.getName());
//							 pd.setDeliveryDetail(dd);
//							 pd.setAvailable(true);
//							 pd.setDiscarded(false);
//							 productDts.add(pd);
//						 }
//					}
//			 }
//				 //productDtsRepo.findAll(QProductDetail.productDetail.id.in(ddVM.getUpdatedProductNums().stream().map(x => x.id)));
//		 }
//		 
//		 if(ddId < 1) {
//		 //List<ProductDetail> ddProductDts = ddVM.getProductNums();
//			 List<SelectItem> ddProductDts = ddVM.getProductNums();
//		 for(int i = 0; i < ddProductDts.size(); i++) {
//				ProductDetail pd = new ProductDetail(ddProductDts.get(i).getName());
//				Predicate pdP = QProductDetail.productDetail.inventoryNumber.eq(pd.getInventoryNumber()).and(userP);
//				if(productDtsRepo.exists(pdP)) {						
//					isOk = false;						
//					System.out.println("duplicate inventory number !!!");
//					String[] productNumErrors = ddVM.getNumErrors();
//					if(productNumErrors == null) productNumErrors = new String[ddProductDts.size()];
//					productNumErrors[i] =  "duplicate inventory number !!!";
//					
//					ddVM.setNumErrors(productNumErrors);
//				}
//				else {
//					
//				pd.setDeliveryDetail(dd);
//				pd.setAvailable(true);
//				pd.setDiscarded(false);
//				productDts.add(pd);
//				}
//		 }
//		 }
//	}
//	
//	if(!isOk)					
//		throw new DuplicateNumbersException();
//	
//	productDtsRepo.deleteByIdIn(deletedProductDts);
//	productDtsRepo.saveAll(productDts);
//}
//

	
//	protected ResponseEntity<?> saveResponse(EditVM model, Delivery item) { 
		
//		boolean isOk = true;
//		if(model.getId() < 1) 
//		{
//			List<com.inventory.inventory.ViewModels.DeliveryDetail.EditVM> ddVMs = model.getDeliveryDetailEditVMs();
//			List<ProductDetail> productDts = new ArrayList<>();
//			//List<String> productNumErrors = new ArrayList<>();
//			
//			Predicate userP = QProductDetail.productDetail.deliveryDetail.product.user.id.eq(getLoggedUser().getId());
//			
//			//for(com.inventory.inventory.ViewModels.DeliveryDetail.EditVM ddVM : model.getDeliveryDetailEditVMs()) {
//			
//			for(int c = 0; c < ddVMs.size(); c++) {
//					com.inventory.inventory.ViewModels.DeliveryDetail.EditVM ddVM = ddVMs.get(c);
//					ddVM.setNumErrors(null);
//				DeliveryDetail dd = 
//						new DeliveryDetail(ddVM.getQuantity(), ddVM.getPricePerOne(), item, new Product(ddVM.getProductId()));
//				
//				//dds.add(dd);
//				dd = ddRepo.save(dd);
//				
//				List<ProductDetail> ddProductDts = ddVM.getProductNums();
//				
//				/* check unique constraint *******************************/
//				//for(ProductDetail pd : ddProductDts) {
//				for(int i = 0; i < ddProductDts.size(); i++) {
//					ProductDetail pd = ddProductDts.get(i);
//					Predicate pdP = QProductDetail.productDetail.inventoryNumber.eq(pd.getInventoryNumber()).and(userP);
//					if(productDtsRepo.exists(pdP)) {
//						
//						isOk = false;
//						
//						System.out.println("duplicate inventory number !!!");
//						String[] productNumErrors = ddVM.getNumErrors();
//						if(productNumErrors == null) productNumErrors = new String[ddProductDts.size()];
//						productNumErrors[i] =  "duplicate inventory number !!!";
//						
//						/**********************************/
//						ddVM.setNumErrors(productNumErrors);
//						//System.out.println("ddVM.productNumErrors[i] = "+ddVM.getProductNumErrors()[i]);		
//						/*List<String> productNumErrors = ddVM.getProductNumErrors();
//						if(productNumErrors == null) productNumErrors = new ArrayList();
//						int index = ddProductDts.indexOf(pd);
//						productNumErrors.set(index, "inventory number already exists !!!");
//						ddVM.setProductNumErrors(productNumErrors);
//						isOk = false;*/
//						//productNumErrors.get(index)
//						//new ArrayList<>();
//						//productNumErrors[ddProductDts.indexOf(pd)] = "duplicate inventory number !!!";
//						//ddProductDts.indexOf(pd);
//					}
//					else {
//						
//					pd.setDeliveryDetail(dd);
//					pd.setAvailable(true);
//					pd.setDiscarded(false);
//					productDts.add(pd);
//					}
//				}
//				
//				
//			}
//			
//				if(productDts.size() > 0 ) productDts = productDtsRepo.saveAll(productDts);
//			
//			
//			/*dds = ddRepo.saveAll(dds);
//			for(int i =0; i<dds.size();i++) {
//				List<ProductDetail> productDts = model.getDeliveryDetailEditVMs().get(i).getProductNums();
//				for(ProductDetail pd : productDts)
//					pd.setDeliveryDetail(dds.get(i));
//			}
//			item.setDeliveryDetails(dds);*/
//		}
//		System.out.println("isOk = "+isOk);
//		if(isOk) 	return ResponseEntity.ok(item) ;
//		else		{
//			//System.out.println("model.ddVM.productNumErrors[0]= "+model.getDeliveryDetailEditVMs().get(0).getNumErrors()[0]);
//			String[][] NumErrors = new String[model.getDeliveryDetailEditVMs().size()][];
//			for(int i = 0; i < model.getDeliveryDetailEditVMs().size(); i++) {
//				NumErrors[i] = model.getDeliveryDetailEditVMs().get(i).getNumErrors() ;
//					//	new String[model.getDeliveryDetailEditVMs().get(i).getNumErrors().length];
//			}
//			return ResponseEntity		
//							.badRequest()
//							.body(NumErrors);
//							//.body(model);
//							//.body(new String("Error : duplicate numbers"));
//		}
//	}

	
//	for(int i = 0; i < ddVMs.size(); i++) {
//	com.inventory.inventory.ViewModels.DeliveryDetail.EditVM ddVM = ddVMs.get(i);
//	ddVM.setNumErrors(null);
//	ddVM.setDelivery(item);
//	DeliveryDetail dd = new DeliveryDetail();	               
//    ddVM.populateEntity(dd);	       
//    dd = ddRepo.save(dd);// save dd
//    
//    Long ddVMId = ddVM.getId();
//    if(ddVMId != null && ddVMId > 0 && ddVM.getDeletedNums() != null)// gathere pds for delete
//    	 deletedProductDts.addAll(ddVM.getDeletedNums());///////////////////// check needed ???
//    
//    List<SelectItem> pdNums = null;// get pds for process
//    if(ddVMId != null && ddVMId > 0) {
//    	pdNums = ddVM.getUpdatedProductNums();
//    }else {
//    	pdNums = ddVM.getProductNums();
//    }
//    
//    Predicate userP = QProductDetail.productDetail.deliveryDetail.product.user.id.eq(getLoggedUser().getId());
//    if(pdNums != null ) {
//    for(int p = 0; p < pdNums.size(); p++) {
//    	//ProductDetail pd = new ProductDetail(pdNums.get(p).getName());
//        Predicate pdP = QProductDetail.productDetail.inventoryNumber.eq(pdNums.get(p).getName()).and(userP);
//        
//        String idStr = pdNums.get(p).getValue();
//		Long pdId = idStr.length() > 0 ? Long.parseLong(idStr) : 0;// 0 for new pds in new dds -x for new pds in existing dds
//        if(productDtsRepo.exists(pdP)) {
//			
//			isOk = false;
//			
//			List<SelectItem> ddVMproductNums = ddVM.getProductNums();
//			String[] productNumErrors = ddVM.getNumErrors();
//			if(productNumErrors == null) productNumErrors = new String[ddVMproductNums.size()];
//			//String value = pdNums.get(p).getValue();
//			//System.out.println("value = "+value);
//			int index = 0;
//			SelectItem pdSi = pdNums.get(p);
////			System.out.println("pdSi.getValue() = "+pdSi.getValue());
////			int counter = 0 ;
////			for(SelectItem si : ddVMproductNums) {
////				
////				System.out.println("counter "+counter+"  _si.getValue() = "+si.getValue());
////				System.out.println("pdSi.getValue().equals(si.getValue() = "+(pdSi.getValue().equals(si.getValue())));
////				counter++;
////			}
//			if(ddVMId != null && ddVMId > 0) {
//				index = (int) (pdId > 0 ? IntStream.range(0, ddVMproductNums.size())  // find index for pd with error
//						.filter(n -> pdSi.getValue().equals(ddVMproductNums.get(n).getValue()))
//						.findFirst().orElse(-1) : pdId < 0 ? -1 * pdId : p);
//				System.out.println("index 1= "+index);
//			} else{
//				index = p;
//			}
////			System.out.println("ddVMId = "+ddVMId);
////			System.out.println("p = "+p);
////			System.out.println("index = "+index);
//			
//			
//			productNumErrors[index] =  "duplicate inventory number !!!";	// errors found for pd				
//			ddVM.setNumErrors(productNumErrors);
//		}
//		else {
//		
////		String idStr = pdNums.get(p).getValue();
////		Long id = idStr.length() > 0 ? Long.parseLong(idStr) : -1;
//		 if(pdId > 0) {
//				 
//			 ProductDetail existing = productDtsRepo.findById(pdId).get(); // catch null
//			 existing.setInventoryNumber(pdNums.get(p).getName());
//			 productDts.add(existing);// gather pds for update
//				 
//		 }else {
//			 ProductDetail pd = new ProductDetail();
//			 pd.setInventoryNumber(pdNums.get(p).getName());
//			 pd.setDeliveryDetail(dd);
//			 pd.setAvailable(true);
//			 pd.setDiscarded(false);
//			 productDts.add(pd); // gather pds for save
//		 }
//		}
//    }
//    }
//}
//if(deletedProductDts.size()>0)
//	productDtsRepo.deleteByIdIn(deletedProductDts);// delete pds
//if(productDts.size()>0)
//	productDtsRepo.saveAll(productDts);// save & update pds


/*boolean isOk = true;
if(model.getId() < 1) 
{
	List<com.inventory.inventory.ViewModels.DeliveryDetail.EditVM> ddVMs = model.getDeliveryDetailEditVMs();
	
	List<ProductDetail> productDts = new ArrayList<>();
	Predicate userP = QProductDetail.productDetail.deliveryDetail.product.user.id.eq(getLoggedUser().getId());
	
	for(int c = 0; c < ddVMs.size(); c++) {
			com.inventory.inventory.ViewModels.DeliveryDetail.EditVM ddVM = ddVMs.get(c);
			ddVM.setNumErrors(null);
		DeliveryDetail dd = 
				new DeliveryDetail( ddVM.getPricePerOne(), item, new Product(ddVM.getProductId()));
		dd = ddRepo.save(dd);
		
		List<SelectItem> ddProductDts = ddVM.getProductNums();
		for(int i = 0; i < ddProductDts.size(); i++) {
			ProductDetail pd = new ProductDetail(ddProductDts.get(i).getName());
			Predicate pdP = QProductDetail.productDetail.inventoryNumber.eq(pd.getInventoryNumber()).and(userP);
			if(productDtsRepo.exists(pdP)) {
				
				isOk = false;
				
				System.out.println("duplicate inventory number !!!");
				String[] productNumErrors = ddVM.getNumErrors();
				if(productNumErrors == null) productNumErrors = new String[ddProductDts.size()];
				productNumErrors[i] =  "duplicate inventory number !!!";
				
				ddVM.setNumErrors(productNumErrors);
			}
			else {
				
			pd.setDeliveryDetail(dd);
			pd.setAvailable(true);
			pd.setDiscarded(false);
			productDts.add(pd);
			}
		}
	}
	
		if(productDts.size() > 0 ) productDts = productDtsRepo.saveAll(productDts);
}
		
*/


}
