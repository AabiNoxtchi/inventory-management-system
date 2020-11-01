package com.inventory.inventory;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.Role;
import com.inventory.inventory.Model.User;
import com.inventory.inventory.Repository.RolesRepository;
import com.inventory.inventory.Repository.UsersRepository;

@Component
public class UserDataSeeder implements CommandLineRunner {

	@Autowired
	UsersRepository usersRepository;
	
	@Autowired
	RolesRepository roleRepository;
	
	@Autowired
	PasswordEncoder encoder ;

	
	@Value("${app.password}")
	private String password;

	@Override
	public void run(String... args) throws Exception {
		loadUserData();
	}

	private void loadUserData() {
		if(roleRepository.count()==0) {
			
			roleRepository.save(new Role(ERole.ROLE_Admin));			
			roleRepository.save(new Role(ERole.ROLE_Employee));
			roleRepository.save(new Role(ERole.ROLE_Mol));
		}
		
		if (usersRepository.count() == 0) {			
			
			 Role adminRole = null;
			 Optional<Role> opt = roleRepository.findByName(ERole.ROLE_Admin);
			if (!opt.isPresent())
				adminRole=roleRepository.save(new Role(ERole.ROLE_Admin));
			else 
				adminRole=roleRepository.findByName(ERole.ROLE_Admin).get();
		
			User user = new User("admin", 					
					encoder.encode(password), "admin123@gmail.com", adminRole);
			
			usersRepository.save(user);
			
		}
		
	}
}
