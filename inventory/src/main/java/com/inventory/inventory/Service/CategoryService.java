package com.inventory.inventory.Service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventory.inventory.Exception.DuplicateNumbersException;
import com.inventory.inventory.Exception.NoParentFoundException;
import com.inventory.inventory.Model.Category;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Repository.Interfaces.BaseRepository;
import com.inventory.inventory.Repository.Interfaces.CategoryRepository;
import com.inventory.inventory.ViewModels.Category.EditVM;
import com.inventory.inventory.ViewModels.Category.FilterVM;
import com.inventory.inventory.ViewModels.Category.IndexVM;
import com.inventory.inventory.ViewModels.Category.OrderBy;
import com.inventory.inventory.ViewModels.Shared.SelectItem;

@Service
public class CategoryService extends BaseService<Category, FilterVM, OrderBy, IndexVM, EditVM>{

	@Autowired
	CategoryRepository repo;
	
	@Override
	protected BaseRepository<Category> repo() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	protected Category newItem() {
		// TODO Auto-generated method stub
		return new Category();
	}

	@Override
	protected FilterVM filter() {
		// TODO Auto-generated method stub
		return new FilterVM();
	}

	@Override
	protected EditVM editVM() {
		// TODO Auto-generated method stub
		return new EditVM();
	}

	@Override
	protected OrderBy orderBy() {
		// TODO Auto-generated method stub
		return new OrderBy();
	}

	@Override
	protected void populateModel(IndexVM model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void populateEditGetModel(EditVM model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void populateEditPostModel(@Valid EditVM model)
			throws DuplicateNumbersException, NoParentFoundException, Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean checkGetAuthorization() {		
		return checkRole().equals(ERole.ROLE_Admin) ;
	}

	@Override
	public Boolean checkSaveAuthorization() {
		return checkRole().equals(ERole.ROLE_Admin) ;
	}

	@Override
	public Boolean checkDeleteAuthorization() {
		return checkRole().equals(ERole.ROLE_Admin) ;
	}
	
	private List<SelectItem> getProductTypes(){
		List<SelectItem> productTypes = new ArrayList<>();
		SelectItem item = new SelectItem(ProductType.LTA.name(), ProductType.LTA.name());
		SelectItem item2 = new SelectItem(ProductType.STA.name(), ProductType.STA.name());
		productTypes.add(item);		
		productTypes.add(item2);
		
		return productTypes;
	}
	
	protected void dealWithEnumDropDowns(IndexVM model) {
		model.getFilter().setProductTypes(getProductTypes());
	}

}
