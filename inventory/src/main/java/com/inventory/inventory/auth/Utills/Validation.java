package com.inventory.inventory.auth.Utills;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.inventory.inventory.Model.User.MOL;
import com.inventory.inventory.Model.User.QUser;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.Repository.Interfaces.UsersRepository;
import com.inventory.inventory.auth.Models.RegisterRequest;
import com.inventory.inventory.auth.Models.RegisterResponse;

public class Validation {

	public static ResponseEntity<?> validateSignupInput(RegisterRequest registerRequest,
			 boolean isForUpdate, boolean changedPassword, boolean changedUserName, User user, PasswordEncoder encoder, UsersRepository repo) {
			if( isForUpdate && user == null ) {		   
					return ResponseEntity
						.badRequest()
						.body(new RegisterResponse("Error: This record no longer exists !!!!"));
			}
			
			boolean changedEmail = (isForUpdate && changedEmail(registerRequest, user));
			
			if( ( !isForUpdate && ( registerRequest.getPassword()==null||(registerRequest.getPassword().length()<6) ) ) ||
					(changedPassword && registerRequest.getPassword().length()<6))					
				return ResponseEntity
						.badRequest()
						.body(new RegisterResponse("Error: Password should at least be 6 charachters long !!!!"));
			if ( (!isForUpdate && validateUserName(registerRequest,repo)) ||
					(changedUserName && validateUserName(registerRequest,repo)) ) {
				return ResponseEntity
						.badRequest()
						.body(new RegisterResponse("Error: Username is already taken!"));
			}
			if( (!isForUpdate && ValidateEmail(registerRequest,repo)) ||
					(changedEmail && ValidateEmail(registerRequest,repo)) ) {
				return ResponseEntity
						.badRequest()
						.body(new RegisterResponse("Error: Email is already in use!"));
			}
			
			if(!isForUpdate || changedPassword)		
			     registerRequest.setPassword(encoder.encode(registerRequest.getPassword()));
			else if(isForUpdate && !changedPassword)
				registerRequest.setPassword(user.getPassword());		
			return null;
		}
	
	public static boolean changedEmail(RegisterRequest registerRequest, User user) {
		if(!registerRequest.getEmail().equals(user.getEmail()))
			return true;
		return false;
	}

	public static boolean changedUserName(@Valid RegisterRequest registerRequest , User user) {
		
		if(!registerRequest.getUsername().equals(user.getUserName()))
			return true;
		return false;		
	}

	public static boolean changedPassword(@Valid RegisterRequest registerRequest) {
		if(registerRequest.getPassword()!=null && registerRequest.getPassword().length()>0 )
		{			
			 return true;
		}else {			
			return false;
		}
	}
	
	public static boolean ValidateEmail(@Valid RegisterRequest registerRequest, UsersRepository repo) {
		if ( repo.existsByEmail(registerRequest.getEmail()))
			return true;
		return false;
	}

	public static boolean validateUserName(@Valid RegisterRequest registerRequest, UsersRepository repo) {
		
		return 
				repo.findAll(QUser.user.userName.eq(registerRequest.getUsername())) == null;
				
	}    

}
