package com.inventory.inventory.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.inventory.Model.City;
import com.inventory.inventory.Model.Country;
import com.inventory.inventory.Model.Delivery;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.QCity;
import com.inventory.inventory.Model.QDelivery;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.QUserProfile;
import com.inventory.inventory.Model.UserProfile;
import com.inventory.inventory.Model.User.Employee;
import com.inventory.inventory.Model.User.QEmployee;
import com.inventory.inventory.Model.User.QMOL;
import com.inventory.inventory.Model.User.QUser;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.Repository.UserRepositoryImpl;
import com.inventory.inventory.Repository.Interfaces.BaseRepository;
import com.inventory.inventory.Repository.Interfaces.DeliveryRepository;
import com.inventory.inventory.Repository.Interfaces.ProductsRepository;
import com.inventory.inventory.Repository.Interfaces.ProfileDetailRepository;
import com.inventory.inventory.Repository.Interfaces.UserProfilesRepository;
import com.inventory.inventory.Repository.Interfaces.UsersRepository;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.inventory.inventory.ViewModels.User.EditVM;
import com.inventory.inventory.ViewModels.User.FilterVM;
import com.inventory.inventory.ViewModels.User.IndexVM;
import com.inventory.inventory.ViewModels.User.OrderBy;
import com.inventory.inventory.ViewModels.User.UserDAO;
import com.inventory.inventory.auth.Models.RegisterRequest;
import com.inventory.inventory.auth.Service.UserDetailsServiceImpl;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;


@Service
public class UsersService extends BaseService<User, FilterVM, OrderBy, IndexVM, EditVM>{
	
	@Autowired
	private UsersRepository repo;
	
	@Autowired
	UserRepositoryImpl repoImpl;
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	UserProfilesService upService;
	
	@Autowired
	UserProfilesRepository upRepo;
	
	@Autowired
	ProfileDetailRepository profileDetailRepo;
	
	@Autowired
	ProductsRepository productsRepository;
	
	@Autowired
	DeliveryRepository dsRepo;
	
	
	@Override
	protected BaseRepository<User> repo() {
		return repo;
	}

	@Override
	protected User newItem() {
		return new User();
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
		ERole role = checkRole();
		model.getFilter().setWhosAskingRole(role);
		model.getFilter().setWhosAskingId(getLoggedUser().getId());	
	}
	
	@Override
	protected void populateEditGetModel(EditVM model) {

		if(getLoggedUser() != null) {
			if(checkRole().equals(ERole.ROLE_Admin) &&
					model.getId() != null && model.getId().equals(getLoggedUser().getId())) return;
			if(checkRole().equals(ERole.ROLE_Mol) &&
					!(model.getId() != null && model.getId().equals(getLoggedUser().getId()))) return;
			if(checkRole().equals(ERole.ROLE_Employee)) return;
		}			
			
		fillModelCityAndCountryFilters(model);
		if(model.getId() != null && model.getId() > 0) {
			City city = cityRepo.findOne(QCity.city.id.eq(
					JPAExpressions.selectFrom(QMOL.mOL).where(QMOL.mOL.id.eq(model.getId())).select(QMOL.mOL.city.id)
					)).get();
			model.setCityId(city.getId());
			model.setCountryId(city.getCountryId());
		}		
	}	

	@Override
	protected void populateEditPostModel(@Valid EditVM model) {
	}

	@Override
	protected void handleDeletingChilds(List<User> items) throws Exception {
		for(User u : items) {
			handleDeletingChilds(u);
			if(u.getClass().isAssignableFrom(Employee.class)) {
				if(((Employee) u ).getDeleted() != null)
					items.removeIf(i -> i.getId().equals(u.getId()));
			}
		}
	}

