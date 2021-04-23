package com.inventory.inventory.Controller;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import javax.validation.Valid;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
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
	
	protected ResponseEntity<?> exceptionResponse(String msg) {
		// TODO Auto-generated method stub
		return ResponseEntity		
 				.badRequest()
 				.body(msg);
	}
	
	protected ResponseEntity<?> exceptionResponse(DataIntegrityViolationException e, String method) {
		
		Throwable error = e.getMostSpecificCause();
		
    	String msg = error.getMessage();
    	System.out.println("error.getMessage() = "+msg);
    	
    	if(msg.contains("Duplicate entry"))
    		msg = "Duplicate entry "+msg.substring(msg.indexOf("for key"), msg.length()); 
    	
    	if(msg.contains("ddcNumber"))msg = msg.replace("ddcNumber", "DDC number"); 
    	
    	if(msg.startsWith("Cannot delete or update a parent row")) {
    		
    		if(msg.contains("(`inventory`.`profile_detail`")) {
    			
    			msg = "Cannot delete item with remaining owings";
    		}
    	}
    	
    	//e.printStackTrace();
    	return exceptionResponse("errors occured while triyng to "+method+" data, "+msg+" !!!");
	}
	
	protected ResponseEntity<?> errorsResponse(EditVM model) {
 		// TODO Auto-generated method stub
    	 return ResponseEntity		
 				.badRequest()
 				.body("number already exist , save failed !!!");
 	}
	
	 public Boolean checkGetAuthorization() {return service().checkGetAuthorization();}
	 public Boolean checkGetItemAuthorization() {return service().checkGetItemAuthorization();}
     public Boolean checkSaveAuthorization() {return service().checkSaveAuthorization();}
     public Boolean checkDeleteAuthorization() {return service().checkDeleteAuthorization();}
     
     
    
    @GetMapping
	@ResponseBody 
	@PreAuthorize("this.checkGetAuthorization()")
	public  ResponseEntity<IndexVM> getAll(IndexVM model) {   
    	System.out.println("get all base controller");
		 return  service().getAll(model); 
	}
    
    @GetMapping("/{id}")
    @PreAuthorize("this.checkGetItemAuthorization()")
	public ResponseEntity<EditVM> get(@PathVariable("id") Long id) throws Exception {  
    	
		 return service().get(id);
	}
    
    @PutMapping() 
    @PreAuthorize("this.checkSaveAuthorization()")
	public ResponseEntity<?> save(@RequestBody @Valid EditVM model) throws Exception{
    	System.out.println("in base controller save model");
    	try {    	
    		return service().save(model);	
    	}catch(DuplicateNumbersException e) {
    		return errorsResponse(model);        	      	
        }catch(NoChildrensFoundException e) {
        	return exceptionResponse("no childrens found");
//        	return ResponseEntity		
//    				.badRequest()
//    				.body("no childrens found");
        }catch(NoParentFoundException e) {
        	return exceptionResponse("no parent found");
//        	return ResponseEntity		
//    				.badRequest()
//    				.body("no parent found");
        }catch(DataIntegrityViolationException e) {
        	
        	return exceptionResponse( e, "save");
        	//String msg = ""+e.getMostSpecificCause().getMessage();
        	
        	//System.out.println("sql data integrity exception caught ");
        	//System.out.println("exception,msg = "+e.getMessage());
        	//System.out.println("exception.cause = "+e.getCause());
        	//System.out.println("exception.localized = "+e.getLocalizedMessage());
        	//System.out.println("exception.e.getMostSpecificCause() = "+e.getMostSpecificCause());
        	
//        	Throwable error = e.getMostSpecificCause();
//        	//System.out.println("error.getMessage() = "+error.getMessage());
//        	//System.out.println("error.getLocalizedMessage() = "+error.getLocalizedMessage());
//        	//System.out.println("error.tostring() = "+error.toString());
//        	
//        	String msg = error.getMessage();
//        	System.out.println("error.getMessage() = "+msg);
//        	
//        	if(msg.contains("Duplicate entry"))
//        		msg = "Duplicate entry "+msg.substring(msg.indexOf("for key"), msg.length()); 
//        	
//        	if(msg.contains("ddcNumber"))msg = msg.replace("ddcNumber", "DDC number");
//        	
//        	
//        	//e.printStackTrace();
//        	return exceptionResponse("errors occured while triyng to save data, "+msg+" !!!");//please make sure you are not trying to delete arecord with related records !!!");
        }catch(Exception e) {
        	//System.out.println("e = "+e);
        	e.printStackTrace();
        	return exceptionResponse(e.getMessage());//errors occured, please try again later !!!");
        }
         
    }

	@DeleteMapping("/ids/{ids}")
    @PreAuthorize("this.checkDeleteAuthorization()")
	public ResponseEntity<?> delete( @PathVariable ArrayList<Long> ids ) throws Exception {    	
			return service().delete(ids);
	}
	  
	@DeleteMapping("/{id}")
	@PreAuthorize("this.checkDeleteAuthorization()")
	public ResponseEntity<?> delete( @PathVariable Long id) {		
			try {
				return service().delete(id);
			}catch(DataIntegrityViolationException e) {
				
				return exceptionResponse( e, "delete");
				
			}catch(SQLDataException e) {
			
				System.out.println("sqlException  ");
				return ResponseEntity		
		 				.badRequest()
		 				.body("can't delete item with associated records !!!");
			} catch (Exception e) {	
				//System.out.println("error = "+e.toString());
				e.printStackTrace();
				return exceptionResponse(e.getMessage());
			}
	}
	
	
}
