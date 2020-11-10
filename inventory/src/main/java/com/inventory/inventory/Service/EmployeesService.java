package com.inventory.inventory.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.inventory.inventory.Model.Employee;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.User;
import com.inventory.inventory.Repository.EmployeesRepository;
import com.inventory.inventory.Repository.ProductsRepository;

@Service
public class EmployeesService {
	
	@Autowired
	private EmployeesRepository repo;
	
	@Autowired
	ProductsRepository productsRepository;
	
	public List<Employee> getEmployeesForUser(Long id) {
		 List<Employee> employees=repo.findByUserId(id);
		
	       if(employees.size()>0)return employees;
	       else return new ArrayList<>();
	       
	    }	
	
	public Employee getEmployeeById(long id) {
		return repo.findById(id).get();
		}
	 
	 public ResponseEntity<?> delete(Long id) {	
			Optional<Employee> existingEmployee=repo.findById(id);
			if(!existingEmployee.isPresent())
				return ResponseEntity
						.badRequest()
						.body("No record with that ID");
			
			List<Product> products=productsRepository.findByEmployeeId(id);
			if(products.size()>0) {
				for(Product product:products)
				{
					product.setEmployee((Employee)null);
				}
			}
			
			repo.deleteById(id);
			return ResponseEntity.ok(id);
		}
}
