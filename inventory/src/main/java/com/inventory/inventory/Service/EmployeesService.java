package com.inventory.inventory.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.inventory.inventory.Model.Employee;
import com.inventory.inventory.Repository.EmployeesRepository;

@Service
public class EmployeesService {
	
	@Autowired
	private EmployeesRepository repo;
	
	public List<Employee> getEmployeesForUser(Long id) {
		 List<Employee> employees=repo.findByUserId(id);
		
	       if(employees.size()>0)return employees;
	       else return new ArrayList<>();
	       
	    }
	
	 public ResponseEntity<String> save(Employee employee){
		 repo.save(employee);
		 String success = "{ \"success\": true }";		 
		 return ResponseEntity.ok(success);		 
		 
	 }

}
