package com.inventory.inventory.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.hibernate.NullPrecedence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.inventory.Model.Delivery;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.ProfileDetail;
import com.inventory.inventory.Model.QDelivery;
import com.inventory.inventory.Model.QProductDetail;
import com.inventory.inventory.Model.QUserProfile;
import com.inventory.inventory.Model.UserProfile;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.Repository.UserProfileRepositoryImpl;
import com.inventory.inventory.Repository.Interfaces.BaseRepository;
import com.inventory.inventory.Repository.Interfaces.CityRepository;
import com.inventory.inventory.Repository.Interfaces.DeliveryRepository;
import com.inventory.inventory.Repository.Interfaces.UserProfilesRepository;
import com.inventory.inventory.ViewModels.Shared.PagerVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.inventory.inventory.ViewModels.UserProfiles.EditVM;
import com.inventory.inventory.ViewModels.UserProfiles.FilterVM;
import com.inventory.inventory.ViewModels.UserProfiles.IndexVM;
import com.inventory.inventory.ViewModels.UserProfiles.OrderBy;
import com.inventory.inventory.ViewModels.UserProfiles.TimeLineEditVM;
import com.inventory.inventory.ViewModels.UserProfiles.UserProfileDAO;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;

@Service
public class UserProfilesService extends BaseService<UserProfile, FilterVM, OrderBy, IndexVM, EditVM>{

	@Autowired
	UserProfilesRepository repo;
	
	@Autowired
	UserProfileRepositoryImpl repoImpl;
	
	@Autowired
	DeliveryRepository dRepo;
	
	//@Autowired
	//CityRepository cityRepo;
	
	
	
//	@Autowired
//	UsersRepository userRepo;
//	
//	@Autowired
//	ProductDetailsRepository pdRepo;
	
	@Override
	protected BaseRepository<UserProfile> repo() {
		return repo;
	}

	@Override
	protected UserProfile newItem() {
		return new UserProfile();
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
		// TODO Auto-generated method stub
		boolean isAuthorized = checkRole().equals(ERole.ROLE_Mol) || checkRole().equals(ERole.ROLE_Employee);
		System.out.println("isAuthorized = "+isAuthorized);
		return checkRole().equals(ERole.ROLE_Mol) || checkRole().equals(ERole.ROLE_Employee);
	}

	@Override
	public Boolean checkSaveAuthorization() {
		// TODO Auto-generated method stub
		return checkRole().equals(ERole.ROLE_Mol);
	}

	@Override
	public Boolean checkDeleteAuthorization() {
		// TODO Auto-generated method stub
		return checkRole().equals(ERole.ROLE_Mol);
	}

	@Override
	protected void populateModel(IndexVM model) {
		model.getFilter().setWhoseAskingId(getLoggedUser().getId());		
		model.getFilter().seteRole(checkRole());
		
	}

