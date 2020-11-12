package com.inventory.inventory;

//import java.util.List;
import java.util.Optional;
//import java.util.function.Predicate;

//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.inventory.inventory.Model.ERole;
//import com.inventory.inventory.Model.Product;
//import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.Role;
import com.inventory.inventory.Model.User;
//import com.inventory.inventory.Repository.ProductsRepository;
import com.inventory.inventory.Repository.RolesRepository;
import com.inventory.inventory.Repository.UsersRepository;
//import com.querydsl.core.types.Predicate;
//import com.querydsl.jpa.impl.JPAQuery;

@Component
//@SuppressWarnings({ "rawtypes", "unchecked" })
public class UserDataSeeder implements CommandLineRunner {

	@Autowired
	UsersRepository usersRepository;
	
	@Autowired
	RolesRepository roleRepository;
	
	@Autowired
	PasswordEncoder encoder ;
	
	@Value("${app.password}")
	private String password;
	
	//private static final Logger logger = LoggerFactory.getLogger(UserDataSeeder.class);

	/*
	 * @PersistenceContext private EntityManager entityManager;
	 * 
	 * @Autowired private ProductsRepository productsRepository;
	 */

	@Override
	public void run(String... args) throws Exception {
		loadUserData();
		//checksqld();
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
