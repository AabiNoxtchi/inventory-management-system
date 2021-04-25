package com.inventory.inventory.auth.Service;

import java.time.LocalDate;
import java.util.Optional;

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

import com.inventory.inventory.Events.EventSender;
import com.inventory.inventory.Events.EventType;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.User.Employee;
import com.inventory.inventory.Model.User.MOL;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.Repository.Interfaces.PendingUsersRepository;
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
	
	@Autowired
	PendingUsersRepository pendingUsersRepo;
	
	@Autowired
	EventSender sender;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	PasswordEncoder encoder;

	@Transactional
	public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<User> user = userRepository.findByUserName(username);	
		if(!user.isPresent() || user.get().getDeleted() != null)
			throw new UsernameNotFoundException("User with username: " + username +" Not Found !!!" );
		
		User u = user.get();
		if(u.getErole().equals(ERole.ROLE_Mol)) {
			
			((MOL)u).setLastActive(LocalDate.now());			
			userRepository.save(u);
		} 
		
	    return UserDetailsImpl.build(u);		
	}
	
	public ResponseEntity<LoginResponse> signin(LoginRequest loginRequest) {
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
	
		return ResponseEntity.ok(
				    new LoginResponse(jwt, userDetails.getId(), 
									   userDetails.getUsername(),
									   userDetails.getErole().name()));									   
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public ResponseEntity<?> signup(@Valid RegisterRequest registerRequest) throws Exception {			
		
		boolean isForUpdate = (registerRequest.getId() != null && registerRequest.getId() > 0);
		User user = isForUpdate ? getUser(registerRequest.getId()) : null;
		
		boolean changedPassword = (isForUpdate && Validation.changedPassword(registerRequest));
		boolean changedUserName = (isForUpdate && Validation.changedUserName(registerRequest,user));

		ResponseEntity<?> response = 
				Validation.validateSignupInput(
						registerRequest, isForUpdate, changedPassword, changedUserName, user ,encoder, userRepository);
		if(response != null) return response ; 	
		
		if(loggedUserId() == null && registerRequest.getCityId() == null && registerRequest.getNewCity() != null) {
			return handleNewCityRequest(registerRequest);		
		}
		
		boolean isSameUser = (registerRequest.getId() != null && registerRequest.getId() == loggedUserId());
		saveUser(registerRequest, isSameUser, isForUpdate, user);		
		
		if(!isForUpdate)  //newly registerd  or else updated
			return ResponseEntity.ok(new RegisterResponse("User registered successfully!"));
		if((changedUserName || changedPassword) &&	registerRequest.getId() > 0 &&	isSameUser) //updated + create new jwt token
			return ResponseEntity.ok(new RegisterResponse("User updated successfully!", true, createToken(registerRequest), user.getUserName()));		
		 else       //updated
			return ResponseEntity.ok(new RegisterResponse("User updated successfully!"));//without token			
	}
	
	

	private Authentication getAuthentication() {
		return
				SecurityContextHolder.getContext().getAuthentication();
	}
	
	private Long loggedUserId() {
		if(!(getAuthentication().getPrincipal() instanceof  UserDetailsImpl)) return null;
		return
		((UserDetailsImpl)getAuthentication().getPrincipal()).getId();		
	}
	
	private ERole getRole() {		
		if(!(getAuthentication().getPrincipal() instanceof  UserDetailsImpl)) return null;
		return
				((UserDetailsImpl)getAuthentication().getPrincipal()).getErole();	
	}

	@Transactional(propagation = Propagation.MANDATORY)
	private void saveUser(
			@Valid RegisterRequest registerRequest,
			boolean isSameUser, boolean isForUpdate, User user) throws Exception {		
		
		ERole currentUserRole = getRole();
		ERole role = null;
		if(currentUserRole == null) role = ERole.ROLE_Mol;
		else
		switch (currentUserRole) {
		case ROLE_Admin :
			if(isForUpdate && isSameUser) {
				role = ERole.ROLE_Admin;
			}
			else {
				
				role = ERole.ROLE_Mol;
			}	
			break;
			
		case ROLE_Mol :
			if(isForUpdate && isSameUser) {
				role = ERole.ROLE_Mol;
			}
			else
			role = ERole.ROLE_Employee;
			break;
		case ROLE_Employee :		
				role = ERole.ROLE_Employee;	
			break;
		default:
			break;
		}
		user = makeUser(registerRequest, role, user, isSameUser);  
		userRepository.save(user);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	private User makeUser(@Valid RegisterRequest registerRequest,
			ERole role, User user, boolean isSameUser) throws Exception {		
		user = user == null ? new User() : user;
		if(role.equals(ERole.ROLE_Admin)) {
			registerRequest.populateEntity(user, role);	
		}
		
		if(role.equals(ERole.ROLE_Mol)) {
			if(registerRequest.getCityId() == null) throw new Exception("city is required !!!");			
			LocalDate lastActive =  ((MOL)user).getLastActive();
			if(lastActive == null) lastActive = LocalDate.now();
			user = registerRequest.getMol(role, lastActive);			
		}else if(role.equals(ERole.ROLE_Employee)) {			
			user = registerRequest.getEmployee(role, isSameUser ? 
					((Employee)user).getMol().getId() : loggedUserId());			
		}
		return user;
	}
	
 	private User getUser(Long id) {	
 		User user = null;
		Optional<User> optuser = userRepository.findById(id);
		if(optuser.isPresent())
			user = optuser.get();		
		return user; 		
     }
 	
 	private String createToken(@Valid RegisterRequest registerRequest) {		
		Authentication auth = getAuthentication();		 
		return jwtUtils.createToken(registerRequest, auth) ;			
	}
	
	private ResponseEntity<?> handleNewCityRequest(@Valid RegisterRequest registerRequest) throws Exception {
		
		if(registerRequest.getCountryId() == null) throw new Exception("country is required !!!");		
		registerRequest = pendingUsersRepo.save(registerRequest);
		sender.notifyAdmin(EventType.cityRequest, registerRequest.getId());		
		return ResponseEntity.ok("Thank you for registering, we'll send you an email as soon as this request is processed .");
	}
 	

}



