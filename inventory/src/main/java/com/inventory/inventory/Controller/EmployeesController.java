package com.inventory.inventory.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.inventory.inventory.Model.Employee;
import com.inventory.inventory.Model.User;
import com.inventory.inventory.Service.EmployeesService;


@RestController
@RequestMapping("/employees")
public class EmployeesController {
	
	@Autowired
	private EmployeesService service;
	
	/*---get all employees for one user---*/
    @GetMapping("/{id}")
    public List<Employee> getEmployeesByUser(@PathVariable("id") long id) {
    	
        List<Employee> list=service.getEmployeesForUser(id);    
    	//System.out.println(list.get(1).getFirstName()+" ,"+list.get(1).getUser().getUserName());    	
        return list;
    }  
    
    @PostMapping("/add/{id}")
   	public ResponseEntity<String> add(@RequestBody Employee employee,@PathVariable("id") long id) {
       	employee.setUser(new User());
       	employee.getUser().setId(id);    	
   		return service.save(employee);     	
       }
    
    @PutMapping("/{id}")
   	public ResponseEntity<String> update(@RequestBody Employee employee,@PathVariable("id") long id) {
       	employee.setUser(new User());
       	employee.getUser().setId(id);
       	
   		return service.save(employee);
   	}    
	

}
