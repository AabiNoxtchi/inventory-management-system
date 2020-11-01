package com.inventory.inventory.auth.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
									   roles.get(0)
												
						             )
				                );
	}

	public ResponseEntity<?> signup(@Valid RegisterRequest registerRequest) {
		
		boolean changedUserNameOrPassword=false;
		if(registerRequest.getId()==null||registerRequest.getId() <= 0) {
			if(registerRequest.getPassword()==null||(registerRequest.getPassword().length()<6))
				return ResponseEntity
						.badRequest()
						.body(new RegisterResponse("Error: Password should at least be 6 charachters long !!!!"));

			if (validateUserName(registerRequest)) {
				return ResponseEntity
						.badRequest()
						.body(new RegisterResponse("Error: Username is already taken!"));
			}
	
			if(ValidateEmail(registerRequest)) {
				return ResponseEntity
						.badRequest()
						.body(new RegisterResponse("Error: Email is already in use!"));
			}
			
			registerRequest.setPassword(encoder.encode(registerRequest.getPassword()));
		}else {
			
				if(changedPassword(registerRequest) || changedUserName(registerRequest)) {
					changedUserNameOrPassword=true;
				}
			}

		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		String currentUserRole=authentication.getAuthorities().stream()
		.map(item -> item.getAuthority())
		.collect(Collectors.toList()).get(0);
		
		switch (currentUserRole) {
		case "ROLE_Admin" :
			createMol(registerRequest);			
			break;
			
		case "ROLE_Mol" :
			createEmployee(registerRequest);			
			break;
		default:
		
		}
		
		String jwt=null;
		if(changedUserNameOrPassword &&	registerRequest.getId()>0 &&  //create new jwt token
				registerRequest.getId()==((UserDetailsImpl) authentication.getPrincipal()).getId()) {
		 authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(registerRequest.getUsername(), registerRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		 jwt= jwtUtils.generateJwtToken(authentication);
		
		}

		if(registerRequest.getId()==null)  //newly registerd or updated
			return ResponseEntity.ok(new RegisterResponse("User registered successfully!"));
		else {      //updated
			if(registerRequest.getId()==((UserDetailsImpl) authentication.getPrincipal()).getId())//with new jwt token
			     return ResponseEntity.ok(new RegisterResponse("User updated successfully!", true, jwt));
			else
				return ResponseEntity.ok(new RegisterResponse("User updated successfully!"));//without token
				
		}
			
	}

	private boolean changedUserName(@Valid RegisterRequest registerRequest) {
		if(registerRequest.getUsername()!=userRepository.getOne(registerRequest.getId()).getUserName())
			return true;
		return false;
		
	}

	private boolean changedPassword(@Valid RegisterRequest registerRequest) {
		if(registerRequest.getPassword()!=null&&registerRequest.getPassword().length()>6)
		{
			 registerRequest.setPassword(encoder.encode(registerRequest.getPassword()));
			 return true;
		}
		else {
			
			registerRequest.setPassword(userRepository.getOne(registerRequest.getId()).getPassword());
			return false;
		}
		
	}

	private void createEmployee(@Valid RegisterRequest registerRequest) {
		Role empRole = roleRepository.findByName(ERole.ROLE_Employee)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	
		Employee emp = new Employee(registerRequest.getId(), registerRequest.getFirstName(),
				registerRequest.getLastName(),registerRequest.getUsername(),					
				registerRequest.getPassword(), registerRequest.getEmail(),empRole);
		
		   //get user Id//
		UserDetailsImpl principal=
				(UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		emp.setUser(new User(principal.getId()));	
		
		Long idToUpdate=registerRequest.getId();
		if(idToUpdate!=null && idToUpdate>0)
			emp.setId(idToUpdate);
		employeesRepository.save(emp);
		
	}

	private void createMol(@Valid RegisterRequest registerRequest) {
		Role molRole = roleRepository.findByName(ERole.ROLE_Mol)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			
				User user = new User(registerRequest.getId(),registerRequest.getFirstName(),
						registerRequest.getLastName(), registerRequest.getUsername(), 					 
						registerRequest.getPassword(), registerRequest.getEmail(),molRole);
				
				Long idToUpdate=registerRequest.getId();
				if(idToUpdate!=null&&idToUpdate>0)
					user.setId(idToUpdate);
			
				userRepository.save(user);
		
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
	
    
}
