package com.inventory.inventory.Service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventory.inventory.Model.Category;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.SubCategory;
import com.inventory.inventory.Repository.RepositoryImpl;
import com.inventory.inventory.Repository.Interfaces.BaseRepository;
import com.inventory.inventory.Repository.Interfaces.CategoryRepository;
import com.inventory.inventory.Repository.Interfaces.ProductsRepository;
import com.inventory.inventory.Repository.Interfaces.SubCategoryRepository;
import com.inventory.inventory.ViewModels.Product.EditVM;
import com.inventory.inventory.ViewModels.Product.FilterVM;
import com.inventory.inventory.ViewModels.Product.IndexVM;
import com.inventory.inventory.ViewModels.Product.OrderBy;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;

@Service
public class ProductsService extends BaseService<Product, FilterVM, OrderBy, IndexVM, EditVM> {
	
	private static final Logger logger = LoggerFactory.getLogger("Product Service");

	@Autowired
	private ProductsRepository repo;
	
	@Autowired
	RepositoryImpl repoImpl;
	
	@Autowired
	SubCategoryRepository subCategoryRepo;

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
	}
	
	@Override
	protected void populateEditGetModel(EditVM model) {
		
		Predicate p = Expressions.asBoolean(true).isTrue();
		List<SelectItem> productTypes = getProductTypes();		
		List<SubCategory> subCategories = subCategoryRepo.findAll();			
		List<Category> categories = categoryRepo.findAll();
		
		model.setCategories(categories);
		model.setSubCategories(subCategories);
		model.setProductTypes(productTypes);
		
	}
	
	@Override
	protected void populateEditPostModel(@Valid EditVM model) {		
		model.setUserId(getLoggedUser().getId());		
	}	
	
	private List<SelectItem> getProductTypes(){
		List<SelectItem> productTypes = new ArrayList<>();
		SelectItem item = new SelectItem(ProductType.DMA.name(), ProductType.DMA.name());
		SelectItem item2 = new SelectItem(ProductType.MA.name(), ProductType.MA.name());
		productTypes.add(item);		
		productTypes.add(item2);
		
		return productTypes;
	}
	
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

	
	
	 
}









