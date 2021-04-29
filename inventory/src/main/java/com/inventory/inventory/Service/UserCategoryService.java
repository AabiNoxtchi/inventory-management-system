package com.inventory.inventory.Service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventory.inventory.Events.EventHandler;
import com.inventory.inventory.Exception.DuplicateNumbersException;
import com.inventory.inventory.Exception.NoChildrensFoundException;
import com.inventory.inventory.Exception.NoParentFoundException;
import com.inventory.inventory.Model.Category;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.QCategory;
import com.inventory.inventory.Model.QUserCategory;
import com.inventory.inventory.Model.UserCategory;
import com.inventory.inventory.Repository.Interfaces.BaseRepository;
import com.inventory.inventory.Repository.Interfaces.UserCategoryRepository;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.inventory.inventory.ViewModels.UserCategory.EditVM;
import com.inventory.inventory.ViewModels.UserCategory.FilterVM;
import com.inventory.inventory.ViewModels.UserCategory.IndexVM;
import com.inventory.inventory.ViewModels.UserCategory.OrderBy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;


@Service
public class UserCategoryService extends BaseService<UserCategory, FilterVM, OrderBy, IndexVM, EditVM>{

	@Autowired
	UserCategoryRepository repo;
	
	@Autowired
	EventHandler eventHandler;
	
	@Override
	protected BaseRepository<UserCategory> repo() {
		return repo;
	}

	@Override
	protected UserCategory newItem() {
		return new UserCategory();
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
		model.getFilter().setUserId(getLoggedUser().getId());		
	}

	@Override
	protected void populateEditGetModel(EditVM model) {
		QUserCategory uc = QUserCategory.userCategory;
		Predicate p = QCategory.category.id.notIn(
				JPAExpressions.selectFrom(uc).where(
						uc.userId.eq(getLoggedUser().getId())
						).select(uc.category.id)
				);
			p = (model.getId()!=null && model.getId()>0&&model.getCategoryId()!=null)?QCategory.category.id.eq(model.getCategoryId()):p;
		List<SelectItem> names = getListItems(
				p, Category.class, "name", "id", "productType", "category");
		
		names.remove(0);		
		model.setNames(names);		
	}

	@Override
	protected void populateEditPostModel(@Valid EditVM model)
			throws DuplicateNumbersException, NoParentFoundException, Exception {
		model.setUserId(getLoggedUser().getId());		
	}
	
	protected void handleAfterSave(EditVM model, UserCategory item) 
			throws DuplicateNumbersException, NoChildrensFoundException, NoParentFoundException, Exception {
		if(model.getId() != null && model.getId() > 0) {
			eventHandler.onAmortizationChanged(getLoggedUser().getId(), item.getId());
		}		
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
		return checkRole().equals(ERole.ROLE_Mol) ;
	}

	@Override
	public Boolean checkSaveAuthorization() {
		return checkRole().equals(ERole.ROLE_Mol) ;
	}

	@Override
	public Boolean checkDeleteAuthorization() {
		return checkRole().equals(ERole.ROLE_Mol) ;
	}
	
	

	

}
