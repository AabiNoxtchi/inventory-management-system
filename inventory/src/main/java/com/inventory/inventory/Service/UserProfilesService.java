package com.inventory.inventory.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.ProductDetail;
import com.inventory.inventory.Model.ProfileDetail;
import com.inventory.inventory.Model.QUserProfile;
import com.inventory.inventory.Model.UserProfile;
import com.inventory.inventory.Model.User.QEmployee;
import com.inventory.inventory.Model.User.QUser;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.Repository.UserProfileRepositoryImpl;
import com.inventory.inventory.Repository.Interfaces.BaseRepository;
import com.inventory.inventory.Repository.Interfaces.UserProfilesRepository;
import com.inventory.inventory.Repository.Interfaces.UsersRepository;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.inventory.inventory.ViewModels.UserProfiles.EditVM;
import com.inventory.inventory.ViewModels.UserProfiles.FilterVM;
import com.inventory.inventory.ViewModels.UserProfiles.IndexVM;
import com.inventory.inventory.ViewModels.UserProfiles.OrderBy;
import com.inventory.inventory.ViewModels.UserProfiles.TimeLineEditVM;
import com.inventory.inventory.ViewModels.UserProfiles.UserProfileDAO;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;

@Service
public class UserProfilesService extends BaseService<UserProfile, FilterVM, OrderBy, IndexVM, EditVM>{

	@Autowired
	UserProfilesRepository repo;
	
	@Autowired
	UserProfileRepositoryImpl repoImpl;
	
