package com.inventory.inventory.Controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.inventory.inventory.Model.User;
import com.inventory.inventory.Service.BaseService;
import com.inventory.inventory.Service.UsersService;
import com.inventory.inventory.ViewModels.User.EditVM;
import com.inventory.inventory.ViewModels.User.FilterVM;
import com.inventory.inventory.ViewModels.User.IndexVM;
import com.inventory.inventory.ViewModels.User.OrderBy;
import com.inventory.inventory.auth.Models.RegisterRequest;
import com.inventory.inventory.auth.Service.UserDetailsServiceImpl;

@RestController
@RequestMapping("${app.BASE_URL}/users")
//@PreAuthorize("hasRole('Admin')")
public class UsersController extends BaseController<User, FilterVM, OrderBy, IndexVM, EditVM>{
	
	
	
	@Autowired
	private UsersService service;
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	
	@Override
	protected BaseService<User, FilterVM, OrderBy, IndexVM, EditVM> service() {
		// TODO Auto-generated method stub
		return service;
	}
	
	
	
	/*
	 * //@Override
	 * 
	 * @PutMapping("/save")
	 * 
	 * @PreAuthorize("this.checkSaveAuthorization()") public ResponseEntity<?>
	 * save(@Valid @RequestBody RegisterRequest registerRequest){ // return
	 * service().save(model); return userDetailsService.signup(registerRequest); }
	 */
	
	@PostMapping("/signup")
	//@PreAuthorize("#user.userName == authentication.name")
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
		
		return userDetailsService.signup(registerRequest);
	}

	/*
	 * @Override protected Boolean checkGetAuthorization() { // TODO Auto-generated
	 * method stub return true; }
	 * 
	 * @Override protected Boolean checkSaveAuthorization() { // TODO Auto-generated
	 * method stub return true; }
	 * 
	 * @Override protected Boolean checkDeleteAuthorization() { // TODO
	 * Auto-generated method stub return true; }
	 */
	
//	@GetMapping
//	public List<User> getAll(){
//	
//		return service.listAll();
//	}
//	
//	@GetMapping("/{id}")
//	public User get(@PathVariable Long id) {
//		return service.get(id);				
//	}	
//	
//	
//	@PostMapping("/signup")
//	//@PreAuthorize("#user.userName == authentication.name")
//	public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
//		
//		return userDetailsService.signup(registerRequest);
//	}
//	
//	
//	@DeleteMapping("/{id}")
//    public ResponseEntity<?> delete(@PathVariable Long id) {
//
//        return service.delete(id);
//       
//    }

	

}
