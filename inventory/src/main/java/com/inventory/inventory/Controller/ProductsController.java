package com.inventory.inventory.Controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Service.BaseService;
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
	
	 @GetMapping("/selectProducts")
	 @ResponseBody 
	 @PreAuthorize("this.checkGetAuthorization()")
	 public  ResponseEntity<?> getselectProducts() {     	
		return  service.getselectProducts(); 
	}
		
	
	@DeleteMapping("/nullify/{ids}") 
    @PreAuthorize("this.checkSaveAuthorization()")
	public ResponseEntity<?> nullifyEmployees(@PathVariable ArrayList<Long> ids){		  
		 return service.nullifyEmployees(ids);		  
    }
	
	
}
