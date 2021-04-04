package com.inventory.inventory.Service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.inventory.inventory.Model.Category;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.QUserCategory;
import com.inventory.inventory.Model.UserCategory;
import com.inventory.inventory.Repository.ProductRepositoryImpl;
import com.inventory.inventory.Repository.RepositoryImpl;
import com.inventory.inventory.Repository.Interfaces.BaseRepository;
import com.inventory.inventory.Repository.Interfaces.CategoryRepository;
import com.inventory.inventory.Repository.Interfaces.ProductsRepository;
import com.inventory.inventory.Repository.Interfaces.UserCategoryRepository;
import com.inventory.inventory.ViewModels.Product.EditVM;
import com.inventory.inventory.ViewModels.Product.FilterVM;
import com.inventory.inventory.ViewModels.Product.IndexVM;
import com.inventory.inventory.ViewModels.Product.OrderBy;
import com.inventory.inventory.ViewModels.Product.ProductDAO;
import com.inventory.inventory.ViewModels.ProductDetail.ProductDetailDAO;
import com.inventory.inventory.ViewModels.Shared.PagerVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;

@Service
public class ProductsService extends BaseService<Product, FilterVM, OrderBy, IndexVM, EditVM> {
	
	private static final Logger logger = LoggerFactory.getLogger("Product Service");

	@Autowired
	private ProductsRepository repo;
	
	@Autowired
	ProductRepositoryImpl repoImpl;
	
	
	
	@Autowired
	UserCategoryRepository userCategoryRepo;

	@Autowired
	CategoryRepository categoryRepo;
	
	@Override
	protected BaseRepository<Product> repo() {
		return repo;
	}
	
	@Override
	protected Product newItem() {
		return new Product();
	}

	@Override
	protected FilterVM filter() {
		return new FilterVM();
	}

	@Override
	protected OrderBy orderBy() {
		return new OrderBy();
	}
	
	@Override
	protected EditVM editVM() {
		return new EditVM();
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
		  ERole role =  checkRole(); return role.equals(ERole.ROLE_Mol) ; 
	}
	
	protected void populateModel(IndexVM model) {		
		Long id = getLoggedUser().getId();
		model.getFilter().setUserId(id);
		
		FilterVM vm =  model.getFilter();
		Predicate p = QUserCategory.userCategory.user.id.eq(getLoggedUser().getId());
		
		vm.setUserCategories(repoImpl.selectCategoryItems(p));
	}
	
	/*protected boolean setModel(IndexVM model, Predicate predicate, Sort sort) {
		
		if(model.isLongView()) {			
			PagerVM pager =  model.getPager();
			Long limit = (long) pager.getItemsPerPage();
			Long offset = pager.getPage() * limit;
			List<ProductDAO> DAOs = repoImpl.getDAOs(predicate, offset, limit);//, pager);
			model.setDAOItems(DAOs);
			
			Long totalCount = repoImpl.DAOCount(predicate);//.fetchCount();
			pager.setItemsCount(totalCount);
			pager.setPagesCount((int) (totalCount % limit > 0 ? (totalCount/limit) + 1 : totalCount / limit));
			return true;
		}
		else return false;		
	}*/
	
	@Override
	protected void populateEditGetModel(EditVM model) {
		
		Predicate p = Expressions.asBoolean(true).isTrue();
		List<SelectItem> productTypes = getProductTypes();		
		List<UserCategory> userCategories = (List<UserCategory>) userCategoryRepo.findAll(QUserCategory.userCategory.user.id.eq(getLoggedUser().getId()));
	
		//List<Category> categories = categoryRepo.findAll();
		
		//model.setCategories(categories);
		model.setUserCategories(userCategories);
		model.setProductTypes(productTypes);
		
		if(model.getUserCategoryId() == null) return;
		model.setProductType(
				userCategories.stream()
				.filter(uc -> uc.getId().equals(model.getUserCategoryId()))
						.findFirst().get().getCategory().getProductType()
						);
		double percent = userCategories.stream().filter(uc -> uc.getId() == model.getUserCategoryId()).findFirst().get().getAmortizationPercent();
		model.setAmortizationPercent(percent);
		
		
	}
	
	@Override
	protected void populateEditPostModel(@Valid EditVM model) {		
		//model.setUserId(getLoggedUser().getId());		
	}	
	
	/*private List<SelectItem> getProductTypes(){
		List<SelectItem> productTypes = new ArrayList<>();
		SelectItem item = new SelectItem(ProductType.LTA.name(), ProductType.LTA.name());
		SelectItem item2 = new SelectItem(ProductType.STA.name(), ProductType.STA.name());
		productTypes.add(item);		
		productTypes.add(item2);
		
		return productTypes;
	}*/
	
	protected void dealWithEnumDropDowns(IndexVM model) {
		model.getFilter().setProductTypes(getProductTypes());
	}
	
	 @Override	 
	 protected void handleDeletingChilds(List<Product> items) { 		
	  //Auto-generated method stub	  
	 }

	@Override
	protected void handleDeletingChilds(Product e) {
		// TODO Auto-generated method stub		
	}

	@Override
	protected Long setDAOItems(IndexVM model, Predicate predicate, Long offset, Long limit,
			OrderSpecifier<?> orderSpecifier) {
		List<ProductDAO> DAOs = repoImpl.getDAOs(predicate, offset, limit);//, pager);
		model.setDAOItems(DAOs);
		
		return repoImpl.DAOCount(predicate);//.fetchCount();
	}

	
	
	 
}