	@Override
	protected void populateEditGetModel(EditVM model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void populateEditPostModel(@Valid EditVM model) throws Exception {		
		if(model.getId() == null || model.getId() < 1) // < 0 ??
			handleNew(model);
		else {
			//handleUpdate(model);
			UserProfile original = repo.findById(model.getId()).get();
			System.out.println("original 141 = "+original.toString());
			System.out.println("model.profileDetail = "+model.getProfileDetail());
			model.setProductDetailId(original.getProductDetailId());
			model.setGivenAt(original.getGivenAt());			
			model.setReturnedAt(original.getReturnedAt());
			
			if(model.getPaidPlus() != null && model.getPaidPlus() > 0) {
				
			ProfileDetail pd = model.getProfileDetail();
			BigDecimal paid = pd.getPaidAmount().add(BigDecimal.valueOf(model.getPaidPlus()));
			if(paid.compareTo(pd.getOwedAmount())==1) { throw new Exception("paing more than needed !!!"); }
			  if( paid.compareTo(pd.getOwedAmount()) == 0 ){
				pd.setCleared(true);  
			  /********* delete profile detail event ***************/}  
			pd.setPaidAmount(paid);
			pd.setModifiedAt(getUserCurrentDate());
			
			}
			//1 check if its origin profile
			//if(isFirst(original ) && (model.getGivenAt() != original.getGivenAt()||
					//original.getProductDetailId() != model.getProductDetailId()||
					//model.getReturnedAt() != original.getReturnedAt()) )
				//throw new Exception("for the first profile associated with the delivery can't update given time !!!");
		}
	}

	private void handleNew(@Valid EditVM model) throws Exception {
		
		//TimeZone 
		LocalDate now = getUserCurrentDate();//cityRepo	
		if(model.getGivenAt() == null )model.setGivenAt(now);
		if(model.getReturnedAt() != null || model.getGivenAt().isBefore(now)) { throw new Exception("time can't be changed in new records !!!"); }
		
		List<Long> ids = model.getProductDetailIds();	
		System.out.println("ids.size = "+(ids!=null?ids.size():"null"));
		
		if((ids == null || ids.size() < 1 ) && model.getUserId() == null) /********** returned from emp, set current user = mol *****************/	
				model.setUserId(getLoggedUser().getId());
			
		if(ids == null ) {//&& model.getPreviousId() == null) {  /************* giving employee one inventory set previous profile returned at*************//////??	
			if(model.getProductDetailId() == null){ throw new Exception("must choose inventory !!!"); }
			UserProfile previous = model.getPreviousId() == null ? 			
				getPreviousProfile(model.getProductDetailId(), freePredicate()) : 
					repo.findById(model.getPreviousId()).get();
			previous.setReturnedAt(model.getGivenAt());
			repo.save(previous);
		}
		/*if(ids == null && model.getPreviousId()!=null) {
			UserProfile up =  repo.findById(model.getPreviousId()).get();//getPreviousProfile(model.getPreviousId(), null);// for inventory
			up.setReturnedAt(model.getGivenAt());
			repo.save(up);
			//updatePreviousProfile(up, model.getGivenAt());
		}*/
		
		
		if(ids != null && model.getUserId() != null) { /****************** giving employee multi inventories ******************/
			
			for(int i = 0 ; i < ids.size() ; i++) {
				Long id = ids.get(i);//productDetailId
				model.setProductDetailId(id);
				//if(model.getReturnedAt() != null ) { handleUpdate(model); }
				//else {
				UserProfile previous = getPreviousProfile(id, freePredicate());//null user 
				previous.setReturnedAt(model.getGivenAt());
				repo.save(previous);
				//updatePreviousProfile(previous, model.getGivenAt());					
				
				//}									 
					
				if(i < ids.size() - 1) {         /*********************** if it's not last, save and add just to saved ids to track number **********************/
					UserProfile up = newItem() ;
					model.populateEntity(up);
					up = repo.save(up);
					System.out.println("saved profile and i = "+i + " : "+up.toString());
					model.addToSavedIds(up.getId());
				}		
			}
		}	
				
	}
	
	@Transactional
	public ResponseEntity<?> delete(List<Long> ids) {
		
		List<UserProfile> items = repo().findAllById(ids);
		//handleDeletingChilds(items);
		for(UserProfile up : items)
			up.setUser(new User(getLoggedUser().getId()));
		repo.saveAll(items);
		/************ in need of event to check parents children count ??????????????   **************////////////////
		return ResponseEntity.ok(ids);

	}
	
	@Transactional
	public ResponseEntity<?> delete(Long id) throws Exception {
		
		Optional<UserProfile> existingItem = repo().findById(id);
		if (!existingItem.isPresent())
			return ResponseEntity.badRequest().body("No record found !!!");
		//handleDeletingChilds(existingItem.get());
		//repo().deleteById(id);
		
		UserProfile up = existingItem.get();
		Long molId = getLoggedUser().getId();
		
			up.setUser(new User(molId));
			repo.save(up);
			return ResponseEntity.ok(id);
		
	}
	
	/*private void handleUpdate(@Valid EditVM model) throws Exception {
		if(model.getId()!=null && model.getId() > 0) checkPreviousWithId(model);
		else checkDeliveryTime(model.getProductDetailId(),model.getGivenAt());
		
		
		//2 if just give date is changed		
		//3 if just return date is changed		
		//if returned at null 
		
		Predicate p = QUserProfile.userProfile.returnedAt.after(model.getGivenAt());
		Predicate p2 = QUserProfile.userProfile.givenAt.before(model.getReturnedAt());
		p = model.getReturnedAt() != null ? ((BooleanExpression) p).and(p2) : p;
		List<UserProfile> previousList = getPreviousList(model.getProductDetailId(), p);
		
		List<UserProfile> toDelete = new ArrayList<>();
		List<UserProfile> updated = new ArrayList<>();
		//toDelete.add(up);
		
		for(UserProfile profile : previousList) {
			if(model.getReturnedAt() != null) {
				if(profile.getGivenAt().isAfter(model.getGivenAt()) && profile.getReturnedAt().isBefore(model.getReturnedAt())) toDelete.add(profile);//repo.delete(profile);
				else if(profile.getGivenAt().isBefore(model.getGivenAt()) && profile.getReturnedAt().isAfter(model.getReturnedAt())) {
					if(profile.getUserId() == model.getUserId()) {
						model.setGivenAt(profile.getGivenAt());
						model.setReturnedAt(profile.getReturnedAt());
						toDelete.add(profile);
					}else {
						profile.setReturnedAt(model.getGivenAt());
						updated.add(profile);
						UserProfile e = new UserProfile(model.getUserId(),model.getProductDetailId(), model.getReturnedAt(), profile.getReturnedAt());
						updated.add(e);
					}
				}
				else if( profile.getGivenAt().isBefore(model.getGivenAt()) || profile.getGivenAt().isEqual(model.getGivenAt())){
					profile.setReturnedAt(model.getGivenAt());
					updated.add(profile);//repo.save(profile);
				}
				else if(profile.getReturnedAt().isAfter(model.getReturnedAt()) || profile.getReturnedAt().isEqual(model.getReturnedAt())){
					profile.setGivenAt(model.getReturnedAt());
					updated.add(profile);
				}
			}else {
				if(profile.getGivenAt().isAfter(model.getGivenAt()) ) toDelete.add(profile);
				else {
					profile.setReturnedAt(model.getGivenAt());
					updated.add(profile);
				}
			}
		}
		repo.deleteAll(toDelete);
		repo.saveAll(updated);
		
		//1 if just user is changed
		
		
	}*/
	
	
	/*private void checkPreviousWithId(@Valid EditVM model) throws Exception {
		UserProfile original = repo.findById(model.getId()).get();
		
		//1 check if its origin profile
		if(isFirst(original ) && model.getGivenAt()!=original.getGivenAt() )
			throw new Exception("for the first profile associated with the delivery can't update given time !!!");
		
		//boolean isChangedProductId = false; 
		
		//4 if just product is changed
		if(original.getProductDetailId() != model.getProductDetailId()) {
			handleDeletingChilds(original, "change");
			//isChangedProductId = true;
			//List<UserProfile> toUpdate = getPreviousList(model.getProductDetailId(), QUserProfile.userProfile.returnedAt.after(model.getGivenAt())
		}
		
	}*/

//	private void updatePreviousProfile( UserProfile up , LocalDate givenAt) throws Exception {//, EditVM model
//		/*******************    check date if not now more checks needed    ????????????????   **************************************/
//		LocalDate now = LocalDate.now();		
//		
//		if(givenAt.equals(now) || givenAt.isAfter(up.getGivenAt()) || givenAt.equals(up.getGivenAt())) {
//		up.setReturnedAt(givenAt);
//		repo.save(up);
//		return;
//		}
//		
//		Delivery d = deliveryByPdId(up.getProductDetailId());				
//		if(givenAt.isBefore(d.getDate()))throw new Exception("given date can't be earlier than delivery date !!!");			
//		checkDeliveryTime(up.getProductDetailId(), givenAt);
//		
//		
//		List<UserProfile> previousList = getPreviousList(up.getProductDetailId(), QUserProfile.userProfile.returnedAt.after(givenAt));
//		
//		List<UserProfile> toDelete = new ArrayList<>();
//		toDelete.add(up);
//		
//		for(UserProfile profile : previousList) {
//			if(profile.getGivenAt().isAfter(givenAt)) toDelete.add(profile);//repo.delete(profile);
//			if( profile.getGivenAt().isBefore(givenAt) || profile.getGivenAt().isEqual(givenAt)){
//				profile.setReturnedAt(givenAt);
//				repo.save(profile);
//			}
//		}
//		repo.deleteAll(toDelete);
//		//updatePreviousInBetween(previousList,model.getGivenAt());
//		
//	}
	
//	private void checkDeliveryTime(Long productDetailId, LocalDate givenAt) throws Exception {
//		Delivery d = deliveryByPdId(productDetailId);				
//		if(givenAt.isBefore(d.getDate()))throw new Exception("given date can't be earlier than delivery date !!!");//for inventory with number "+up.getInventoryNumber()+" !!!");			
//	}

	/*private void updatePreviousInBetween(List<UserProfile> previousList, LocalDate givenAt) {
		List<UserProfile> toDelete = new ArrayList<>();
		toDelete.add(up);
		
		for(UserProfile profile : previousList) {
			if(profile.getGivenAt().isAfter(model.getGivenAt())) toDelete.add(profile);//repo.delete(profile);
			if( profile.getGivenAt().isBefore(model.getGivenAt()) || profile.getGivenAt().isEqual(model.getGivenAt())){
				profile.setReturnedAt(model.getGivenAt());
				repo.save(profile);
			}
		}
		repo.deleteAll(toDelete);
		
	}*/

	private List<UserProfile> getPreviousList(Long pdId, Predicate predicate){
		Predicate p = 
				QUserProfile.userProfile.productDetail.id.eq(pdId)
				.and(predicate);
		return (List<UserProfile>)repo.findAll(p);
	}
	
	/***************************  to get previous profile and set it as returned *******************************/
	private UserProfile getPreviousProfile(Long pdId, Predicate p) throws Exception {
		
		if(pdId != null){ //??????????????????????????????? may need to extend logic
			
			List<UserProfile> previous = (List<UserProfile>) 
					repo.findAll(QUserProfile.userProfile.productDetail.id.eq(pdId).and(p));
			if(previous.size() == 1) return previous.get(0);
			else if(previous.size() > 1) throw new Exception("more than one record found when just expected one to update previous record !!!");
			else throw new Exception("no record found when expected one to update previous record !!!");
		}
		return null;
	}
	
	
	
	private Delivery deliveryByPdId(Long pdId) {
		List<Delivery> dList = (List<Delivery>) dRepo.findAll(
				QDelivery.delivery.id.in(
						JPAExpressions
						.selectFrom(QProductDetail.productDetail)
						.where(QProductDetail.productDetail.id.eq(pdId))
						.select(QProductDetail.productDetail.deliveryDetail.delivery.id)
								
								)
				);
		return dList.get(0);	
	}
	
	@Override
	protected void handleDeletingChilds(UserProfile e) throws Exception {
		handleDeletingChilds(e, "delete");			
	}
	
	private void handleDeletingChilds(UserProfile e, String msg) throws Exception {
			if(isFirst(e )) throw new Exception("can't delete the first profile associated with the delivery !!!");			
		
			Long pdId = e.getProductDetailId();
			if(e.getReturnedAt() == null ) {
				
				//get previous and update returned=null
				UserProfile previous = getPreviousForDelete(pdId, e);
				System.out.println("previous = "+previous.toString());
				previous.setReturnedAt(null);
				repo.save(previous);
				
				return;
			}
			//get previous and next and update both
			UserProfile previous = getPreviousForDelete(pdId, e);
			UserProfile next = getNextForDelete(pdId,e);
			if(previous.getUserId() == next.getUserId()) {
				previous.setReturnedAt(next.getReturnedAt());
				repo.save(previous);
				repo.delete(next);
			}else {
				previous.setReturnedAt(e.getReturnedAt());
				repo.save(previous);
			}					
	}
	
	private boolean isFirst(UserProfile e) {//throws Exception {
		return e.getGivenAt().isEqual(deliveryByPdId(e.getProductDetailId()).getDate());
		//if( e.getGivenAt().isEqual(deliveryByPdId(e.getProductDetailId()).getDate()))
		//throw new Exception("can't "+msg+" the first profile associated with the delivery !!!");		
	}

	private UserProfile getNextForDelete(Long pdId, UserProfile e) {
		Predicate p = QUserProfile.userProfile.givenAt.eq(e.getReturnedAt());
		return getNextForDelete(pdId, p);// e,
	}
	
	private UserProfile getNextForDelete(Long pdId, Predicate p) {//, UserProfile e
		
		return (getPreviousList(pdId, p)
				.stream().min(Comparator.comparing(UserProfile::getId))).get();
	}

	private UserProfile getPreviousForDelete(Long pdId, UserProfile e) {
		// TODO Auto-generated method stub
		Predicate p = QUserProfile.userProfile.returnedAt.eq(e.getGivenAt());
		return (getPreviousList(pdId, p)
				.stream().max(Comparator.comparing(UserProfile::getId))).get();
	}
	
	private Predicate freePredicate(){
		
		Long molId =  getLoggedUser().getId();
		return QUserProfile.userProfile.userId.eq(molId).and(QUserProfile.userProfile.returnedAt.isNull());
		
	}	

	@Override
	protected void handleDeletingChilds(List<UserProfile> items) {
		// TODO Auto-generated method stub
		
	}
	
	/*protected boolean setModel(IndexVM model, Predicate predicate, OrderSpecifier<?> sort) {
		
		if(model.isLongView()) {			
			PagerVM pager =  model.getPager();
			Long limit = (long) pager.getItemsPerPage();
			Long offset = pager.getPage() * limit;
			
			
			List<UserProfileDAO> DAOs = 
			repoImpl.getDAOs(predicate, offset, limit, sort);//, pager);
			model.setDAOItems(DAOs);
			//System.out.println("DAOs size = "+DAOs.size());
			System.out.println("sort = "+sort);
			
			Long totalCount = repoImpl.DAOCount(predicate);//.fetchCount();
			pager.setItemsCount(totalCount);
			pager.setPagesCount((int) (totalCount % limit > 0 ? (totalCount/limit) + 1 : totalCount / limit));
			return true;
		}
		else return false;		
	}*/
	
	protected ResponseEntity<?> saveResponse(EditVM model, UserProfile item) { // if its multi save seng itms ids, else item id
		if(model.getProductDetailIds() != null)
		    model.addToSavedIds(item.getId());
		return ResponseEntity.ok( model.getSavedIds() != null ? model.getSavedIds() : item.getId());
	}

	public ResponseEntity<?> getTimeline(FilterVM filter) throws Exception {
		
//		if(filter.getProductDetailId() == null || filter.getProductDetailId() < 1)
//			throw new Exception("must choose inventory for time line edit !!!");
//		
//		filter.setWhoseAskingId(getLoggedUser().getId());		
//		filter.seteRole(checkRole());
//		Sort sort = Sort.by(
//			    Sort.Order.asc("givenAt"),
//			   Sort.Order.asc("returnedAt").nullsLast());
		/*PageRequest.of( Page, ItemsPerPage, sort )
		Page<E> page =  repo().findAll(predicate, model.getPager().getPageRequest(sort));//;
		model.setItems(page.getContent());
		model.getPager().setPagesCount(page.getTotalPages());
		model.getPager().setItemsCount(page.getTotalElements());	*/	
		Page<UserProfile> page = getTimeLinePage(filter, 10);// repo().findAll(filter.getPredicate(),PageRequest.of( 0, 10, sort ));
		
		
		return getTimeLineEditVM(page);
		
	}

	private ResponseEntity<TimeLineEditVM> getTimeLineEditVM(Page<UserProfile> page) {
		List<UserProfile> items = page.getContent();
		int count = items.size();
		Long total = page.getTotalElements();
		
		//items.stream().forEach((p,i)-> p.getReturnedAt()==null?Collections.swap(items, i, items.size()-1):{});
		/*for(int i = 0; i < items.size(); i++) {
			if(items.get(i).getReturnedAt() == null) {
				//Collections.swap(items, i, items.size()-1);
				UserProfile u = items.get(i);
				items.remove(i);
				items.add(u);
				break;
			}
				
		}*/
		//items.stream().forEach(p->System.out.println(p.toString()));
		//System.out.println("first = "+items.get(0).toString());
		//System.out.println("last = "+items.get(items.size()-1));
		
		TimeLineEditVM editVM = count > 0 ? new TimeLineEditVM(items, items.get(0).getId(), items.get(items.size()-1).getId(), count, total ) : 
			new TimeLineEditVM(null, null, null, count, total );
		
		if(count > 0 && total > count) {
			
			editVM.setMessage("sending maximum of 10 records at atime !!!");
		}
		//editVM.setMessage(editVM.getMessage()+ "sending maximum of 10 records at atime !!!");
		
		SelectItem select = new SelectItem(""+getLoggedUser().getId(),getLoggedUser().getUsername());
		editVM.setSelect(select);
				 
		return ResponseEntity.ok(editVM);
		
	}

	private Page<UserProfile> getTimeLinePage(FilterVM filter, int limit) throws Exception {
		if(filter.getProductDetailId() == null || filter.getProductDetailId() < 1)
			throw new Exception("must choose inventory for time line edit !!!");
		
		filter.setWhoseAskingId(getLoggedUser().getId());		
		filter.seteRole(checkRole());
		Sort sort = Sort.by(
			    Sort.Order.asc("givenAt"),
			   Sort.Order.asc("returnedAt").nullsLast());
		
		Page<UserProfile> page =  repo().findAll(filter.getPredicate(),PageRequest.of( 0, limit, sort ));
		//List<UserProfile> items = page.getContent();
		return page;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public ResponseEntity<?> saveTimeline(TimeLineEditVM model) throws Exception {
		
		List<UserProfile> items = model.getItems();	
		if(items == null || items.size() == 0) return null;
		if(items.size() > 25) throw new Exception("maximum number of items to edit is 25 !!!");
		
		///************************** get original list from server *********************************/
		
		/************************** get original list by original filter and Check all Existence and same inventory with filter predicate ************************************/
		//
		
		FilterVM filter = getTimeLineFilter(model);
		/*new FilterVM();
		filter.setGivenAfter(model.getSubmitGivenAfter());
		filter.setReturnedBefore(model.getSubmitReturnedBefore());
		filter.setProductDetailId(model.getSubmitProductDetailId());*/
		Page<UserProfile> page = getTimeLinePage(filter, 10);
		List<UserProfile> originalList =  page.getContent();
		if(originalList == null || originalList.size() == 0) throw new Exception("errors in recieved data verification with the originals !!!");
		
		
		boolean allThere = isAllThere(originalList, model);
		if(!allThere) throw new Exception("errors in recieved data verification with the originals !!!");
		
		
		// quick check and return
		if(originalList.size() == 1 && items.size() == 1 && (model.getDeletedIds() == null || model.getDeletedIds().size() == 0))
			//return quickCheckAndSave(originalList.get(0), model, items);
			if(quickCheckAndSave(originalList.get(0), model, items)) return getTimeLineEditVM(getTimeLinePage(filter, 25));
		
		if(originalList.size() == 1 && items.size() == 1 && model.getDeletedIds().size() > 0)
			throw new Exception("errors in recieved data verification with the originals !!!");
		
		/************************** get original first and last and check given at and returned at ************************************/
		
		List<UserProfile> firstAndLast = checkFirstAndLast(items, model, originalList);
		UserProfile updatedFirst = firstAndLast.get(0);
		UserProfile updatedLast = firstAndLast.get(1);
		
		
		
		/*************************************** sort sent items in order dates  just in case *********************************************/
		
		sortItems(items, updatedFirst, updatedLast );
		
		 
		 /************************************ check order dates **************************************/
		
		
		 boolean isOk =  true; //checkModelItemsValid(items, updatedFirst, updatedLast);//true;
		 
		 String[] givenAtErrors = new String[items.size()];
		 String[] returnAtErrors = new String[items.size()];
		 String[] timeErrors = new String[items.size()];		 
		 LocalDate minDate = updatedFirst.getGivenAt();
		 LocalDate maxDate = updatedLast.getReturnedAt() != null ? updatedLast.getReturnedAt() : getUserCurrentDate();;
		 
		 for(int i = 0; i < items.size(); i++) {			 
			 if(items.get(i).getUserId() == null) items.get(i).setUserId(getLoggedUser().getId());
			 
			 LocalDate previousReturn = i > 0 ? items.get(i-1).getReturnedAt():null;
			 LocalDate currentReturn = items.get(i).getReturnedAt();
			 LocalDate currentGiven = items.get(i).getGivenAt();
			  if(i != items.size() - 1 && currentReturn == null) {
				 returnAtErrors[i]="required field !!!";
				 isOk=false;
			 }
			 if(currentGiven == null) {
				 givenAtErrors[i] = "required field !!!";
				 isOk=false;
				 
			 }
			 if(currentGiven.isBefore(minDate)) {
				 givenAtErrors[i] = "can't be before first given !!!";
				 isOk=false;
				 
			 }	
			 if(currentReturn != null && currentReturn.isBefore(minDate)){
				 returnAtErrors[i] = "can't be before first given !!!";
				 isOk=false;
			 }
			 if(currentGiven.isAfter(maxDate)) {
				 givenAtErrors[i] = "can't be after last return !!!";
				 isOk=false;
				 
			 }	
			 if(currentReturn != null && currentReturn.isAfter(maxDate)){
				 returnAtErrors[i] = "can't be after last return !!!";
				 isOk=false;
			 }
			
			 if(currentReturn != null && currentReturn.isBefore(currentGiven)){
				 returnAtErrors[i] = "can't be before given at !!!";
				 isOk=false;
			 }			 
			 if(i > 0 && currentGiven.isBefore(previousReturn)){
				 timeErrors[i-1] = "time overlap !!!";
				 isOk=false;
			 }
			 
			 if(i > 0 && currentGiven.isAfter(previousReturn)){
				 timeErrors[i-1] = "time gap !!!";
				 isOk=false;
			 }
		 }
		
		 
	 /***************************** save & prepare response ***************************************/
		if(!isOk) {
			model.setGivenAtErrors(givenAtErrors);			
			model.setReturnAtErrors(returnAtErrors);
			model.setTimeErrors(timeErrors);			
			return ResponseEntity.badRequest().body(model);
		}
		
		model.populateEntities(items);
		items = repo.saveAll(items);
		if(model.getDeletedIds()!=null) {
		repo.deleteByIdIn(model.getDeletedIds());
		}
		
		//
		//return ResponseEntity.ok(items.size());
		return getTimeLineEditVM(getTimeLinePage(filter, 25));
	}

	private boolean quickCheckAndSave(UserProfile original, TimeLineEditVM model, List<UserProfile> items) throws Exception {
		UserProfile updated = items.get(0);
		if(!(updated.getGivenAt().equals(original.getGivenAt()) && 
				((updated.getReturnedAt() != null && updated.getReturnedAt().equals(original.getReturnedAt())) ||
						(updated.getReturnedAt() == null && original.getReturnedAt()==null)))) throw new Exception("error found in first and/or last items !!!");
		if(updated.getUserId() != null && updated.getUserId().equals(original.getUserId())) throw new Exception("item hasn't changed !!!");
		 if(updated.getUserId() == null) updated.setUserId(getLoggedUser().getId());
		 
		 model.populateEntities(items);
			items = repo.saveAll(items);
		 
			return true;//ResponseEntity.ok(items.size());
			
		
	}

	private FilterVM getTimeLineFilter(TimeLineEditVM model) {
		FilterVM filter = new FilterVM();
		filter.setGivenAfter(model.getSubmitGivenAfter());
		filter.setReturnedBefore(model.getSubmitReturnedBefore());
		filter.setProductDetailId(model.getSubmitProductDetailId());
		return filter;
	}

	

	private void sortItems(List<UserProfile> items, UserProfile updatedFirst, UserProfile updatedLast) {
		items.removeIf(p-> p.getId() != null && (p.getId().equals(updatedFirst.getId()) ||
				p.getId().equals(updatedLast.getId())));
		 Collections.sort(items, 
			    Comparator.comparing(UserProfile::getGivenAt).thenComparing(UserProfile::getReturnedAt,Comparator.nullsLast(Comparator.naturalOrder())));
		 items.add(0, updatedFirst);
		 items.add(updatedLast);
		
	}

	private boolean isAllThere(List<UserProfile> originalList, TimeLineEditVM model) {
		List<UserProfile> items = model.getItems();	
		List<Long> deletedIds = model.getDeletedIds();
		
		//Map<Long, Integer> founds = new HashMap<>();
	//	items.stream().forEach( x ->{ if( x.getId() != null && x.getId() > 0 ) {
			
		List<UserProfile> withIds = items != null ? items.stream().filter(i -> i.getId() != null && i.getId() > 0).collect(Collectors.toList()) : null;
		int withIdsSize = withIds != null ? withIds.size(): 0;
		int deletedSize = deletedIds != null ? deletedIds.size() : 0;
		//size += deletedIds != null ? deletedIds.size() : 0; 
			if(withIdsSize + deletedSize  != originalList.size()) return false;//throw new Exception("errors in recieved data verification with the originals !!!");
			boolean foundNoneOrDuplicate = false;
		//originalList.stream().forEach(o -> {
			
			for(UserProfile o : originalList) {
			/*boolean found*/
			List<UserProfile> foundsWithIds = withIdsSize > 0 ? ((List<UserProfile>) withIds.stream()
					.filter(p -> p.getId().equals(o.getId())).collect(Collectors.toList())) : null ;
			List<Long> foundsDeletedIds = deletedSize > 0 ? (List<Long>) deletedIds.stream()
					.filter(i -> i.equals(o.getId())).collect(Collectors.toList()) : null;
			
			int founds = foundsWithIds != null ? foundsWithIds.size() : 0;
			founds+= foundsDeletedIds != null ? foundsDeletedIds.size() : 0;
			
			if( founds != 1 ) {
				foundNoneOrDuplicate = true;
			}
		}
		
		return !foundNoneOrDuplicate;
	}

	


	private List<UserProfile> checkFirstAndLast(List<UserProfile> items, TimeLineEditVM model, List<UserProfile> originalList) throws Exception {
		
		//if(model.getFirstId() != originalList.get(0).getId() || model.)
		UserProfile originalFirst = originalList.get(0);//repo.findById(model.getFirstId()).get();
				UserProfile originalLast = originalList.get(originalList.size()-1);//repo.findById(model.getLastId()).get();
				
		Long firstId = originalFirst.getId();
		Long lastId = originalLast.getId();
		
		List<UserProfile> firstAndLast = items.stream()
				.filter (x-> x.getId()!=null && (x.getId().equals(firstId) || x.getId().equals(lastId)) ).collect(Collectors.toList());
		
		if(firstAndLast.size() != 2 ) throw new Exception("error found in first and/or last items !!!");
		
		UserProfile updatedFirst = (firstAndLast.stream().filter(x-> x.getId().equals(firstId))).collect(Collectors.toList()).get(0);
		UserProfile updatedLast = (firstAndLast.stream().filter(x-> x.getId().equals(lastId))).collect(Collectors.toList()).get(0);
				
		if( !updatedFirst.getGivenAt().equals(originalFirst.getGivenAt()) || 
				( updatedLast.getReturnedAt() != null && originalLast.getReturnedAt() != null && !updatedLast.getReturnedAt().equals(originalLast.getReturnedAt())) ||
				(updatedLast.getReturnedAt() != null && originalLast.getReturnedAt() == null) ||
				(updatedLast.getReturnedAt() == null && originalLast.getReturnedAt() != null ||
				updatedFirst.getProductDetailId() != originalFirst.getProductDetailId() ||
				updatedLast.getProductDetailId() != originalLast.getProductDetailId() ||
				updatedFirst.getProductDetailId() != updatedLast.getProductDetailId()
						))
			throw new Exception("error found in first and/or last items !!!");			
		
		List<UserProfile> toReturn = new ArrayList<>();
		toReturn.add(updatedFirst);
		toReturn.add(updatedLast);
		return toReturn;
		
	}

	@Override
	protected Long setDAOItems(IndexVM model, Predicate predicate, Long offset, Long limit,
			OrderSpecifier<?> orderSpecifier) {
		List<UserProfileDAO> DAOs = 
				repoImpl.getDAOs(predicate, offset, limit, orderSpecifier);//, pager);
				model.setDAOItems(DAOs);
				//System.out.println("DAOs size = "+DAOs.size());
				//System.out.println("sort = "+sort);
				
				return repoImpl.DAOCount(predicate);
	}

}








