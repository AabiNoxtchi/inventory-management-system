package com.inventory.inventory.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory.Model.Employee;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.UpdatedProductResponse;
import com.inventory.inventory.Model.User;
import com.inventory.inventory.Service.ProductsService;


@RestController
@RequestMapping("/products")
public class ProductsController {
	
	@Autowired
	private ProductsService service;
	
	@GetMapping
	public List<Product> list(){	
		return service.listAll();
		}
	
	/*---get all products for one user---*/	
    @GetMapping("/{id}")
    public List<Product> getProductsByUser(@PathVariable("id") long id) {
    	
        List<Product> list=service.getProductsForUser(id);     
        return list;
    }    
    
    /*---get all products for one user by product type---*/
    @GetMapping("/{id}/{producttype}")
    public List<Product> getProductsByUser(@PathVariable("id") long id,
    		 @PathVariable("producttype") ProductType productType) {
    	
        List<Product> list=service.getProductsForUser(id,productType);     
        return list;
    }    
    
    /*---get all products for one user by is discarded---*/
    @GetMapping("/{id}/discarded/{discarded}")
    public List<Product> getProductsByUser(@PathVariable("id") long id,@PathVariable("discarded") boolean discarded) {
    	
        List<Product> list=service.getProductsForUser(id,discarded);     
        return list;
    }    
    
    /*---get all products for one user by is available---*/
    @GetMapping("/{id}/available/{available}")
    public List<Product> getAvailableProductsByUser(@PathVariable("id") long id,@PathVariable("available") boolean available) {    	
        List<Product> list=service.getAvailableProductsForUser(id,available);    	  	
        return list;
    }      
    
    /*---get all products for one user by employee---*/
    @GetMapping("/{id}/employee/{employeeid}")
    public List<Product> getProductsByUser(@PathVariable("id") long id,@PathVariable("employeeid") Long employeeId) {
    	
        List<Product> list=service.getProductsForEmployee(employeeId);     
        return list;
    }  
    
    @PostMapping("/add/{id}")
	public ResponseEntity<String> add(@RequestBody Product product,@PathVariable("id") long id) {
    	product.setUser(new User(id));
		return service.save(product);     	
    }
    
    @PutMapping("/{id}")
	public ResponseEntity<UpdatedProductResponse> update(@RequestBody Product product,
			@PathVariable("id") long id) {
    	
    	product.setUser(new User(id));
		return service.update(product);
	}    
    
    @PutMapping("/{id}/addemployee/{employeeid}")
   	public ResponseEntity<UpdatedProductResponse> updateForEmployee(@RequestBody Product product,
   			@PathVariable("employeeid") long employeeId
   			,@PathVariable("id") long id){
    	
    	product.setUser(new User(id));
       	product.setEmployee(new Employee(employeeId));
       	
   		return service.update(product);
   	}   
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        return service.delete(id);
       
    }
	
}
