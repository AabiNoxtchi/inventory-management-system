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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.inventory.Model.City;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.User.InUser;
import com.inventory.inventory.Model.User.MOL;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.Repository.Interfaces.EmployeeRepository;
import com.inventory.inventory.Repository.Interfaces.MOLRepository;
import com.inventory.inventory.Repository.Interfaces.UsersRepository;
import com.inventory.inventory.auth.Models.LoginRequest;
import com.inventory.inventory.auth.Models.LoginResponse;
import com.inventory.inventory.auth.Models.RegisterRequest;
import com.inventory.inventory.auth.Models.RegisterResponse;
import com.inventory.inventory.auth.Models.UserDetailsImpl;
import com.inventory.inventory.auth.Utills.JwtUtils;
import com.inventory.inventory.auth.Utills.Validation;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	UsersRepository userRepository;
	
	//@Autowired
	//MOLRepository molRepo;
	
	//@Autowired
	//EmployeeRepository empRepo;
	
//	@Autowired
//	RolesRepository roleRepository;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	PasswordEncoder encoder;

	@Transactional
	public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUserName(username);	
		if(!user.isPresent())throw new UsernameNotFoundException("User with username: " + username+" Not Found !!!" );
	    return UserDetailsImpl.build(user.get());
	    
	   
		
	}
	
	public ResponseEntity<LoginResponse> signin(LoginRequest loginRequest) {
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
//		List<String> roles = userDetails.getAuthorities().stream()
//			.map(item -> item.getAuthority())
//			.collect(Collectors.toList());
//		
		return ResponseEntity.ok(
				    new LoginResponse(jwt, //userDetails.getId(), 
									   userDetails.getUsername(),
									   userDetails.getErole().name()));
									   //roles.get(0)));
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public ResponseEntity<?> signup(@Valid RegisterRequest registerRequest) throws Exception {	
		System.out.println("userDetailsService got signup request");
		
		boolean isForUpdate = (registerRequest.getId() != null && registerRequest.getId() > 0);
		User user = isForUpdate ? getUser(registerRequest.getId()) : null;
		
		boolean changedPassword = (isForUpdate && Validation.changedPassword(registerRequest));
		boolean changedUserName = (isForUpdate && Validation.changedUserName(registerRequest,user));

		ResponseEntity<?> response = 
				Validation.validateSignupInput(
						registerRequest, isForUpdate, changedPassword, changedUserName, user ,encoder, userRepository);
		if(response != null) return response ; 		
		
		String currentUserRole = getRole();
		
		boolean isSameUser = (registerRequest.getId() == loggedUserId());	
		saveUser(registerRequest, currentUserRole, isSameUser, isForUpdate, user);		
		
		if(!isForUpdate)  //newly registerd  or else updated
			return ResponseEntity.ok(new RegisterResponse("User registered successfully!"));
		if((changedUserName || changedPassword) &&	registerRequest.getId() > 0 &&	isSameUser) //updated + create new jwt token
			return ResponseEntity.ok(new RegisterResponse("User updated successfully!", true, createToken(registerRequest)));		
		 else       //updated
			return ResponseEntity.ok(new RegisterResponse("User updated successfully!"));//without token			
		
		//return null;
	}
	
	private Authentication getAuthentication() {
		return
				SecurityContextHolder.getContext().getAuthentication();
	}
	
	private Long loggedUserId() {
		return
		((UserDetailsImpl)getAuthentication().getPrincipal()).getId();		
	}
	
	private String getRole() {
		return
				((UserDetailsImpl)getAuthentication().getPrincipal()).getErole().name();	
//		getAuthentication().getAuthorities().stream()
//		.map(item -> item.getAuthority())
//		.collect(Collectors.toList()).get(0);
	}
	
	public String createToken(@Valid RegisterRequest registerRequest) {
		
		Authentication auth = getAuthentication();		 
		return jwtUtils.createToken(registerRequest, auth) ;
			
	}

	@Transactional(propagation = Propagation.MANDATORY)
	private void saveUser(
			@Valid RegisterRequest registerRequest, String currentUserRole,
			boolean isSameUser, boolean isForUpdate, User user) throws Exception {
		
		System.out.println("save user currentUserRole = "+currentUserRole);
		System.out.println("save user currentUserRole.equals(Role_Admin) = "+currentUserRole.equals("Role_Admin"));
		ERole role;
		//User user = null;
		switch (currentUserRole) {
		case "ROLE_Admin" :
			if(isForUpdate && isSameUser) {
				role = ERole.ROLE_Admin;
			}
			else {
				
				role = ERole.ROLE_Mol;
			}	
			
			user =  makeUser(registerRequest, role, user);			
			break;
			
		case "ROLE_Mol" :
			role = ERole.ROLE_Employee;
			user = makeUser(registerRequest, role, user);  
			user.setMol(loggedUserId());	
				
			break;
		default:
			break;
		}
		
		//System.out.println("saving user = "+user.toString());
		userRepository.save(user);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	private User makeUser(@Valid RegisterRequest registerRequest, ERole role, User user) throws Exception {
		System.out.println("making user role = "+role.name());
		//System.out.println("making user = ");
		user = user==null? new User():user;
//		user = new User(registerRequest.getFirstName(),
//				registerRequest.getLastName(), registerRequest.getUsername(), 					 
//				registerRequest.getPassword(), registerRequest.getEmail(),role);
		registerRequest.populateEntity(user, role);		
		Long idToUpdate=registerRequest.getId();		
		//if(idToUpdate != null && idToUpdate > 0)
			user.setId(idToUpdate != null && idToUpdate > 0 ? idToUpdate : -1);	
		
		if(role.equals(ERole.ROLE_Mol)) {
			if(registerRequest.getCityId() == null) throw new Exception("city is required !!!");
			if(user.getId() > 0) {user.getMolUser().setCity(new City(registerRequest.getCityId()));}
			else user.setMolUser( new MOL(new City(registerRequest.getCityId())));
			}
		//System.out.println("user ==null = "+user==null);
		return user;
		//return null;
	}

	/*private Role findRole(ERole eRole) {
		
		return  roleRepository.findByName(eRole)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));	
	}*/
	
 	private User getUser(Long id) {
	
 		User user = null;
		Optional<User> optuser = userRepository.findById(id);
		if(optuser.isPresent())
			user = optuser.get();
		
		return user;
 		
 		//return null;
			
     }	
	
}