	@Override
	protected boolean handleDeletingChilds(User e) throws Exception {		
		
		if(e.getErole().equals(ERole.ROLE_Mol)) {
			
			QUserProfile qp = QUserProfile.userProfile;
			QUser qu = QUser.user;
			
			List<UserProfile> userProfiles = (List<UserProfile>) upRepo.findAll(qp.userId.eq(e.getId())
					.or(qp.userId.in(JPAExpressions
							.selectFrom(qu.as(QEmployee.class))
							.where(qu.as(QEmployee.class).mol.id.eq(e.getId()))
							.select(qu.as(QEmployee.class).id))));
			
			List<Long> ids = new ArrayList<>();
			userProfiles.stream().map(i -> ids.add(i.getId()));
			profileDetailRepo.deleteByIdIn(ids);
			
			upRepo.deleteAll(userProfiles);	
			
			List<User> emps = (List<User>) 
					repo.findAll(qu.as(QEmployee.class).mol.id.eq(e.getId()));
			repo.deleteAll(emps); 
			
			List<Delivery> dsList = (List<Delivery>) dsRepo
					.findAll(QDelivery.delivery.supplier.user.id.eq(e.getId()));
			dsRepo.deleteAll(dsList);
			
			List<Product> products = (List<Product>) productsRepository.findAll(QProduct.product.userCategory.user.id.eq(e.getId()));
			productsRepository.deleteAll(products);	
		}
		
		
		if(e.getErole().equals(ERole.ROLE_Employee)) {
			List<UserProfile> ups = (List<UserProfile>) upRepo.findAll(QUserProfile.userProfile.user.id.eq(e.getId()));
			
			if(ups.size() > 0) {
				List<UserProfile> unclearedOwings = 
						(List<UserProfile>) ups.stream()
						.filter(p -> p.getProfileDetail() != null && 
						!p.getProfileDetail().isCleared())
						.collect(Collectors.toList());
				
				if(unclearedOwings.size() > 0) { throw new Exception("employee still has owings !!!");} 
				
				upService.handleReturns(ups);  /************* return all inventories from employee to be deleted *********************/
				
				((Employee)e).setDeleted(getUserCurrentDate());
				((Employee)e).setUserName(((Employee)e).getUserName() + "(deleted)");
				repo().save(e);
				return true;
			}
		}
		return false;
	}	

	@Override
	protected Long setDAOItems(IndexVM model, Predicate predicate, Long offset, Long limit,
			OrderSpecifier<?> orderSpecifier) {
		boolean isAdmin = checkRole().equals(ERole.ROLE_Admin);
		List<UserDAO> DAOs = isAdmin ? repoImpl.getDAOsLong(predicate, offset, limit) : 
			repoImpl.getDAOs(predicate, offset, limit);
		model.setDAOItems(DAOs);
		
		return isAdmin ? repoImpl.DAOCountLong(predicate): repoImpl.DAOCount(predicate);
	}

	@Override
	protected boolean furtherAuthorize(Long id) {		
		if(getLoggedUser() == null && (id != null && id > 0)) return false; 		
		return true;
	}
	
	protected User getItem(Long id) throws Exception {		
		if(id == null ) throw new Exception("item not found !!!");
		Optional<User> opt = repo().findOne(
				filter().getFurtherAuthorizePredicate(id, getLoggedUser().getId(), checkRole()));		
		if(opt.isPresent()) { return opt.get();}
		else throw new Exception("item not found !!!");		
	}
	
	@Override
	public Boolean checkGetAuthorization() {		
		return checkRole().equals(ERole.ROLE_Admin) || checkRole().equals(ERole.ROLE_Mol);
	}

	@Override
	public Boolean checkSaveAuthorization() {		
		return checkRole().equals(ERole.ROLE_Admin) || checkRole().equals(ERole.ROLE_Mol);
	}

	@Override
	public Boolean checkDeleteAuthorization() {		
		return checkRole().equals(ERole.ROLE_Admin) || checkRole().equals(ERole.ROLE_Mol);
	}
	
	public Boolean checkGetItemAuthorization() { return true; }
	
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public ResponseEntity<?> save(EditVM model) throws Exception{
		//if(!furtherAuthorize(model.getId())) throw new Exception("Unauthorized !!!");
		//return userDetailsService.signup(model.registerRequest());
		return signup(model.registerRequest());
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public ResponseEntity<?> signup(RegisterRequest model) throws Exception{
		
		if(!furtherAuthorize(model.getId())) throw new Exception("Unauthorized !!!");		
		User user = model.getId() != null && model.getId() > 0 ? getItem(model.getId()) : null;
		return userDetailsService.signup(model, user);
	}
	
	@Transactional
	public ResponseEntity<?> delete(List<Long> ids) throws Exception {		
		List<User> items = repo().findAllById(ids);
		handleDeletingChilds(items);		
		repo().deleteAll(items);
		return ResponseEntity.ok(ids);
	}
	
	private void fillModelCityAndCountryFilters(EditVM model) {
		Predicate p = Expressions.asBoolean(true).isTrue();
		List<SelectItem> countries =  getListItems( p, Country.class, "name","id", "country");
		List<SelectItem> cities =  getListItems( p, City.class, "name","id", "countryId","city");	
		
		countries.remove(0);
		cities.remove(0);					
		model.setCities(cities);
		model.setCountries(countries);		
	}	
	
	
	
}
