package com.inventory.inventory.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Service.BaseService;
import com.inventory.inventory.Service.ProductDetailsService;
import com.inventory.inventory.Service.ProductsService;
import com.inventory.inventory.ViewModels.Product.EditVM;
import com.inventory.inventory.ViewModels.Product.FilterVM;
import com.inventory.inventory.ViewModels.Product.IndexVM;
import com.inventory.inventory.ViewModels.Product.OrderBy;

@RestController
@RequestMapping(value = "${app.BASE_URL}/products")
public class ProductsController extends BaseController<Product, FilterVM, OrderBy, IndexVM, EditVM> {

	@Autowired
	private ProductsService service;

	@Override
	protected BaseService<Product, FilterVM, OrderBy, IndexVM, EditVM> service() {
		return service;
	}
	
}
