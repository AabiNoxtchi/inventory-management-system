package com.inventory.inventory.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory.Model.Role;
import com.inventory.inventory.Model.User;
import com.inventory.inventory.Service.UsersService;

@RestController
@RequestMapping("/users")
public class UsersController {
	
	@Autowired
	private UsersService service;
	
	@GetMapping
	public List<User> list(){
	
		return service.listAll();
	}
	
	@GetMapping("/{id}")
	public User get(@PathVariable Long id) {
		return service.get(id);				
	}
	
	@PostMapping("/login")
	public User login(@RequestParam("username") final String username,
            @RequestParam("password") final String password) {
		
		return service.login(username,password);
		
	}
	
	@PostMapping("/add")
	//public void add(@RequestBody User user) {
	public void add(@RequestParam("username") final String username,
			@RequestParam("password") final String password,
			@RequestParam("role") final Role role) {
		User user =new User();
		user.setUserName(username);
		user.setPassword(password);
		user.setRole(role);		
	
		service.save(user);
	}
	
	@PutMapping("{id}")
	public void update(@RequestBody User user,@PathVariable Integer id) {
		service.save(user);
	}

}
