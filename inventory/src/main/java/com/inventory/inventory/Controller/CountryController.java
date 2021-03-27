package com.inventory.inventory.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory.Exception.DuplicateNumbersException;
import com.inventory.inventory.Exception.NoChildrensFoundException;
import com.inventory.inventory.Exception.NoParentFoundException;
import com.inventory.inventory.Model.Country;
import com.inventory.inventory.Service.BaseService;
import com.inventory.inventory.Service.CountryService;
import com.inventory.inventory.ViewModels.Country.CityEditVM;
import com.inventory.inventory.ViewModels.Country.EditVM;
import com.inventory.inventory.ViewModels.Country.FilterVM;
import com.inventory.inventory.ViewModels.Country.IndexVM;
import com.inventory.inventory.ViewModels.Country.OrderBy;


@RestController
@RequestMapping(value = "${app.BASE_URL}/countries")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
public class CountryController extends BaseController<Country, FilterVM, OrderBy, IndexVM, EditVM>{

	@Autowired
	CountryService service;
	
	@Override
	protected BaseService<Country, FilterVM, OrderBy, IndexVM, EditVM> service() {
		// TODO Auto-generated method stub
		return service;
	}
	
	@DeleteMapping("/child/{childid}")
	@PreAuthorize("this.checkDeleteAuthorization()")
	public ResponseEntity<?> deletechild( @PathVariable Long childid) throws Exception {	
			return service.deleteChild(childid);
	}
	
	@GetMapping("/child/{id}")
    @PreAuthorize("this.checkGetAuthorization()")
	public ResponseEntity<?> getChild(@PathVariable("id") Long id) {    	
		 return service.getChild(id);
	}
	
	@PutMapping("/child") 
    @PreAuthorize("this.checkSaveAuthorization()")
	public ResponseEntity<?> saveChild(@RequestBody com.inventory.inventory.ViewModels.City.EditVM model) throws Exception{
    	System.out.println("in save model");
    	try {    	
    		return service.saveChild(model);	
    	}catch(Exception e) {
        	//System.out.println("e = "+e);
        	return exceptionResponse(e.getMessage());
        }
         
    }
}

