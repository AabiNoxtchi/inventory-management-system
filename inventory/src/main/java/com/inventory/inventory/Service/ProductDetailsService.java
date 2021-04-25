package com.inventory.inventory.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.annotation.Nullable;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.inventory.Exception.DuplicateNumbersException;
import com.inventory.inventory.Exception.NoParentFoundException;
import com.inventory.inventory.Model.DeliveryDetail;
import com.inventory.inventory.Model.ECondition;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.ProductDetail;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.ProfileDetail;
import com.inventory.inventory.Model.QDelivery;
import com.inventory.inventory.Model.QDeliveryDetail;
import com.inventory.inventory.Model.QProductDetail;
import com.inventory.inventory.Model.QProfileDetail;
import com.inventory.inventory.Model.QUserProfile;
import com.inventory.inventory.Model.UserProfile;
import com.inventory.inventory.Repository.ProductDetailRepositoryImpl;
import com.inventory.inventory.Repository.Interfaces.BaseRepository;
import com.inventory.inventory.Repository.Interfaces.DeliveryRepository;
import com.inventory.inventory.Repository.Interfaces.ProductDetailsRepository;
import com.inventory.inventory.Repository.Interfaces.UserProfilesRepository;
import com.inventory.inventory.ViewModels.ProductDetail.EditVM;
import com.inventory.inventory.ViewModels.ProductDetail.FilterVM;
import com.inventory.inventory.ViewModels.ProductDetail.IndexVM;
import com.inventory.inventory.ViewModels.ProductDetail.OrderBy;
import com.inventory.inventory.ViewModels.ProductDetail.ProductDetailDAO;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;

@Service
public class ProductDetailsService extends BaseService<ProductDetail, FilterVM, OrderBy, IndexVM, EditVM>{

	@Autowired
	ProductDetailsRepository repo;
	
	@Autowired
	ProductDetailRepositoryImpl repoImpl;	
	
	@Autowired
	DeliveryRepository dRepo;
	
	@Autowired
	UserProfilesRepository upRepo;
	
	@Override
	protected BaseRepository<ProductDetail> repo() {
		return repo;
	}

	@Override
	protected ProductDetail newItem() {
		return new ProductDetail();
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
		Long id = getLoggedUser().getId();
		ERole role = checkRole();		
		if(role.equals(ERole.ROLE_Mol))
			model.getFilter().setUserId(id);
		else if(role.equals(ERole.ROLE_Employee))
			model.getFilter().setEmployeeId(id);
	}	
	

	@Override
	protected void populateEditGetModel(EditVM model) {
		model.setEconditions(getConditions());	
	}

	@Override
	protected void populateEditPostModel(@Valid EditVM model) throws Exception {
		if(model.getDeliveryDetailId() == null) throw new NoParentFoundException();
		Long id = model.getId();
		if((id == null || id < 1) &&
			checkNumberExists(model.getInventoryNumber(), null)) throw new DuplicateNumbersException();		
		else {
			ProductDetail item = repo.findById(model.getId()).get(); // item = original			
			if(!item.getInventoryNumber().equals(model.getInventoryNumber()) &&
					checkNumberExists(model.getInventoryNumber(), null)) throw new DuplicateNumbersException();
			
			if(item.getEcondition().equals(ECondition.Available) && !model.getEcondition().equals(ECondition.Available)) {  // if condition changed first time
				
				/****************************** discard or not to discard  ****************************************/// ?????????????????????
				
				ProductDetailDAO pd = (repoImpl.getDAOs(QProductDetail.productDetail.id.eq(id), (long) 0, (long) 1)).get(0); //original DAO 
							
				if(pd.getProductType().equals(ProductType.STA)){					
					// event discard
					return;
				}
				
				Double percent = pd.getTotalAmortizationPercent();
				if(percent.equals(100.0)) {	
					// event discard
					return;					
				}
				
				Optional<UserProfile> upOpt = upRepo.findOne( 
						QUserProfile.userProfile.productDetailId.eq(item.getId()).and(QUserProfile.userProfile.returnedAt.isNull()));
				
				UserProfile up = upOpt.isPresent() ? upOpt.get() : null;				
				if(up == null) throw new Exception("associated profile not found !!!");	
				
				BigDecimal owedAmount = pd.getPrice().subtract(pd.getTotalAmortization());
				ProfileDetail profileDetail = new ProfileDetail(getUserCurrentDate(), owedAmount, new BigDecimal("0") );
				up.setProfileDetail(profileDetail);
				upRepo.save(up);
				
			}
			
			if(!item.getEcondition().equals(ECondition.Available) && model.getEcondition().equals(ECondition.Available)) {
				
				Optional<UserProfile> upOpt = upRepo.findOne( 
						QUserProfile.userProfile.productDetailId.eq(item.getId()).and(QUserProfile.userProfile.returnedAt.isNull()));
				
				UserProfile up = upOpt.isPresent() ? upOpt.get() : null;
				if(up == null) throw new Exception("associated profile not found !!!");
				
				if(up.getProfileDetail() != null) {
					ProfileDetail pd = up.getProfileDetail();
					if(!pd.isCleared()) {/************* sth to do ****************************/ }
					
					UserProfile updated = new UserProfile(up.getUserId(), up.getProductDetailId(),up.getGivenAt(), null);
					updated.setId(up.getId());
					upRepo.save(updated);
					
				}				
			}
		}			
	}
	
	
	protected void dealWithEnumDropDowns(IndexVM model) {		
		model.getFilter().setProductTypes(getProductTypes());
		model.getFilter().setEconditions(getConditions());		
	}
	
