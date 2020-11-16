package com.inventory.inventory.Controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory.Model.User;
import com.inventory.inventory.Service.UsersService;
import com.inventory.inventory.auth.Models.RegisterRequest;
import com.inventory.inventory.auth.Service.UserDetailsServiceImpl;

@RestController
@RequestMapping("/users")
//@PreAuthorize("hasRole('ROLE_Admin')")
public class UsersController {
	
	
	
	@Autowired
	private UsersService service;
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	
	@GetMapping
	public List<User> getAll(){
	
		return service.listAll();
	}
	
	@GetMapping("/{id}")
	public User get(@PathVariable Long id) {
		return service.get(id);				
	}	
	
	
	@PostMapping("/signup")
	//@PreAuthorize("#user.userName == authentication.name")
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
		
		return userDetailsService.signup(registerRequest);
	}
	
	
	@DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        return service.delete(id);
       
    }

}
