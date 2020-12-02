package com.inventory.inventory.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inventory.inventory.Model.BaseEntity;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.UpdatedProductResponse;
import com.inventory.inventory.Service.BaseService;
import com.inventory.inventory.ViewModels.Shared.BaseEditVM;
import com.inventory.inventory.ViewModels.Shared.BaseFilterVM;
import com.inventory.inventory.ViewModels.Shared.BaseIndexVM;
import com.inventory.inventory.ViewModels.Shared.BaseOrderBy;

public abstract class BaseController <E extends BaseEntity , F extends BaseFilterVM,O extends BaseOrderBy,
							IndexVM extends BaseIndexVM<E, F, O>, EditVM extends BaseEditVM<E>> {
	
	protected abstract BaseService< E , F , O, IndexVM , EditVM> service();
	
	  public Boolean checkGetAuthorization() {return service().checkGetAuthorization();}
    //protected abstract Boolean checkGetAuthorization(); //{return service().checkGetAuthorization();}
     public Boolean checkSaveAuthorization() {return service().checkSaveAuthorization();}
     public Boolean checkDeleteAuthorization() {return service().checkDeleteAuthorization();}
    
    @GetMapping
	@ResponseBody 
	//@PreAuthorize("this.checkGetAuthorization()")
	@PreAuthorize("this.checkGetAuthorization()")
	public  ResponseEntity<IndexVM> getAll(IndexVM model) {     	
		 return  service().getAll(model); 
	}
    
    @GetMapping("/{id}")
    @PreAuthorize("this.checkGetAuthorization()")
	public ResponseEntity<EditVM> get(@PathVariable("id") Long id) {    	
		 return service().get(id);
	}
    
    @PutMapping("/save") 
    @PreAuthorize("this.checkSaveAuthorization()")
	public ResponseEntity<?> save(@RequestBody EditVM model){		  
		 return service().save(model);		  
    }
    
	
	/*
	 * @PutMapping("/save/{id}") public ResponseEntity<UpdatedProductResponse>
	 * save(@RequestBody Product product, @PathVariable("id") Long id) { //return
	 * service.save(product, id); }
	 */
	 
	  
    @DeleteMapping("/ids/{ids}")
    @PreAuthorize("this.checkDeleteAuthorization()")
	public ResponseEntity<?> delete( @PathVariable ArrayList<Long> ids ) {    	
			return service().delete(ids);
	}
	  
	@DeleteMapping("/id/{id}")
	@PreAuthorize("this.checkDeleteAuthorization()")
	public ResponseEntity<?> delete( @PathVariable Long id) {		
			return service().delete(id);
	}

}