	@Autowired
	UsersRepository usersRepo;
	
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
		if(model.getId() == null || model.getId() < 1) 
			handleNew(model);
		else {
			
			UserProfile original = repo.findById(model.getId()).get();
			
			model.setProductDetailId(original.getProductDetailId());
			model.setGivenAt(original.getGivenAt());			
			model.setReturnedAt(original.getReturnedAt());
			
			if(model.getProfileDetail() != null && model.getPaidPlus() != null && model.getPaidPlus() > 0) {
				
			ProfileDetail pd = model.getProfileDetail();
			BigDecimal paid = pd.getPaidAmount().add(BigDecimal.valueOf(model.getPaidPlus()));
			
			if(paid.compareTo(pd.getOwedAmount()) == 1) { throw new Exception("paying more than needed !!!"); }
			if( paid.compareTo(pd.getOwedAmount()) == 0 ){
				pd.setCleared(true);  
			 }  
			pd.setPaidAmount(paid);
			pd.setModifiedAt(getUserCurrentDate());
			
			}
			else {
				if(!original.getProfileDetail().isCleared())
					model.setProfileDetail(original.getProfileDetail());
			}
		}
	}
	
	protected void handleReturns(List<UserProfile> ups) {		
		LocalDate now = getUserCurrentDate();
		List<UserProfile> newUps = new ArrayList<>();		
		for(UserProfile up : ups) {
			UserProfile newUp = new UserProfile(getLoggedUser().getId(), new ProductDetail(up.getProductDetailId()), now );
			newUps.add(newUp);
			up.setReturnedAt(now);
			newUps.add(up);			
		}		
		repo.saveAll(newUps);		
	}
			
	protected ResponseEntity<?> saveResponse(EditVM model, UserProfile item) { // if its multi save send items ids, else item id
		if(model.getProductDetailIds() != null)
		    model.addToSavedIds(item.getId());
		return ResponseEntity.ok( model.getSavedIds() != null ? model.getSavedIds() : item.getId());
	}
	
	@Override
	protected Long setDAOItems(IndexVM model, Predicate predicate, Long offset, Long limit,
			OrderSpecifier<?> orderSpecifier) {
		List<UserProfileDAO> DAOs = 
				repoImpl.getDAOs(predicate, offset, limit, orderSpecifier);		
			model.setDAOItems(DAOs);
			return repoImpl.DAOCount(predicate);
	}
	
	@Override
	public Boolean checkGetAuthorization() {
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
	
	@Transactional
	public ResponseEntity<?> delete(List<Long> ids) {		
		List<UserProfile> items = repo().findAllById(ids);		
		for(UserProfile up : items)
			up.setUser(new User(getLoggedUser().getId()));
		repo.saveAll(items);		
		return ResponseEntity.ok(ids);
	}
	
	@Transactional
	public ResponseEntity<?> delete(Long id) throws Exception {		
		Optional<UserProfile> existingItem = repo().findById(id);
		if (!existingItem.isPresent())
			return ResponseEntity.badRequest().body("No record found !!!");		
		
		UserProfile up = existingItem.get();
		Long molId = getLoggedUser().getId();		
			up.setUser(new User(molId));
			repo.save(up);
			
			return ResponseEntity.ok(id);		
	}	
	
	public ResponseEntity<?> deleteBefore(LocalDate date, Long productDetailId) {		
		QUserProfile up = QUserProfile.userProfile;
		QEmployee emp = QUser.user.as(QEmployee.class);
		
		Predicate p = up.givenAt.before(date).and(up.returnedAt.before(date));		
		p = productDetailId != null ? up.productDetailId.eq(productDetailId).and(p) : p ;
		
		Long userId = getLoggedUser().getId();
		p = ((up.userId.eq(userId)
				.or(up.userId.in(
						JPAExpressions.selectFrom(emp).where(emp.mol.id.eq(userId)).select(emp.id)))).and(p));
		
		List<UserProfile> ups = (List<UserProfile>) repo.findAll(p);
		
		List<UserProfile> withUnclearedOwings = 
				(List<UserProfile>) ups.stream().filter(i -> i.getProfileDetail()!= null /*&& !i.getProfileDetail().isCleared()*/).collect(Collectors.toList());
		
		if(withUnclearedOwings.size() > 0)
			return ResponseEntity.badRequest().body("some of these profiles still have owings !!!");
		
		repo.deleteAll(ups);
		return ResponseEntity.ok(ups.size());
	}

	public ResponseEntity<?> getTimeline(FilterVM filter) throws Exception {
		Page<UserProfile> page = getTimeLinePage(filter, 10);
		return getTimeLineEditVM(page);		
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public ResponseEntity<?> saveTimeline(TimeLineEditVM model) throws Exception {
		
		List<UserProfile> items = model.getItems();	
		if(items == null || items.size() == 0) return null;
		if(items.size() > 25) throw new Exception("maximum number of items to edit is 25 !!!");		
			
		/***** get original list from server by original filter and Check all Existence and same inventory with filter predicate ********/		
		
		FilterVM filter = getTimeLineFilter(model);		
		Page<UserProfile> page = getTimeLinePage(filter, 10);
		List<UserProfile> originalList =  page.getContent();
		if(originalList == null || originalList.size() == 0) throw new Exception("errors in recieved data verification with the originals !!!");		
		
		boolean allThere = isAllThere(originalList, model);
		if(!allThere) throw new Exception("errors in recieved data verification with the originals !!!");
				
		/******** quick check and return **********/
		if(originalList.size() == 1 && items.size() == 1 && (model.getDeletedIds() == null || model.getDeletedIds().size() == 0))			
			if(quickCheckAndSave(originalList.get(0), model, items)) return getTimeLineEditVM(getTimeLinePage(filter, 25));
		
		if(originalList.size() == 1 && items.size() == 1 && model.getDeletedIds().size() > 0)
			throw new Exception("errors in recieved data verification with the originals !!!");
		
		/******** get original first and last and check given at and returned at ********/
		
		List<UserProfile> firstAndLast = checkFirstAndLast(items, model, originalList);
		UserProfile updatedFirst = firstAndLast.get(0);
		UserProfile updatedLast = firstAndLast.get(1);		
		
		/******** sort sent items in order dates  just in case **********/
		
		sortItems(items, updatedFirst, updatedLast );		
		 
		/********* check order dates *********/	
		 
		 String[] givenAtErrors = new String[items.size()];
		 String[] returnAtErrors = new String[items.size()];
		 String[] timeErrors = new String[items.size()];
		 String[] userErrors = new String[items.size()];
		 
		 boolean isOk = validateTimeLine(givenAtErrors, returnAtErrors, timeErrors, userErrors, items, updatedFirst, updatedLast);
		 
		
	 /********* save & prepare response ********/
		if(!isOk) {
			model.setGivenAtErrors(givenAtErrors);			
			model.setReturnAtErrors(returnAtErrors);
			model.setTimeErrors(timeErrors);	
			model.setUserErrors(userErrors);	
			return ResponseEntity.badRequest().body(model);
		}
		
		model.populateEntities(items);
		items = repo.saveAll(items);
		if(model.getDeletedIds()!=null) {
		repo.deleteByIdIn(model.getDeletedIds());
		}
		
		return getTimeLineEditVM(getTimeLinePage(filter, 25));
	}
	

	private boolean validateTimeLine(String[] givenAtErrors, String[] returnAtErrors, String[] timeErrors,
			String[] userErrors, List<UserProfile> items, UserProfile updatedFirst, UserProfile updatedLast) {
		
		boolean isOk = true;
		Long molId = getLoggedUser().getId();

		 LocalDate minDate = updatedFirst.getGivenAt();
		 LocalDate maxDate = updatedLast.getReturnedAt() != null ? updatedLast.getReturnedAt() : getUserCurrentDate();;
		 
		 for(int i = 0; i < items.size(); i++) {			 
			 if(items.get(i).getUserId() == null) items.get(i).setUserId(molId);
			 else if(items.get(i).getId() == null || (items.get(i).getId() < 1))
			 {
				 try{
					 checkUserValid(items.get(i).getUserId(), molId);
				 }catch(Exception e) {
					 userErrors[i] = e.getMessage();
					 isOk=false;
				 }
			 }
			 
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
		
		 
		return isOk;
	}

	private void handleNew(@Valid EditVM model) throws Exception {
		
		//TimeZone 
		LocalDate now = getUserCurrentDate();
		if(model.getGivenAt() == null ) model.setGivenAt(now);
		if(model.getReturnedAt() != null || model.getGivenAt().isBefore(now)) { throw new Exception("time can't be changed in new records !!!"); }
		
		List<Long> ids = model.getProductDetailIds();		
		
		
		if((ids == null || ids.size() < 1 ) && model.getUserId() == null) /********** returned from emp, set current user = mol **********/	
				model.setUserId(getLoggedUser().getId());
					
		checkUserValid(model.getUserId(), getLoggedUser().getId());		
			
		if(ids == null ) { /************* giving employee one inventory set previous profile returned at*************/
		
			if(model.getProductDetailId() == null){ throw new Exception("must choose inventory !!!"); }
			
			UserProfile previous = model.getPreviousId() == null ? 			
				getPreviousProfile(model.getProductDetailId(), freePredicate()) : 
					repo.findById(model.getPreviousId()).get();
			previous.setReturnedAt(model.getGivenAt());
			repo.save(previous);
		}
		
		if(ids != null && model.getUserId() != null) { /****************** giving employee multi inventories ******************/
			
			for(int i = 0 ; i < ids.size() ; i++) {
				Long id = ids.get(i);
				model.setProductDetailId(id);
				
				UserProfile previous = getPreviousProfile(id, freePredicate()); 
				previous.setReturnedAt(model.getGivenAt());
				repo.save(previous);												 
					
				if(i < ids.size() - 1) {         /******************** if it's not last, save and add just to saved ids to track number *******************/
					UserProfile up = newItem() ;
					model.populateEntity(up);
					up = repo.save(up);
					System.out.println("saved profile and i = "+i + " : "+up.toString());
					model.addToSavedIds(up.getId());
				}	
			}
		}					
	}
	
	private void checkUserValid(Long id, Long molId) throws Exception {
		QUser u = QUser.user;
		QEmployee e = u.as(QEmployee.class);
		Optional<User> user = usersRepo.findOne(e.id.eq(id).and(e.mol.id.eq(molId)).and(e.deleted.isNull()));		
		if(user == null) throw new Exception("user is deleted or not found !!!");		
	}

	/***************************  to get previous profile and set it as returned *******************************/
	private UserProfile getPreviousProfile(Long pdId, Predicate p) throws Exception {
		
		if(pdId != null){
			List<UserProfile> previous = (List<UserProfile>) 
					repo.findAll(QUserProfile.userProfile.productDetail.id.eq(pdId).and(p));
			if(previous.size() == 1) return previous.get(0);
			else if(previous.size() > 1) throw new Exception("more than one record found when just expected one to update previous record !!!");
			else throw new Exception("no record found when expected one to update previous record !!!");
		}
		return null;
	}	

	
	private Predicate freePredicate(){		
		Long molId =  getLoggedUser().getId();
		return QUserProfile.userProfile.userId.eq(molId).and(QUserProfile.userProfile.returnedAt.isNull());		
	}	
	
	
	private ResponseEntity<TimeLineEditVM> getTimeLineEditVM(Page<UserProfile> page) {
		
		List<UserProfile> items = page.getContent();
		int count = items.size();
		Long total = page.getTotalElements();		
		
		TimeLineEditVM editVM = count > 0 ? new TimeLineEditVM(items, items.get(0).getId(), items.get(items.size()-1).getId(), count, total ) : 
			new TimeLineEditVM(null, null, null, count, total );
		
		if(count > 0 && total > count) {			
			editVM.setMessage("sending maximum of 10 records at atime !!!");
		}
		
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
		return page;
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
	
	private boolean quickCheckAndSave(UserProfile original, TimeLineEditVM model, List<UserProfile> items) throws Exception {
		UserProfile updated = items.get(0);
		if(!(updated.getGivenAt().equals(original.getGivenAt()) && 
				((updated.getReturnedAt() != null && updated.getReturnedAt().equals(original.getReturnedAt())) ||
						(updated.getReturnedAt() == null && original.getReturnedAt()==null)))) throw new Exception("error found in first and/or last items !!!");
		if(updated.getUserId() != null && updated.getUserId().equals(original.getUserId())) throw new Exception("item hasn't changed !!!");
		 if(updated.getUserId() == null) updated.setUserId(getLoggedUser().getId());
		 
		 model.populateEntities(items);
			items = repo.saveAll(items);
		 
			return true;		
	}

	private boolean isAllThere(List<UserProfile> originalList, TimeLineEditVM model) {
		List<UserProfile> items = model.getItems();	
		List<Long> deletedIds = model.getDeletedIds();		
			
		List<UserProfile> withIds = items != null ? items.stream().filter(i -> i.getId() != null && i.getId() > 0).collect(Collectors.toList()) : null;
		int withIdsSize = withIds != null ? withIds.size(): 0;
		int deletedSize = deletedIds != null ? deletedIds.size() : 0;
		
			if(withIdsSize + deletedSize  != originalList.size()) return false;
			boolean foundNoneOrDuplicate = false;		
			
			for(UserProfile o : originalList) {			
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
		
		UserProfile originalFirst = originalList.get(0);
				UserProfile originalLast = originalList.get(originalList.size()-1);
				
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

	

	
}