	@Override
	protected Long setDAOItems(IndexVM model, Predicate predicate, Long offset, Long limit,
			OrderSpecifier<?> orderSpecifier) {
		List<ProductDetailDAO> DAOs = 
				repoImpl.getDAOs(predicate, offset, limit);
				model.setDAOItems(DAOs);				
		return repoImpl.DAOCount(predicate);
	}

	public List<Long> checkWhereException(List<Long> deletedProductDts) {
		QProfileDetail pd = QProfileDetail.profileDetail;
		List<UserProfile> ups = 
				(List<UserProfile>) upRepo.findAll(
						QUserProfile.userProfile.id.in(deletedProductDts)
				.and(QUserProfile.userProfile.id.in(JPAExpressions.selectFrom(pd)
						.where(pd.id.in(deletedProductDts))
						.select(pd.id))));		
		List<Long> ids = new ArrayList<>();
		for(UserProfile up : ups)
			ids.add(up.getProductDetailId());
		return ids;			
	}
	
	@Override
	public Boolean checkGetAuthorization() {
		ERole role = checkRole();
		return role.equals(ERole.ROLE_Mol) || role.equals(ERole.ROLE_Employee);
	}

	@Override
	public Boolean checkSaveAuthorization() {
		ERole role = checkRole();
		return role.equals(ERole.ROLE_Mol);		
	}

	@Override
	public Boolean checkDeleteAuthorization() {
		ERole role = checkRole();
		return role.equals(ERole.ROLE_Mol);
	}	

	public ResponseEntity<?> getInventoryNumbers(FilterVM filter) {
		filter.setUserId(getLoggedUser().getId());		
		List<SelectItem> list = repoImpl.getInventoryNumbers(filter.getPredicate());		
		return ResponseEntity.ok(list);
	}
	
	
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public ResponseEntity<?> saveNumber	(SelectItem item, 
			@Nullable Long parentId) throws DuplicateNumbersException, NoParentFoundException{
		
        if(checkNumberExists(item.getName(), item.getValue())) throw new DuplicateNumbersException();		
        ProductDetail pd = getProductDetail(item,parentId);
        pd = repo.save(pd);        
        if(item.getValue() == null || Long.parseLong(item.getValue()) < 1 ) 
        	saveUserProfile(pd, parentId);        
        return ResponseEntity.ok(pd.getId());
	}	
	
