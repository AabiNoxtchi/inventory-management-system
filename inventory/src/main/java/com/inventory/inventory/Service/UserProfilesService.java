package com.inventory.inventory.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.inventory.Exception.DuplicateNumbersException;
import com.inventory.inventory.Model.Delivery;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.ProductDetail;
import com.inventory.inventory.Model.QDelivery;
import com.inventory.inventory.Model.QDeliveryDetail;
import com.inventory.inventory.Model.QProductDetail;
import com.inventory.inventory.Model.QUserProfile;
import com.inventory.inventory.Model.UserProfile;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.Repository.ProductDetailRepositoryImpl;
import com.inventory.inventory.Repository.UserProfileRepositoryImpl;
import com.inventory.inventory.Repository.Interfaces.BaseRepository;
import com.inventory.inventory.Repository.Interfaces.DeliveryRepository;
import com.inventory.inventory.Repository.Interfaces.ProductDetailsRepository;
import com.inventory.inventory.Repository.Interfaces.UserProfilesRepository;
import com.inventory.inventory.Repository.Interfaces.UsersRepository;
import com.inventory.inventory.ViewModels.ProductDetail.ProductDetailDAO;
import com.inventory.inventory.ViewModels.Shared.PagerVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.inventory.inventory.ViewModels.UserProfiles.EditVM;
import com.inventory.inventory.ViewModels.UserProfiles.FilterVM;
import com.inventory.inventory.ViewModels.UserProfiles.IndexVM;
import com.inventory.inventory.ViewModels.UserProfiles.OrderBy;
import com.inventory.inventory.ViewModels.UserProfiles.TimeLineEditVM;
import com.inventory.inventory.ViewModels.UserProfiles.UserProfileDAO;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;

@Service
public class UserProfilesService extends BaseService<UserProfile, FilterVM, OrderBy, IndexVM, EditVM>{

	@Autowired
	UserProfilesRepository repo;
	
	@Autowired
	DeliveryRepository dRepo;
	
