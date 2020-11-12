package com.inventory.inventory.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.inventory.inventory.Model.Employee;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.UpdatedProductResponse;
import com.inventory.inventory.Model.User;
import com.inventory.inventory.Repository.ProductsRepository;
import com.inventory.inventory.auth.Models.UserDetailsImpl;
import com.querydsl.core.types.Predicate;

@Service
public class ProductsService {
	
	@Autowired
	private ProductsRepository repo;
	
	public List<Product> listAll(){		
		return repo.findAll();		
	}
	
	
	  public Iterable<Product> listAll(Predicate predicate){ 
		  return repo.findAll(predicate);
	  }
	 
	
	public List<Product> getProductsForUser(Long id){
		return getProductsForUser(id,null);
	}
	
	 public List<Product> getProductsForUser(Long id,@Nullable ProductType productType) {
		 List<Product> products;		
		 if(productType!=null) {
			 products=repo.findByUserIdAndProductType(id,productType);			 
		 }
		 else {
	        products = repo.findByUserId(id);		   
		 }		 
	        if(products.size() > 0) {
	            return products;
	        } else {
	            return new ArrayList<>();
	        }
	    }
	 
	 public List<Product> getProductsForUser(Long id,boolean discarded) {
		     List<Product> products;		
			 products=repo.findByUserIdAndIsDiscarded(id,discarded);
	        if(products.size() > 0) {
	            return products;
	        } else {
	            return new ArrayList<>();
	        }
	    }
	 
	 public List<Product> getAvailableProductsForUser(long id, boolean available) {
			 List<Product> products;
			 products=repo.findByUserIdAndIsAvailable(id,available);
	         if(products.size() > 0) {
	           return products;
	         } else {
	           return new ArrayList<>();
	         }
		}
	 
	 public List<Product> getProductsForEmployee(Long employeeId) {
		 List<Product> products;
		 products=repo.findByEmployeeId(employeeId);
       if(products.size() > 0) {
           return products;
       } else {
           return new ArrayList<>();
       }
	}
	 
	 public List<Product> getProductsByIdIn(ArrayList<Long> ids) {
			
			return repo.findByIdIn(ids);
		}
	 
	 public ResponseEntity<UpdatedProductResponse> save(Product product) {
			return save(product,null);
			
		}

	 public ResponseEntity<UpdatedProductResponse> save(Product product,Long employeeId) {
		 
		 UpdatedProductResponse updatedProductResponse=new UpdatedProductResponse();
		 updatedProductResponse.setId(product.getId());
		 updatedProductResponse.setProductName(product.getName());		
		 
		 Long userId=((UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
		 product.setUser(new User(userId));
		 
		 if(product.getId()!=null&&product.getId()>0)
		 {
			 Optional<Product> existing=repo.findById(product.getId());
			 if(!existing.isPresent())throw new NullPointerException("No record with that ID");
			 else {				    
				 if( !existing.get().isDiscarded()&&product.isDiscarded()) {					 
					 updatedProductResponse.setDiscarded(true);		 
				 }
				 if(existing.get().getProductType()==ProductType.DMA && product.getProductType()==ProductType.MA)
				 {
					 updatedProductResponse.setConvertedToMA(true);					
				 }
				 if(existing.get().getEmployee()!=null ){ 	
					 product.setEmployee(existing.get().getEmployee()); 
		         }				
		     } 
			 updatedProductResponse.setResponse("updated");
	    }
		 else
			 updatedProductResponse.setResponse("saved");
		if(employeeId!=null&&employeeId>0)
	    		product.setEmployee(new Employee(employeeId));
	    else if(employeeId!=null&&employeeId==0) 
	    		product.setEmployee(null); 	   
		 repo.save(product);
		 if(product.getEmployee()!=null) 
			    updatedProductResponse.setEmployeeId(product.getEmployee().getId());			   
		 return ResponseEntity.ok(updatedProductResponse);
 }	
	 
	 public ResponseEntity<?> delete(Long id) {
		 Optional<Product> existingProduct=repo.findById(id);
			if(!existingProduct.isPresent())
				return ResponseEntity
						.badRequest()
						.body("No record with that ID");			
			repo.deleteById(id);
			return ResponseEntity.ok(id);
			
		}
	 
}
