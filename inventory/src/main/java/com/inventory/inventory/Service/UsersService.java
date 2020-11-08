package com.inventory.inventory.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.inventory.inventory.Model.Employee;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.User;
import com.inventory.inventory.Repository.EmployeesRepository;
import com.inventory.inventory.Repository.ProductsRepository;
import com.inventory.inventory.Repository.UsersRepository;
import com.inventory.inventory.auth.Models.RegisterResponse;



@Service
public class UsersService {
	
	@Autowired
	private UsersRepository repo;
	
	@Autowired
	EmployeesRepository empRepository;
	
	@Autowired
	ProductsRepository productsRepository;
	
	@Autowired
	PasswordEncoder encoder;
	
	public List<User> listAll(){
		return repo.findAll();
	}
	
	public User get(Long id) {
		return repo.findById(id).get();		
	}	
	
	public ResponseEntity<?> delete(Long id) {	
		Optional<User> existingUser=repo.findById(id);
		if(!existingUser.isPresent())
			return ResponseEntity
					.badRequest()
					.body("No record with that ID");
		
		List<Product> products=productsRepository.findByUserId(id);
		if(products.size()>0) productsRepository.deleteAll(products);		
		
		List<Employee> employees=empRepository.findByUserId(id);
		if(employees.size()>0) empRepository.deleteAll(employees);
		
		repo.deleteById(id);
		return ResponseEntity.ok(id);
	}
	
	/*
	 * public ResponseEntity<?> update(User user ) {
	 * 
	 * Optional<User> existingUser=repo.findById(user.getId());
	 * if(!existingUser.isPresent())throw new
	 * NullPointerException("No record with that ID"); else {
	 * 
	 * if (user.getUserName()!=existingUser.get().getUserName() &&
	 * (repo.existsByUserName(user.getUserName()) ||
	 * empRepository.existsByUserName(user.getUserName()))) { return ResponseEntity
	 * .badRequest() .body(new
	 * RegisterResponse("Error: Username is already taken!")); }
	 * 
	 * if (user.getEmail()!=existingUser.get().getEmail()&&
	 * (repo.existsByEmail(user.getEmail()) ||
	 * empRepository.existsByEmail(user.getEmail()))) { return ResponseEntity
	 * .badRequest() .body(new RegisterResponse("Error: Email is already in use!"));
	 * } }
	 * 
	 * //here or from app !!! user.setPassword( encoder.encode(user.getPassword()));
	 * 
	 * //System.out.println(user); //System.out.println(user.getRole().getName());
	 * 
	 * User updated=repo.save(user); return ResponseEntity.ok(updated);
	 * 
	 * }
	 * 
	 */

}
