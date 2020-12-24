package com.inventory.inventory.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory.Model.Supplier;
import com.inventory.inventory.Service.BaseService;
import com.inventory.inventory.Service.SuppliersService;
import com.inventory.inventory.ViewModels.Supplier.EditVM;
import com.inventory.inventory.ViewModels.Supplier.FilterVM;
import com.inventory.inventory.ViewModels.Supplier.IndexVM;
import com.inventory.inventory.ViewModels.Supplier.OrderBy;

@RestController
@RequestMapping("${app.BASE_URL}/suppliers")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
public class SuppliersController extends BaseController<Supplier, FilterVM, OrderBy, IndexVM, EditVM>{

	@Autowired
	SuppliersService service;
	@Override
	protected BaseService<Supplier, FilterVM, OrderBy, IndexVM, EditVM> service() {
		
		return service;
	}

}
