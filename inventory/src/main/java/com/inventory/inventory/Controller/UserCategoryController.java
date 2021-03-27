package com.inventory.inventory.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory.Model.UserCategory;
import com.inventory.inventory.Service.BaseService;
import com.inventory.inventory.Service.UserCategoryService;
import com.inventory.inventory.ViewModels.UserCategory.EditVM;
import com.inventory.inventory.ViewModels.UserCategory.FilterVM;
import com.inventory.inventory.ViewModels.UserCategory.IndexVM;
import com.inventory.inventory.ViewModels.UserCategory.OrderBy;

@RestController
@RequestMapping(value = "${app.BASE_URL}/usercategories")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
public class UserCategoryController extends BaseController<UserCategory, FilterVM, OrderBy, IndexVM, EditVM>{

	@Autowired
	UserCategoryService service;
	
	@Override
	protected BaseService<UserCategory, FilterVM, OrderBy, IndexVM, EditVM> service() {
		// TODO Auto-generated method stub
		return service;
	}

}