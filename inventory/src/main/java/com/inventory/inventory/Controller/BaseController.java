package com.inventory.inventory.Controller;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inventory.inventory.Exception.DuplicateNumbersException;
import com.inventory.inventory.Exception.NoChildrensFoundException;
import com.inventory.inventory.Exception.NoParentFoundException;
import com.inventory.inventory.Model.BaseEntity;
import com.inventory.inventory.Service.BaseService;
import com.inventory.inventory.ViewModels.Shared.BaseEditVM;
import com.inventory.inventory.ViewModels.Shared.BaseFilterVM;
import com.inventory.inventory.ViewModels.Shared.BaseIndexVM;
import com.inventory.inventory.ViewModels.Shared.BaseOrderBy;

@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
public abstract class BaseController <E extends BaseEntity , F extends BaseFilterVM,O extends BaseOrderBy,
							IndexVM extends BaseIndexVM<E, F, O>, EditVM extends BaseEditVM<E>> {
	
	protected abstract BaseService< E , F , O, IndexVM , EditVM> service();
	
	private ResponseEntity<?> exceptionResponse(Exception e) {
		// TODO Auto-generated method stub
		return ResponseEntity		
 				.badRequest()
 				.body(e.getMessage());
	}
	
	protected ResponseEntity<?> errorsResponse(EditVM model) {
 		// TODO Auto-generated method stub
    	 return ResponseEntity		
 				.badRequest()
 				.body("number already exist , save failed !!!");
 	}
	
	 public Boolean checkGetAuthorization() {return service().checkGetAuthorization();}
     public Boolean checkSaveAuthorization() {return service().checkSaveAuthorization();}
     public Boolean checkDeleteAuthorization() {return service().checkDeleteAuthorization();}
     
     
    
    @GetMapping
	@ResponseBody 
	@PreAuthorize("this.checkGetAuthorization()")
	public  ResponseEntity<IndexVM> getAll(IndexVM model) {     	
		 return  service().getAll(model); 
	}
    
    @GetMapping("/{id}")
    @PreAuthorize("this.checkGetAuthorization()")
	public ResponseEntity<EditVM> get(@PathVariable("id") Long id) {    	
		 return service().get(id);
	}
    
    @PutMapping() 
    @PreAuthorize("this.checkSaveAuthorization()")
	public ResponseEntity<?> save(@RequestBody EditVM model) throws Exception{
    	System.out.println("in save model");
    	try {    	
    		return service().save(model);	
    	}catch(DuplicateNumbersException e) {
    		return errorsResponse(model);        	      	
        }catch(NoChildrensFoundException e) {
        	return ResponseEntity		
    				.badRequest()
    				.body("no childrens found");
        }catch(NoParentFoundException e) {
        	return ResponseEntity		
    				.badRequest()
    				.body("no parent found");
        }/*catch(Exception e) {
        	System.out.println("e = "+e);
        	return ResponseEntity		
    				.badRequest()
    				.body(e.getMessage());
        }*/
         
    }

	@DeleteMapping("/ids/{ids}")
    @PreAuthorize("this.checkDeleteAuthorization()")
	public ResponseEntity<?> delete( @PathVariable ArrayList<Long> ids ) {    	
			return service().delete(ids);
	}
	  
	@DeleteMapping("/{id}")
	@PreAuthorize("this.checkDeleteAuthorization()")
	public ResponseEntity<?> delete( @PathVariable Long id) {		
			try {
				return service().delete(id);
			} catch (Exception e) {				
				return exceptionResponse(e);
			}
	}

	

}
