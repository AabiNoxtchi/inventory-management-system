package com.inventory.inventory.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory.Model.City;
import com.inventory.inventory.Service.BaseService;
import com.inventory.inventory.Service.CityService;
import com.inventory.inventory.ViewModels.City.EditVM;
import com.inventory.inventory.ViewModels.City.FilterVM;
import com.inventory.inventory.ViewModels.City.IndexVM;
import com.inventory.inventory.ViewModels.City.OrderBy;


@RestController
@RequestMapping(value = "${app.BASE_URL}/cities")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
public class CityController extends BaseController<City, FilterVM, OrderBy, IndexVM, EditVM>{

	@Autowired
	CityService service;
	
	@Override
	protected BaseService<City, FilterVM, OrderBy, IndexVM, EditVM> service() {
		return service;
	}

}
