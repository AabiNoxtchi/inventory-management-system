package com.inventory.inventory.Service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.QUserCategory;
import com.inventory.inventory.Model.UserCategory;
import com.inventory.inventory.Repository.ProductRepositoryImpl;
import com.inventory.inventory.Repository.Interfaces.BaseRepository;
import com.inventory.inventory.Repository.Interfaces.ProductsRepository;
import com.inventory.inventory.Repository.Interfaces.UserCategoryRepository;
import com.inventory.inventory.ViewModels.Product.EditVM;
import com.inventory.inventory.ViewModels.Product.FilterVM;
import com.inventory.inventory.ViewModels.Product.IndexVM;
import com.inventory.inventory.ViewModels.Product.OrderBy;
import com.inventory.inventory.ViewModels.Product.ProductDAO;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;

@Service
public class ProductsService extends BaseService<Product, FilterVM, OrderBy, IndexVM, EditVM> {

	@Autowired
	private ProductsRepository repo;
	
	@Autowired
	ProductRepositoryImpl repoImpl;
	
	@Autowired
	UserCategoryRepository userCategoryRepo;
	
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
	
	protected void populateModel(IndexVM model) {		
		Long id = getLoggedUser().getId();
		model.getFilter().setUserId(id);
		
		FilterVM vm =  model.getFilter();
		Predicate p = QUserCategory.userCategory.user.id.eq(getLoggedUser().getId());
		
		vm.setUserCategories(repoImpl.selectCategoryItems(p));
	}	
	
	@Override
	protected void populateEditGetModel(EditVM model) {
		
		List<SelectItem> productTypes = getProductTypes();		
		List<UserCategory> userCategories = (List<UserCategory>) userCategoryRepo.findAll(QUserCategory.userCategory.user.id.eq(getLoggedUser().getId()));	
		
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
	}
	
	protected void dealWithEnumDropDowns(IndexVM model) {
		model.getFilter().setProductTypes(getProductTypes());
	}

	@Override
	protected Long setDAOItems(IndexVM model, Predicate predicate, Long offset, Long limit,
			OrderSpecifier<?> orderSpecifier) {
		List<ProductDAO> DAOs = repoImpl.getDAOs(predicate, offset, limit);
		model.setDAOItems(DAOs);		
		return repoImpl.DAOCount(predicate);
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

	
	
	 
}









