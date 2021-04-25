package com.inventory.inventory.Service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventory.inventory.Exception.DuplicateNumbersException;
import com.inventory.inventory.Exception.NoParentFoundException;
import com.inventory.inventory.Model.Category;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Repository.Interfaces.BaseRepository;
import com.inventory.inventory.Repository.Interfaces.CategoryRepository;
import com.inventory.inventory.ViewModels.Category.EditVM;
import com.inventory.inventory.ViewModels.Category.FilterVM;
import com.inventory.inventory.ViewModels.Category.IndexVM;
import com.inventory.inventory.ViewModels.Category.OrderBy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;

@Service
public class CategoryService extends BaseService<Category, FilterVM, OrderBy, IndexVM, EditVM>{

	@Autowired
	CategoryRepository repo;
	
	@Override
	protected BaseRepository<Category> repo() {
		return repo;
	}

	@Override
	protected Category newItem() {
		return new Category();
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
	}

	@Override
	protected void populateEditGetModel(EditVM model) {
	}

	@Override
	protected void populateEditPostModel(@Valid EditVM model)
			throws DuplicateNumbersException, NoParentFoundException, Exception {
	}
	
	protected void dealWithEnumDropDowns(IndexVM model) {
		model.getFilter().setProductTypes(getProductTypes());
	}

	@Override
	protected Long setDAOItems(IndexVM model, Predicate predicate, Long offset, Long limit,
			OrderSpecifier<?> orderSpecifier) {
		return null;
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
	
	
}


