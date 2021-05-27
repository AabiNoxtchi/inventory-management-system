package com.inventory.inventory.Controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.Service.BaseService;
import com.inventory.inventory.Service.UsersService;
import com.inventory.inventory.ViewModels.User.EditVM;
import com.inventory.inventory.ViewModels.User.FilterVM;
import com.inventory.inventory.ViewModels.User.IndexVM;
import com.inventory.inventory.ViewModels.User.OrderBy;
import com.inventory.inventory.auth.Models.RegisterRequest;

@RestController
@RequestMapping("${app.BASE_URL}/users")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
public class UsersController extends BaseController<User, FilterVM, OrderBy, IndexVM, EditVM>{
	
	@Autowired
	private UsersService service;
	
	@Override
	protected BaseService<User, FilterVM, OrderBy, IndexVM, EditVM> service() {
		return service;
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
		
		try {
			return service.signup(registerRequest);
		} catch (Exception e) {	
			e.printStackTrace();
			return exceptionResponse(e.getMessage());			
		}
	}

}
