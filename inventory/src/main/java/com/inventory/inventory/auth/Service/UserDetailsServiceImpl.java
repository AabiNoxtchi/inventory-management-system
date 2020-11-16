package com.inventory.inventory.auth.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.inventory.Model.AbstractUser;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.Employee;
import com.inventory.inventory.Model.Role;
import com.inventory.inventory.Model.User;
import com.inventory.inventory.Repository.EmployeesRepository;
import com.inventory.inventory.Repository.RolesRepository;
import com.inventory.inventory.Repository.UsersRepository;
import com.inventory.inventory.auth.Models.LoginRequest;
import com.inventory.inventory.auth.Models.LoginResponse;
import com.inventory.inventory.auth.Models.RegisterRequest;
import com.inventory.inventory.auth.Models.RegisterResponse;
import com.inventory.inventory.auth.Models.UserDetailsImpl;
import com.inventory.inventory.auth.Utills.JwtUtils;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	UsersRepository userRepository;
	
	@Autowired
	EmployeesRepository employeesRepository;

	@Autowired
	RolesRepository roleRepository;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	PasswordEncoder encoder;

	@Transactional
	public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<AbstractUser> user = userRepository.findByUserName(username);				
		if(!user.isPresent())user=employeesRepository.findByUserName(username);				
		if(!user.isPresent())throw new UsernameNotFoundException("User Not Found with username: " + username);
	    return UserDetailsImpl.build(user.get());
		
	}
	
	public ResponseEntity<LoginResponse> signin(LoginRequest loginRequest) {
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
			.map(item -> item.getAuthority())
			.collect(Collectors.toList());
		
		return ResponseEntity.ok(
				    new LoginResponse(jwt, userDetails.getId(), 
									   userDetails.getUsername(),											
									   roles.get(0)));
	}

	public ResponseEntity<?> signup(@Valid RegisterRequest registerRequest) {	
		
		boolean isForUpdate = (registerRequest.getId() != null && registerRequest.getId() > 0);
		AbstractUser abstractUser = isForUpdate?getAbstractUser(registerRequest.getId()):null;
		boolean changedPassword = (isForUpdate && changedPassword(registerRequest));
		boolean changedUserName = (isForUpdate && changedUserName(registerRequest,abstractUser));

		ResponseEntity<?> response = 
				validateSignupInput(registerRequest,isForUpdate,changedPassword,changedUserName,abstractUser);
		if(response!=null) return response ; 
		
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		String currentUserRole=authentication.getAuthorities().stream()
			.map(item -> item.getAuthority())
			.collect(Collectors.toList()).get(0);
		
		boolean isSameUser = (registerRequest.getId() == ((UserDetailsImpl) authentication.getPrincipal()).getId());		
		saveUser(registerRequest,currentUserRole,isSameUser,isForUpdate);		
		
		if(!isForUpdate)  //newly registerd or updated
			return ResponseEntity.ok(new RegisterResponse("User registered successfully!"));
		if((changedUserName || changedPassword) &&	registerRequest.getId() > 0 &&	isSameUser) //create new jwt token
			return ResponseEntity.ok(new RegisterResponse("User updated successfully!", true,createToken(registerRequest)));		
		 else       //updated
			return ResponseEntity.ok(new RegisterResponse("User updated successfully!"));//without token			
	}
	
	private String createToken(@Valid RegisterRequest registerRequest) {
		  Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		  ((UserDetailsImpl) authentication.getPrincipal()).setUsername(registerRequest.getUsername());
		  String jwt= jwtUtils.generateJwtToken(authentication);
		return jwt;		
	}

	private void saveUser(@Valid RegisterRequest registerRequest, String currentUserRole,
			boolean isSameUser, boolean isForUpdate) {
		Role role;
		switch (currentUserRole) {
		case "ROLE_Admin" :
			if(isForUpdate && isSameUser) {
				role=findRole(ERole.ROLE_Admin);
			}
			else {
				role=findRole(ERole.ROLE_Mol);
			}
			User user = new User(makeUser(registerRequest,role));
			userRepository.save(user);
			break;
			
		case "ROLE_Mol" :
			role=findRole(ERole.ROLE_Employee);
			Employee emp = new Employee( makeUser(registerRequest,role));			  
			UserDetailsImpl principal=   //get user Id//
					(UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			emp.setUser(new User(principal.getId()));	
			employeesRepository.save(emp);	
			break;
		default:
			break;
		}
	}

	private AbstractUser makeUser(@Valid RegisterRequest registerRequest, Role role) {
		
		AbstractUser abstractUser = new AbstractUser(registerRequest.getId(),registerRequest.getFirstName(),
				registerRequest.getLastName(), registerRequest.getUsername(), 					 
				registerRequest.getPassword(), registerRequest.getEmail(),role);
		
		Long idToUpdate=registerRequest.getId();
		if(idToUpdate!=null&&idToUpdate>0)
			abstractUser.setId(idToUpdate);	
		return abstractUser;
	}

	private Role findRole(ERole eRole) {
		
		return  roleRepository.findByName(eRole)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));	
	}

	private boolean changedEmail(RegisterRequest registerRequest, AbstractUser user) {
		if(!registerRequest.getEmail().equals(user.getEmail()))
			return true;
		return false;
	}

	private boolean changedUserName(@Valid RegisterRequest registerRequest , AbstractUser user) {
		
		if(!registerRequest.getUsername().equals(user.getUserName()))
			return true;
		return false;		
	}

	private boolean changedPassword(@Valid RegisterRequest registerRequest) {
		if(registerRequest.getPassword()!=null && registerRequest.getPassword().length()>0 )
		{			
			 return true;
		}else {			
			return false;
		}
	}
	
	private boolean ValidateEmail(@Valid RegisterRequest registerRequest) {
		if (userRepository.existsByEmail(registerRequest.getEmail())
				|| employeesRepository.existsByEmail(registerRequest.getEmail()))
			return true;
		return false;
	}

	private boolean validateUserName(@Valid RegisterRequest registerRequest) {
		if (userRepository.existsByUserName(registerRequest.getUsername()) 
				|| employeesRepository.existsByUserName(registerRequest.getUsername()))
			return true;
		return false;		
	}    
	
	private ResponseEntity<?> validateSignupInput(RegisterRequest registerRequest,
		 boolean isForUpdate, boolean changedPassword, boolean changedUserName, AbstractUser abstractUser) {
		if(isForUpdate && abstractUser == null) {		   
				return ResponseEntity
					.badRequest()
					.body(new RegisterResponse("Error: This record no longer exists !!!!"));
		}
		
		boolean changedEmail = (isForUpdate && changedEmail(registerRequest, abstractUser));
		
		if( ( !isForUpdate && ( registerRequest.getPassword()==null||(registerRequest.getPassword().length()<6) ) ) ||
				(changedPassword && registerRequest.getPassword().length()<6))					
			return ResponseEntity
					.badRequest()
					.body(new RegisterResponse("Error: Password should at least be 6 charachters long !!!!"));
		if ( (!isForUpdate && validateUserName(registerRequest)) ||
				(changedUserName && validateUserName(registerRequest)) ) {
			return ResponseEntity
					.badRequest()
					.body(new RegisterResponse("Error: Username is already taken!"));
		}
		if( (!isForUpdate && ValidateEmail(registerRequest)) ||
				(changedEmail && ValidateEmail(registerRequest)) ) {
			return ResponseEntity
					.badRequest()
					.body(new RegisterResponse("Error: Email is already in use!"));
		}
		
		if(!isForUpdate || changedPassword)		
		     registerRequest.setPassword(encoder.encode(registerRequest.getPassword()));
		else if(isForUpdate && !changedPassword)
			registerRequest.setPassword(abstractUser.getPassword());		
		return null;
	}

 	private AbstractUser getAbstractUser(Long id) {
	
 		AbstractUser abstractUser=null;
		Optional<User> user=userRepository.findById(id);
		if(user.isPresent())
			abstractUser=user.get();
		else
		{
			Optional<Employee> emp=employeesRepository.findById(id);
			if(emp.isPresent())abstractUser=emp.get();					
		}	
		return abstractUser;
			
     }	
	
}
