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
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;


@Service
public class UserCategoryService extends BaseService<UserCategory, FilterVM, OrderBy, IndexVM, EditVM>{

	@Autowired
	UserCategoryRepository repo;
	
	@Override
	protected BaseRepository<UserCategory> repo() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	protected UserCategory newItem() {
		// TODO Auto-generated method stub
		return new UserCategory();
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
		//p=(model.getId()!=null && model.getId()>0&&model.getCategoryId()!=null)?((BooleanExpression) p).or(QCategory.category.id.eq(model.getCategoryId())):p;
		p=(model.getId()!=null && model.getId()>0&&model.getCategoryId()!=null)?QCategory.category.id.eq(model.getCategoryId()):p;
		List<SelectItem> names = getListItems(
				p, Category.class, "name", "id", "productType", "category");
		model.setNames(names);
		//if(model.getId()!=null && model.getId()>0)
			//names.add(newSelectItem(model.getId().toString(),model.get))
		
	}

	@Override
	protected void populateEditPostModel(@Valid EditVM model)
			throws DuplicateNumbersException, NoParentFoundException, Exception {
		model.setUserId(getLoggedUser().getId());
		
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
