package com.inventory.inventory.auth.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
import com.inventory.inventory.Model.City;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.User.Employee;
import com.inventory.inventory.Model.User.InUser;
import com.inventory.inventory.Model.User.MOL;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.Repository.Interfaces.EmployeeRepository;
import com.inventory.inventory.Repository.Interfaces.MOLRepository;
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
//		List<String> roles = userDetails.getAuthorities().stream()
//			.map(item -> item.getAuthority())
//			.collect(Collectors.toList());
//		
		return ResponseEntity.ok(
				    new LoginResponse(jwt, userDetails.getId(), 
									   userDetails.getUsername(),
									   userDetails.getErole().name()));
									   //roles.get(0)));
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public ResponseEntity<?> signup(@Valid RegisterRequest registerRequest) throws Exception {	
		//System.out.println("userDetailsService got signup request");
		
		
		
		boolean isForUpdate = (registerRequest.getId() != null && registerRequest.getId() > 0);
		User user = isForUpdate ? getUser(registerRequest.getId()) : null;
		
		boolean changedPassword = (isForUpdate && Validation.changedPassword(registerRequest));
		boolean changedUserName = (isForUpdate && Validation.changedUserName(registerRequest,user));

		ResponseEntity<?> response = 
				Validation.validateSignupInput(
						registerRequest, isForUpdate, changedPassword, changedUserName, user ,encoder, userRepository);
		//System.out.println("response == null " +(response == null));
		if(response != null) return response ; 	
		
		if(loggedUserId() == null && registerRequest.getCityId() == null && registerRequest.getNewCity() != null) {
			return handleNewCityRequest(registerRequest);		
		}
		
		//String currentUserRole = getRole();
		
		boolean isSameUser = (registerRequest.getId() != null && registerRequest.getId() == loggedUserId());
		
		
		saveUser(registerRequest, isSameUser, isForUpdate, user);		
		
		if(!isForUpdate)  //newly registerd  or else updated
			return ResponseEntity.ok(new RegisterResponse("User registered successfully!"));
		if((changedUserName || changedPassword) &&	registerRequest.getId() > 0 &&	isSameUser) //updated + create new jwt token
			return ResponseEntity.ok(new RegisterResponse("User updated successfully!", true, createToken(registerRequest), user.getUserName()));		
		 else       //updated
			return ResponseEntity.ok(new RegisterResponse("User updated successfully!"));//without token			
		
		//return null;
	}
	
	private ResponseEntity<?> handleNewCityRequest(@Valid RegisterRequest registerRequest) throws Exception {
		if(registerRequest.getCountryId() == null) throw new Exception("country is required !!!");
		
		registerRequest = pendingUsersRepo.save(registerRequest);
		sender.notifyAdmin(EventType.cityRequest, registerRequest.getId());
		
		return ResponseEntity.ok("Thank you for registering, we'll send you an email as soon as this request is processed .");
		
		
		//return null;
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
			@Valid RegisterRequest registerRequest, //String currentUserRole,
			boolean isSameUser, boolean isForUpdate, User user) throws Exception {
		
		//System.out.println("save user currentUserRole = "+currentUserRole);
		//System.out.println("save user currentUserRole.equals(Role_Admin) = "+currentUserRole.equals("Role_Admin"));
		
		ERole currentUserRole = getRole();
		ERole role = null;
		//User user = null;
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
			
			//user =  makeUser(registerRequest, role, user, isSameUser);			
			break;
			
		case ROLE_Mol :
			if(isForUpdate && isSameUser) {
				role = ERole.ROLE_Mol;
			}
			else
			role = ERole.ROLE_Employee;
			
			//user.setMol(loggedUserId());	
				
			break;
		case ROLE_Employee :
		
				role = ERole.ROLE_Employee;
				
				
			break;
		default:
			break;
		}
		
		//System.out.println("saving user = "+user.toString());
		user = makeUser(registerRequest, role, user, isSameUser);  
		userRepository.save(user);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	private User makeUser(@Valid RegisterRequest registerRequest, ERole role, User user, boolean isSameUser) throws Exception {
		System.out.println("making user role = "+role.name());
		
		user = user == null ? new User() : user;

		if(role.equals(ERole.ROLE_Admin)) {
			registerRequest.populateEntity(user, role);	
		}
		//Long idToUpdate = registerRequest.getId();		
		//idToUpdate = idToUpdate != null && idToUpdate > 0 ? idToUpdate : -1);
		//user.setId(idToUpdate != null && idToUpdate > 0 ? idToUpdate : -1);
		//registerRequest.set
		
		if(role.equals(ERole.ROLE_Mol)) {
			
			//System.out.println("lastactive = "+((MOL) user).getLastActive());
			if(registerRequest.getCityId() == null) throw new Exception("city is required !!!");
			
			LocalDate lastActive =  ((MOL)user).getLastActive();
			if(lastActive == null) lastActive = LocalDate.now();
			user = registerRequest.getMol(role, lastActive);
			// ((MOL) user).setCity(new City(registerRequest.getCityId()));
			//else user.setMolUser( new MOL(new City(registerRequest.getCityId())));
		}else if(role.equals(ERole.ROLE_Employee)) {
			//System.out.println("((Employee)user).getMol()" + ((Employee)user).getMol().toString());
			//System.out.println("((Employee)user).getMol().getId()" + ((Employee)user).getMol().getId());
			user = registerRequest.getEmployee(role, isSameUser ? ((Employee)user).getMol().getId() : loggedUserId());
			
			//System.out.println("employee.tostring() = "+((Employee)user).toString());
				//((Employee) user).setMol(loggedUserId());	
				
				
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

//	public void setLastActive(UserDetailsImpl userDetails) {
//		
//		User mol =  userRepository.findById(userDetails.getId()).get();
//		System.out.println("mol = "+mol.toString());
//		
//		
//		//mol.setLastActive(LocalDate.now());
//		//System.out.println("mol = "+mol.toString());
//		
//		((MOL)mol).setLastActive(LocalDate.now());
//		
//		userRepository.save(mol);
//	}	
// 	
// 	
	
}
