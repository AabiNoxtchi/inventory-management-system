package com.inventory.inventory.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory.Exception.DuplicateNumbersException;
import com.inventory.inventory.Exception.NoParentFoundException;
import com.inventory.inventory.Model.ProductDetail;
import com.inventory.inventory.Service.BaseService;
import com.inventory.inventory.Service.ProductDetailsService;
import com.inventory.inventory.ViewModels.ProductDetail.EditVM;
import com.inventory.inventory.ViewModels.ProductDetail.FilterVM;
import com.inventory.inventory.ViewModels.ProductDetail.IndexVM;
import com.inventory.inventory.ViewModels.ProductDetail.OrderBy;
import com.inventory.inventory.ViewModels.Shared.SelectItem;

@RestController
@RequestMapping(value = "${app.BASE_URL}/productdetails")
public class ProductDetailsController extends BaseController<ProductDetail, FilterVM, OrderBy, IndexVM, EditVM> {

	@Autowired
	private ProductDetailsService service;
	
	@Override
	protected BaseService<ProductDetail, FilterVM, OrderBy, IndexVM, EditVM> service() {
		// TODO Auto-generated method stub
		return service;
	}
	
	@GetMapping("/numbers")
	@ResponseBody 
	@PreAuthorize("this.checkGetAuthorization()")
	public ResponseEntity<?> getInventoryNumbers(FilterVM filter){
		
		return service.getInventoryNumbers(filter);
	}
	
	 @PutMapping("/number") 
	    @PreAuthorize("this.checkSaveAuthorization()")
		public ResponseEntity<?> saveNumber(@RequestBody EditVM model) throws DuplicateNumbersException, NoParentFoundException{
	    	System.out.println("in saveNumber model");
	    	try {    	
	    	return service.saveNumber(model.getSelectItem(), model.getDeliveryDetailId());	
	    	}catch(DuplicateNumbersException e) {
	    		return ResponseEntity		
	    				.badRequest()
	    				.body("duplicate number");      	      	
	        }catch(NoParentFoundException e){
	        	return ResponseEntity
	        			.badRequest()
	        			.body("parent not found !!!");
	        	
	        }
	         
	    }
//	
//	 @GetMapping("/selectProducts")
//	 @ResponseBody 
//	 @PreAuthorize("this.checkGetAuthorization()")
//	 public  ResponseEntity<?> getselectProducts() {     	
//		return  service.getselectProducts(); 
//	}
//	 
//	 @PostMapping("/fillIds") 
//	 @PreAuthorize("this.checkSaveAuthorization()")
//	 public ResponseEntity<?> fillIds(@RequestBody Selectable seletable){		  
//		return service.fillIds(seletable);		  
//	 }
//		
//	
//	@DeleteMapping("/nullify/{ids}") 
//    @PreAuthorize("this.checkSaveAuthorization()")
//	public ResponseEntity<?> nullifyEmployees(@PathVariable ArrayList<Long> ids){		  
//		 return service.nullifyEmployees(ids);		  
//    }
	
	

}