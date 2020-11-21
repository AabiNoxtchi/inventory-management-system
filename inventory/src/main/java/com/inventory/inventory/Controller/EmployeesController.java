package com.inventory.inventory.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.inventory.inventory.Model.Employee;
import com.inventory.inventory.Service.EmployeesService;

@RestController
@RequestMapping("${app.BASE_URL}/employees")
public class EmployeesController {

	@Autowired
	private EmployeesService service;

	/*---get all employees for one user---*/
	@GetMapping("/{id}")
	public List<Employee> getEmployeesByUser(@PathVariable("id") long id) {

		System.out.println("in employee for user id = "+id);
		List<Employee> list = service.getEmployeesForUser(id);
		System.out.println("list size = "+list.size());
		return list;
	}
	
	/*---get employee---*/	
    @GetMapping("/employee/{id}")
    public Employee getById(@PathVariable("id") long id) {
    	
        Employee employee=service.getEmployeeById(id);     
        return employee;
    }   
	
	@DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        return service.delete(id);

       
    }

	/*
	 * @PostMapping("/add/{id}") public ResponseEntity<String> add(@RequestBody
	 * Employee employee,@PathVariable("id") long id) { employee.setUser(new
	 * User(id)); return service.save(employee); }
	 */

	/*
	 * @PutMapping("/{id}") public ResponseEntity<String> update(@RequestBody
	 * Employee employee,@PathVariable("id") long id) { employee.setUser(new
	 * User(id)); 
	 * 
	 * return service.save(employee); }
	 */

}
