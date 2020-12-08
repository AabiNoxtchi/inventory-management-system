package com.inventory.inventory;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.Event;
import com.inventory.inventory.Model.EventType;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.QUser;
import com.inventory.inventory.Model.Role;
import com.inventory.inventory.Model.User;
import com.inventory.inventory.Repository.EventsRepository;
import com.inventory.inventory.Repository.ProductsRepository;
import com.inventory.inventory.Repository.RolesRepository;
import com.inventory.inventory.Repository.UsersRepository;

@Component
public class UserDataSeeder implements CommandLineRunner {
	
	@Autowired
	EventHandler handler;		

	@Autowired
	UsersRepository usersRepository;
	
	@Autowired
	RolesRepository roleRepository;
	
	@Autowired
	EventsRepository eventRepository;
	
	@Autowired
	ProductsRepository productsRepository;
	
	@Autowired
	PasswordEncoder encoder ;
	
	@Value("${app.password}")
	private String password;
	
	List<User> usersMol ;//= new ArrayList<>();
	Map<User, List<User>> usersEmp ;

	@Override
	public void run(String... args) throws Exception {
		loadUserData();	
		System.out.println("seeding ok");
		handler.runHandler();
	}

	private void loadUserData() {
		
		if(roleRepository.count()==0) {
			roleRepository.save(new Role(ERole.ROLE_Admin));			
			roleRepository.save(new Role(ERole.ROLE_Employee));
			roleRepository.save(new Role(ERole.ROLE_Mol));
		}
		
		if(eventRepository.count()==0) {
			eventRepository.save(new Event(EventType.Discarded));			
		}
		
		
		if (usersRepository.count() == 0) {			
			
			Role adminRole = null;
			Optional<Role> opt = roleRepository.findByName(ERole.ROLE_Admin);
			if (!opt.isPresent())
				adminRole = roleRepository.save(new Role(ERole.ROLE_Admin));
			else 
				adminRole=opt.get();
		
			User user = new User("admin", encoder.encode(password), "admin123@gmail.com", adminRole);			
			usersRepository.save(user);
			
			//**************************************************  development       *********************************************************//
			Role MolRole = null;
			Optional<Role> optMol = roleRepository.findByName(ERole.ROLE_Mol);
			if (!optMol.isPresent())
				MolRole = roleRepository.save(new Role(ERole.ROLE_Mol));
			else 
				MolRole=optMol.get();		
		
			if(usersMol == null) usersMol = new ArrayList<>();
			for(int i =0; i<10; i++) {			
				
				String userName = "user"+i;
				User userMol = new User(userName, encoder.encode("user"+i+""+i+""+i), userName+"@gmail.com", MolRole);
				userMol = usersRepository.save(userMol);
				
				usersMol.add(userMol);				
				
			}
			
			User userMol = new User("aabi", encoder.encode("aabi123"),"aabi@gmail.com", MolRole);
			userMol = usersRepository.save(userMol);
			usersMol.add(userMol);
			
		}
		
		if ( ((List<User>)usersRepository.findAll(QUser.user.role.name.eq(ERole.ROLE_Employee))).size() == 0) {
			
			if(usersMol == null) usersMol = (List<User>) usersRepository.findAll(QUser.user.role.name.eq(ERole.ROLE_Mol));
			
			Role empRole = null;
			Optional<Role> opt = roleRepository.findByName(ERole.ROLE_Employee);
			if (!opt.isPresent())
				empRole = roleRepository.save(new Role(ERole.ROLE_Employee));
			else 
				empRole=opt.get();
		
			if(usersEmp == null) usersEmp = new HashMap<>();
			for(int k = 0; k < usersMol.size(); k++) {
				
				for(int i = 0; i<5; i++) {			
					
					String empName = "emp"+k+""+i;
					User userEmp = new User(empName, encoder.encode("emp"+i+""+i+""+i), empName+"@gmail.com", empRole);
					userEmp.setUser_mol(usersMol.get(k));
					
					userEmp = usersRepository.save(userEmp);
					User mol = usersMol.get(k);
					if(usersEmp.containsKey(mol))usersEmp.get(mol).add(userEmp);
					else {
						List<User> emps = new ArrayList<>();
						emps.add(userEmp);
						usersEmp.put(mol, emps);
					}
				}
			}
		}
		
		if(productsRepository.count() == 0) {
			
			if(usersMol == null) usersMol = (List<User>) usersRepository.findAll(QUser.user.role.name.eq(ERole.ROLE_Mol));
			if(usersEmp == null) {
				usersEmp = new HashMap<>();
				for(User mol : usersMol) {
					List<User> emps = (List<User>) usersRepository.findAll(QUser.user.role.name.eq(ERole.ROLE_Employee).and(QUser.user.user_mol.id.eq(mol.getId())));
					usersEmp.put(mol, emps);
				}				
			}
			
			List<Product> products = new ArrayList<>();
			
			for(int i = 0; i < usersMol.size(); i++) {
				
				User emp = usersEmp.get(usersMol.get(i)).get(0);
			
				for(int k = 0; k<20 ; k++) {
					
					int m = k < 12 ? k : 0 ;
					GregorianCalendar calendar = new GregorianCalendar(2018, m, 1, 10, 0, 0);				
					Date date = calendar.getTime();
					
					String productName = "product"+k;
					
					String inventoryNumber = k+"";
					for(int j =0; j < 7; j++)inventoryNumber+=k+"";
					inventoryNumber = k < 10 ? inventoryNumber : inventoryNumber.substring(0, inventoryNumber.length()/2);
					
				Product p = new Product(productName, inventoryNumber, ProductType.MA, k,
						false, true, date, null, null);
				p.setUser(usersMol.get(i));
				
				if( k<5 ) {
					p.setEmployee(emp);
				}
				
				products.add(p);
				
				}				
			}
			
			productsRepository.saveAll(products);
		}
		//****************************************************  development       ************************************************************//
		
	}
}
