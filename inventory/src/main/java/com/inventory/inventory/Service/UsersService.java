package com.inventory.inventory.Service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.inventory.Exception.DuplicateNumbersException;
import com.inventory.inventory.Model.City;
import com.inventory.inventory.Model.Country;
import com.inventory.inventory.Model.Delivery;
import com.inventory.inventory.Model.DeliveryDetail;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.ProductDetail;
import com.inventory.inventory.Model.QCity;
import com.inventory.inventory.Model.QDelivery;
import com.inventory.inventory.Model.QDeliveryDetail;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.QProductDetail;
import com.inventory.inventory.Model.QSupplier;
import com.inventory.inventory.Model.QUserCategory;
import com.inventory.inventory.Model.QUserProfile;
import com.inventory.inventory.Model.Supplier;
import com.inventory.inventory.Model.UserCategory;
import com.inventory.inventory.Model.User.Employee;
import com.inventory.inventory.Model.User.InUser;
import com.inventory.inventory.Model.User.QEmployee;
import com.inventory.inventory.Model.User.QMOL;
import com.inventory.inventory.Model.User.QUser;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.Repository.UserRepositoryImpl;
import com.inventory.inventory.Repository.Interfaces.BaseRepository;
import com.inventory.inventory.Repository.Interfaces.CityRepository;
import com.inventory.inventory.Repository.Interfaces.DeliveryDetailRepository;
import com.inventory.inventory.Repository.Interfaces.DeliveryRepository;
import com.inventory.inventory.Repository.Interfaces.ProductDetailsRepository;
import com.inventory.inventory.Repository.Interfaces.ProductsRepository;
import com.inventory.inventory.Repository.Interfaces.SuppliersRepository;
import com.inventory.inventory.Repository.Interfaces.UserProfilesRepository;
import com.inventory.inventory.Repository.Interfaces.UsersRepository;
import com.inventory.inventory.ViewModels.Product.ProductDAO;
import com.inventory.inventory.ViewModels.Shared.PagerVM;
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
	UserProfilesRepository upRepo;
	
	@Autowired
	ProductsRepository productsRepository;
	
	@Autowired
	DeliveryDetailRepository ddRepo;
	
	@Autowired
	DeliveryRepository dsRepo;
	
	@Autowired
	SuppliersRepository sRepo;
	
	@Autowired
	ProductDetailsRepository ProductDtsRepo;
	
	@Autowired
	CityRepository cityRepo;
	
	//@Autowired
	//AvailableProductsRepository availablesRepo;

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
	
	@Override
	protected void populateModel(IndexVM model) {
		
		model.getFilter().setWhosAskingRole(checkRole());
		model.getFilter().setWhosAskingId(getLoggedUser().getId());
	}

	@Override
	protected void populateEditPostModel(@Valid EditVM model) {
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public ResponseEntity<?> save(EditVM model) throws Exception{
		return userDetailsService.signup(model.registerRequest());
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public ResponseEntity<?> signup(RegisterRequest model) throws Exception{
		
		System.out.println("users service got signup request");
		return userDetailsService.signup(model);
	}
	
	@Transactional
	public ResponseEntity<?> delete(List<Long> ids) {
		
		List<User> items = repo().findAllById(ids);
		handleDeletingChilds(items);		
		repo().deleteAll(items);
		/************ in need of event to check parents children count ??????????????   **************////////////////
		return ResponseEntity.ok(ids);

	}

	
	

	@Override
	protected void handleDeletingChilds(List<User> items) {
		//test.removeIf(i -> i==2);
		for(User u : items) {
			handleDeletingChilds(u);
			if(u.getClass().isAssignableFrom(Employee.class)) {
				if(((Employee)u).getDeleted())
					items.removeIf(i -> i.getId().equals(u.getId()));
			}
		}
	}

	@Override
	protected boolean handleDeletingChilds(User e) {	
		//QUser u = QUser.user;
		//QEmployee q = u.as(QEmployee.class); 
		
		if(e.getErole().equals(ERole.ROLE_Mol)) {			
			
			List<ProductDetail> productDetails = (List<ProductDetail>)
					ProductDtsRepo.findAll(
							/*(QProductDetail.productDetail.user.mol.id.eq(e.getId()))
							.or(QProductDetail.productDetail.user.id.eq(e.getId()))*/
							QProductDetail.productDetail.deliveryDetail.product.userCategory.user.id.eq(e.getId())
							);
			//productDetailRepo.deleteAll(productDetails); //1
			
			List<User> emps = (List<User>) 
					repo.findAll(QUser.user.as(QEmployee.class).mol.id.eq(e.getId()));
			//repo.deleteAll(emps); //2
			
			List<DeliveryDetail> ddsList = (List<DeliveryDetail>) ddRepo
					.findAll(QDeliveryDetail.deliveryDetail.product.userCategory.user.id.eq(e.getId()));
					//.findAll(QDeliveryDetail.deliveryDetail.delivery.supplier.mol.id.eq(e.getId()));
			//ddRepo.deleteAll(ddsList); //3
			
			//List<AvailableProduct> availables = 
					//(List<AvailableProduct>) availablesRepo.findAll(QAvailableProduct.availableProduct.product.user.id.eq(e.getId()));//4
			
			ProductDtsRepo.deleteAll(productDetails); //1
			repo.deleteAll(emps); //2
			ddRepo.deleteAll(ddsList); //3
			
			List<Delivery> dsList = (List<Delivery>) dsRepo
					.findAll(QDelivery.delivery.supplier.user.id.eq(e.getId()));//****************** ?????????????????
			dsRepo.deleteAll(dsList);
			
			List<Supplier> sList = (List<Supplier>) sRepo
					.findAll(QSupplier.supplier.user.id.eq(e.getId()));
			sRepo.deleteAll(sList);
			
			//availablesRepo.deleteAll(availables);
			
			List<Product> products = (List<Product>) productsRepository.findAll(QProduct.product.userCategory.user.id.eq(e.getId()));
			productsRepository.deleteAll(products);
			
			
		}
		
		
		if(e.getErole().equals(ERole.ROLE_Employee)) {
			
			if(upRepo.count(QUserProfile.userProfile.user.id.eq(e.getId())) > 0) {
				((Employee)e).setDeleted(true);
				repo().save(e);
				return true;
			}
			
			/*List<ProductDetail> productDetails = (List<ProductDetail>) ProductDtsRepo
					.findAll(QProductDetail.productDetail.user.id.eq(e.getId()));
			
			System.out.println("product2.size = "+productDetails.size());
			
			if(productDetails.size() > 0) {
				for(ProductDetail productD : productDetails)
				{
					Long LoggedUserId = getLoggedUser().getId();
					productD.setUser(new InUser(LoggedUserId));
				}
			}
			
			ProductDtsRepo.saveAll(productDetails);		*/
		
		}
		return false;
	}

	@Override
	protected void populateEditGetModel(EditVM model) {

//		List<SelectItem> productTypes = getProductTypes();		
//		List<UserCategory> userCategories = (List<UserCategory>) userCategoryRepo.findAll(QUserCategory.userCategory.user.id.eq(getLoggedUser().getId()));			
//		//List<Category> categories = categoryRepo.findAll();
//		
//		//model.setCategories(categories);
//		model.setUserCategories(userCategories);
//		model.setProductTypes(productTypes);
		if(checkRole().equals(ERole.ROLE_Admin)) {
				Predicate p = Expressions.asBoolean(true).isTrue();
			List<SelectItem> countries =  getListItems( p, Country.class, "name","id", "country");
			List<SelectItem> cities =  getListItems( p, City.class, "name","id", "countryId","city");
			
			model.setCities(cities);
			model.setCountries(countries);
			
			if(model.getId() != null && model.getId() > 0) {
				City city = cityRepo.findOne(QCity.city.id.eq(
						JPAExpressions.selectFrom(QMOL.mOL).where(QMOL.mOL.id.eq(model.getId())).select(QMOL.mOL.city.id)
						)).get();
				model.setCityId(city.getId());
				model.setCountryId(city.getCountryId());
						}
		}
		
	}
	
	/*protected boolean setModel(IndexVM model, Predicate predicate, OrderSpecifier<?> sort) {
		
		if(model.isLongView()) {	
			boolean isAdmin = checkRole().equals(ERole.ROLE_Admin);
			PagerVM pager =  model.getPager();
			Long limit = (long) pager.getItemsPerPage();
			Long offset = pager.getPage() * limit;
			List<UserDAO> DAOs = isAdmin ? repoImpl.getDAOsLong(predicate, offset, limit) : 
				repoImpl.getDAOs(predicate, offset, limit);//, pager);
			model.setDAOItems(DAOs);
			
			Long totalCount = isAdmin ? repoImpl.DAOCountLong(predicate): repoImpl.DAOCount(predicate);//.fetchCount();
			pager.setItemsCount(totalCount);
			pager.setPagesCount((int) (totalCount % limit > 0 ? (totalCount/limit) + 1 : totalCount / limit));
			return true;
		}
		else return false;		
	}*/

	@Override
	protected Long setDAOItems(IndexVM model, Predicate predicate, Long offset, Long limit,
			OrderSpecifier<?> orderSpecifier) {
		boolean isAdmin = checkRole().equals(ERole.ROLE_Admin);
		List<UserDAO> DAOs = isAdmin ? repoImpl.getDAOsLong(predicate, offset, limit) : 
			repoImpl.getDAOs(predicate, offset, limit);//, pager);
		model.setDAOItems(DAOs);
		
		return isAdmin ? repoImpl.DAOCountLong(predicate): repoImpl.DAOCount(predicate);
	}
	
}
