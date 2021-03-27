package com.inventory.inventory.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory.Model.Category;
import com.inventory.inventory.Service.BaseService;
import com.inventory.inventory.Service.CategoryService;
import com.inventory.inventory.ViewModels.Category.EditVM;
import com.inventory.inventory.ViewModels.Category.FilterVM;
import com.inventory.inventory.ViewModels.Category.IndexVM;
import com.inventory.inventory.ViewModels.Category.OrderBy;

@RestController
@RequestMapping(value = "${app.BASE_URL}/categories")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
public class CategoryController extends BaseController<Category, FilterVM, OrderBy, IndexVM, EditVM>{

	@Autowired
	CategoryService service;
	
	@Override
	protected BaseService<Category, FilterVM, OrderBy, IndexVM, EditVM> service() {
		// TODO Auto-generated method stub
		return service;
	}

}