	@Autowired
	UserProfileRepositoryImpl repoImpl;
	
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
		if(model.getId()==null || model.getId() < 0) 
			handleNew(model);
		else {
			//handleUpdate(model);
			UserProfile original = repo.findById(model.getId()).get();
			model.setProductDetailId(original.getProductDetailId());
			model.setGivenAt(original.getGivenAt());			
			model.setReturnedAt(original.getReturnedAt());
			//1 check if its origin profile
			//if(isFirst(original ) && (model.getGivenAt() != original.getGivenAt()||
					//original.getProductDetailId() != model.getProductDetailId()||
					//model.getReturnedAt() != original.getReturnedAt()) )
				//throw new Exception("for the first profile associated with the delivery can't update given time !!!");
		}
	}

	private void handleNew(@Valid EditVM model) throws Exception {
		
		if(model.getReturnedAt() != null || model.getGivenAt().isBefore(LocalDate.now())) { throw new Exception("time can't be changed in new records !!!"); }
		List<Long> ids = model.getProductDetailIds();	
		
		if(ids == null&&model.getUserId() == null)
				model.setUserId(getLoggedUser().getId());/********** returned from emp *****************/
			
			if(ids == null&&model.getPreviousId()!=null) {
				UserProfile up =  repo.findById(model.getPreviousId()).get();//getPreviousProfile(model.getPreviousId(), null);// for inventory
				up.setReturnedAt(model.getGivenAt());
				repo.save(up);
				//updatePreviousProfile(up, model.getGivenAt());
			}
			
			if(ids != null && model.getUserId() != null) {
				
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
						
					if(i > ids.size() - 1) {
						UserProfile up = newItem() ;
						model.populateEntity(up);
						up = repo.save(up);
						model.addToSavedIds(up.getId());
					}		
				}
			}
			
			if(ids == null && model.getPreviousId()==null) {
				
				UserProfile previous = getPreviousProfile(model.getProductDetailId(), freePredicate());
				previous.setReturnedAt(model.getGivenAt());
				repo.save(previous);
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
	
	private void handleUpdate(@Valid EditVM model) throws Exception {
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
		
		
	}
	
	
	private void checkPreviousWithId(@Valid EditVM model) throws Exception {
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
		
	}

	private void updatePreviousProfile( UserProfile up , LocalDate givenAt) throws Exception {//, EditVM model
		/*******************    check date if not now more checks needed    ????????????????   **************************************/
		LocalDate now = LocalDate.now();		
		
		if(givenAt.equals(now) || givenAt.isAfter(up.getGivenAt()) || givenAt.equals(up.getGivenAt())) {
		up.setReturnedAt(givenAt);
		repo.save(up);
		return;
		}
		
		Delivery d = deliveryByPdId(up.getProductDetailId());				
		if(givenAt.isBefore(d.getDate()))throw new Exception("given date can't be earlier than delivery date !!!");			
		checkDeliveryTime(up.getProductDetailId(), givenAt);
		
		
		List<UserProfile> previousList = getPreviousList(up.getProductDetailId(), QUserProfile.userProfile.returnedAt.after(givenAt));
		
		List<UserProfile> toDelete = new ArrayList<>();
		toDelete.add(up);
		
		for(UserProfile profile : previousList) {
			if(profile.getGivenAt().isAfter(givenAt)) toDelete.add(profile);//repo.delete(profile);
			if( profile.getGivenAt().isBefore(givenAt) || profile.getGivenAt().isEqual(givenAt)){
				profile.setReturnedAt(givenAt);
				repo.save(profile);
			}
		}
		repo.deleteAll(toDelete);
		//updatePreviousInBetween(previousList,model.getGivenAt());
		
	}
	
	private void checkDeliveryTime(Long productDetailId, LocalDate givenAt) throws Exception {
		Delivery d = deliveryByPdId(productDetailId);				
		if(givenAt.isBefore(d.getDate()))throw new Exception("given date can't be earlier than delivery date !!!");//for inventory with number "+up.getInventoryNumber()+" !!!");			
	}

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
	
	private UserProfile getPreviousProfile(Long pdId, Predicate p) throws Exception {
		
		if(pdId != null){//??????????????????????????????? may need to extend logic
			
			List<UserProfile> previous = (List<UserProfile>) 
					repo.findAll(QUserProfile.userProfile.productDetail.id.eq(pdId).and(p));
			if(previous.size() == 1) return previous.get(0);
			else if(previous.size()>1) throw new Exception("more than one record found when just expected one to update previous record!!!");
			else throw new Exception("no record found when expected one to update previous record!!!");
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
	
protected boolean setModel(IndexVM model, Predicate predicate, Sort sort) {
		
		if(model.isLongView()) {			
			PagerVM pager =  model.getPager();
			Long limit = (long) pager.getItemsPerPage();
			Long offset = pager.getPage() * limit;
			List<UserProfileDAO> DAOs = 
			repoImpl.getDAOs(predicate, offset, limit);//, pager);
			model.setDAOItems(DAOs);
			
			Long totalCount = repoImpl.DAOCount(predicate);//.fetchCount();
			pager.setItemsCount(totalCount);
			pager.setPagesCount((int) (totalCount % limit > 0 ? (totalCount/limit) + 1 : totalCount / limit));
			return true;
		}
		else return false;		
	}
	
	protected ResponseEntity<?> saveResponse(EditVM model, UserProfile item) {
		if(model.getProductDetailIds()!=null)
		    model.addToSavedIds(item.getId());
		return ResponseEntity.ok( model.getSavedIds() != null ? model.getSavedIds():item.getId());
	}

	public ResponseEntity<?> getTimeline(FilterVM filter) {
		filter.setWhoseAskingId(getLoggedUser().getId());		
		filter.seteRole(checkRole());
		Sort sort = Sort.by(
			    Sort.Order.asc("givenAt"),
			    Sort.Order.asc("returnedAt"));
			
		List<UserProfile> items = (List<UserProfile>) repo.findAll(filter.getPredicate(),sort);
		
		//items.stream().forEach((p,i)-> p.getReturnedAt()==null?Collections.swap(items, i, items.size()-1):{});
		for(int i = 0; i < items.size(); i++) {
			if(items.get(i).getReturnedAt() == null) {
				//Collections.swap(items, i, items.size()-1);
				UserProfile u = items.get(i);
				items.remove(i);
				items.add(u);
				break;
			}
				
		}
		//items.stream().forEach(p->System.out.println(p.toString()));
		//System.out.println("first = "+items.get(0).toString());
		//System.out.println("last = "+items.get(items.size()-1));
		
		TimeLineEditVM editVM = items.size()>0 ? new TimeLineEditVM(items,items.get(0).getId(),items.get(items.size()-1).getId(), items.size()) : 
			new TimeLineEditVM(null,null,null, 0);
		
		SelectItem select = new SelectItem(""+getLoggedUser().getId(),getLoggedUser().getUsername());
		editVM.setSelect(select);
				 
		return ResponseEntity.ok(editVM);
	}

	@Transactional
	public ResponseEntity<?> saveTimeline(TimeLineEditVM model) throws Exception {
		
		List<UserProfile> items = model.getItems();		
		List<UserProfile> firstAndLast = items.stream()
				.filter (x-> x.getId()!=null && (x.getId().equals(model.getFirstId()) || x.getId().equals(model.getLastId())) ).collect(Collectors.toList());
		if(firstAndLast.size()!=2) throw new Exception("error found in first and/or last items !!!");
		UserProfile updatedFirst = (firstAndLast.stream().filter(x-> x.getId().equals(model.getFirstId()))).collect(Collectors.toList()).get(0);
		UserProfile updatedLast = (firstAndLast.stream().filter(x-> x.getId().equals(model.getLastId()))).collect(Collectors.toList()).get(0);
		UserProfile originalFirst = repo.findById(model.getFirstId()).get();
		UserProfile originalLast = repo.findById(model.getLastId()).get();
		if( !updatedFirst.getGivenAt().equals(originalFirst.getGivenAt()) || 
				( updatedLast.getReturnedAt()!=null && originalLast.getReturnedAt()!=null && !updatedLast.getReturnedAt().equals(originalLast.getReturnedAt())) ||
				(updatedLast.getReturnedAt()!=null && originalLast.getReturnedAt()==null) ||
				(updatedLast.getReturnedAt()==null && originalLast.getReturnedAt()!=null ))
			throw new Exception("error found in first and/or last items !!!");		
		
		items.removeIf(p-> p.getId()!=null && (p.getId().equals(model.getFirstId()) || p.getId().equals(model.getLastId())));
		 Collections.sort(items, 
			    Comparator.comparing(UserProfile::getGivenAt).thenComparing(UserProfile::getReturnedAt,Comparator.nullsLast(Comparator.naturalOrder())));
		 items.add(0, updatedFirst);
		 items.add(updatedLast);
		 
		 boolean isOk=true;
		 String[] givenAtErrors = new String[items.size()];
		 String[] returnAtErrors = new String[items.size()];
		 String[] timeErrors = new String[items.size()];		 
		 LocalDate minDate = updatedFirst.getGivenAt();
		 LocalDate maxDate = updatedLast.getReturnedAt()!=null ? updatedLast.getReturnedAt():LocalDate.now();
		 
		 for(int i =0;i<items.size();i++) {			 
			 if(items.get(i).getUserId() == null) items.get(i).setUserId(getLoggedUser().getId());
			 
			 LocalDate previousReturn = i > 0 ? items.get(i-1).getReturnedAt():null;
			 LocalDate currentReturn = items.get(i).getReturnedAt();
			 LocalDate currentGiven = items.get(i).getGivenAt();
			  if(i != items.size()-1 && currentReturn == null) {
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
		
		return ResponseEntity.ok(items.size());
	}

}








