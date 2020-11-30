package com.inventory.inventory.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inventory.inventory.Model.BaseEntity;
import com.inventory.inventory.Service.BaseService;
import com.inventory.inventory.ViewModels.Shared.BaseFilterVM;
import com.inventory.inventory.ViewModels.Shared.BaseIndexVM;
import com.inventory.inventory.ViewModels.Shared.BaseOrderBy;

public abstract class BaseController <E extends BaseEntity , F extends BaseFilterVM,O extends BaseOrderBy,
							IndexVM extends BaseIndexVM<E, F, O>> {
	
	
	  protected abstract BaseService< E , F , O, IndexVM > service();
	  public abstract Boolean checkGetAuthorization();
	  
	  //@RequestMapping(method = RequestMethod.GET)	 
	  @GetMapping
	  @ResponseBody 
	  @PreAuthorize("this.checkGetAuthorization()")
	  public  ResponseEntity<IndexVM> getAll(IndexVM model) { 
		  return  service().getAll(model); 
		  }
	  
	  @DeleteMapping("/ids/{productsIds}")
	public ResponseEntity<?> delete( @PathVariable("productsIds")  ArrayList<Long> ids ) {
		  System.out.println("in delete ids");
			return service().delete(ids);
		}
	  
	  @DeleteMapping("/id")
	public ResponseEntity<?> delete(/* @PathVariable */ Long id) {
			System.out.println("in delete id");
			return service().delete(id);
		}

}