	@Transactional(propagation = Propagation.MANDATORY)
	public boolean saveAll(List<SelectItem> pdNums, 
			com.inventory.inventory.ViewModels.DeliveryDetail.EditVM ddVM,	DeliveryDetail dd) 
					throws NoParentFoundException {
		boolean isOk = true;
		List<ProductDetail> productDts = new ArrayList<>();		
		List<ProductDetail> productDtsforUps = new ArrayList<>();	
        if(pdNums == null ) return true;
        
        for(int p = 0; p < pdNums.size(); p++) { 
        	
        	SelectItem pdSi = pdNums.get(p);        	
	        String idStr = pdNums.get(p).getValue();
			Long pdId = idStr.length() > 0 ? Long.parseLong(idStr) : -1; // 0 for new pds in new dds -x for new pds in existing dds
			
			
			boolean exists = checkNumberExists(pdSi.getName(), pdId);
			if(exists) {
				setNumErrors(ddVM, pdSi, pdId, p);
				isOk = false;				
			}else {	
				ProductDetail pd = getProductDetail(pdSi, dd.getId());
				if(pdId < 1) productDtsforUps.add(pd); // gather pds for update // will need user profile					 					 
				else productDts.add(pd); // gather pds for save			  
			}
         }
        
	      if(productDts.size() > 0) repo.saveAll(productDts);// save & update pds    		
	      if(productDtsforUps.size() > 0) {
	    		productDtsforUps = repo.saveAll(productDtsforUps);// save & update pds 
	    		for(ProductDetail pd : productDtsforUps) saveUserProfile(pd, dd.getId());   			
	      }
			return isOk;
	}
	
	private List<SelectItem> getConditions() {
		List<SelectItem> conditions = new ArrayList<>();
		SelectItem item = new SelectItem(ECondition.Available.name(), ECondition.Available.name());
		SelectItem item2 = new SelectItem(ECondition.Missing.name(), ECondition.Missing.name());
		SelectItem item3 = new SelectItem(ECondition.Damaged.name(), ECondition.Damaged.name());
		conditions.add(item);		
		conditions.add(item2);
		conditions.add(item3);
		return conditions;
	}

	private boolean checkNumberExists(String inventoryNumber, Object id) {
		if(id != null) {			
			String idStr = id.toString();
			Long pdId = idStr.length() > 0 ? Long.parseLong(idStr) : -1;
			if( (pdId > 0 && repo.getOne(pdId).getInventoryNumber().equals(inventoryNumber))) return false;	        
		}
		
		Predicate userP = QProductDetail.productDetail.deliveryDetail.product.userCategory.userId.eq(getLoggedUser().getId());
        Predicate pdP = QProductDetail.productDetail.inventoryNumber.eq(inventoryNumber).and(userP);
		if(repo.exists(pdP)) return true;
		return false;
	}

	private ProductDetail getProductDetail(SelectItem item, Long parentId) throws NoParentFoundException {		
		Long id =  (item.getValue() != null && item.getValue().length() > 0) ?
				Long.parseLong(item.getValue()) : -1;				
		if(id < 1 && parentId == null) throw new NoParentFoundException();		
		ProductDetail pd = id > 0 ? repo.findById(id).get() : new ProductDetail() ;		
		if(id < 1) {
			pd.setDeliveryDetail(new DeliveryDetail(parentId));			
			pd.setDiscarded(false);			
		}		
		pd.setInventoryNumber(item.getName());		
		return pd;
	}
	
	private LocalDate getDeliveryDate(Long ddId) {
		return
				dRepo.findOne(QDelivery.delivery.id.in(JPAExpressions.selectFrom(QDeliveryDetail.deliveryDetail)
				.where(QDeliveryDetail.deliveryDetail.id.eq(ddId))
				.select(QDeliveryDetail.deliveryDetail.delivery.id))).get().getDate();		
	}
	
	private void saveUserProfile(ProductDetail pd, Long ddId) {
		UserProfile up = new UserProfile(getLoggedUser().getId(), pd, getDeliveryDate(ddId));
        upRepo.save(up);		
	}

	private void setNumErrors(com.inventory.inventory.ViewModels.DeliveryDetail.EditVM ddVM, 
			SelectItem pdSi, Long pdId, int p) {		
		List<SelectItem> ddVMproductNums = ddVM.getProductNums();
		String[] productNumErrors = ddVM.getNumErrors();
		if(productNumErrors == null) productNumErrors = new String[ddVMproductNums.size()];		
		Long ddVMId = ddVM.getId();
		int index = 0;			

		if(ddVMId != null && ddVMId > 0) {
			index = (int) (pdId > 0 ? IntStream.range(0, ddVMproductNums.size())  // find index for pd with error
					.filter(n -> pdSi.getValue().equals(ddVMproductNums.get(n).getValue()))
					.findFirst().orElse(-1) : pdId < 0 ? -1 * pdId : p);					
		} else
			index = p;			
		
		productNumErrors[index] =  "duplicate inventory number !!!";	// errors found for pd				
		ddVM.setNumErrors(productNumErrors);
		
	}


}
