package com.inventory.inventory.Service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.Employee;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.User;
import com.inventory.inventory.Repository.BaseRepository;
import com.inventory.inventory.Repository.EmployeesRepository;
import com.inventory.inventory.Repository.ProductsRepository;
import com.inventory.inventory.Repository.UsersRepository;
import com.inventory.inventory.ViewModels.User.EditVM;
import com.inventory.inventory.ViewModels.User.FilterVM;
import com.inventory.inventory.ViewModels.User.IndexVM;
import com.inventory.inventory.ViewModels.User.OrderBy;
import com.inventory.inventory.auth.Models.RegisterRequest;
import com.inventory.inventory.auth.Service.UserDetailsServiceImpl;


@Service
public class UsersService extends BaseService<User, FilterVM, OrderBy, IndexVM, EditVM>{
	
	@Autowired
	private UsersRepository repo;
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	EmployeesRepository empRepository;
	
	@Autowired
	ProductsRepository productsRepository;
	
	/*
	 * @Autowired PasswordEncoder encoder;
	 */

	@Override
	protected BaseRepository<User> repo() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	protected User newItem() {
		// TODO Auto-generated method stub
		return new User();
	}

	@Override
	protected FilterVM filter() {
		// TODO Auto-generated method stub
		return new FilterVM();
	}

	@Override
	protected EditVM editVM() {
		// TODO Auto-generated method stub
		return new EditVM();
	}

	@Override
	protected OrderBy orderBy() {
		// TODO Auto-generated method stub
		return new OrderBy();
	}

	
	
	@Override
	public Boolean checkGetAuthorization() {
		ERole role = checkRole();
		//logger.info("role = "+role.name());
		System.out.println( role+"  ****  "+ERole.ROLE_Admin);
		return checkRole().equals(ERole.ROLE_Admin);
	}

	@Override
	public Boolean checkSaveAuthorization() {
		// TODO Auto-generated method stub
		return checkRole().equals(ERole.ROLE_Admin);
	}

	@Override
	public Boolean checkDeleteAuthorization() {
		// TODO Auto-generated method stub
		return checkRole().equals(ERole.ROLE_Admin);
	}
	
	protected void populateModel(IndexVM model) {
		model.getFilter().setWhosAskingRole(checkRole());
	}

	@Override
	protected void PopulateEditPostModel(@Valid EditVM model) {
		// TODO Auto-generated method stub
		
	}
	
	public ResponseEntity<?> save(EditVM model){
		return userDetailsService.signup(model.registerRequest());
	}

	@Override
	protected void handleDeletingChilds(List<User> items) {
		
		/*
		 * Optional<User> existingUser = repo.findById(id);
		 * if(!existingUser.isPresent()) return ResponseEntity .badRequest()
		 * .body("No record with that ID");
		 * 
		 * List<Product> products=productsRepository.findByUserId(id);
		 * if(products.size()>0) productsRepository.deleteAll(products);
		 * 
		 * List<Employee> employees=empRepository.findByUserId(id);
		 * if(employees.size()>0) empRepository.deleteAll(employees);
		 * 
		 * repo.deleteById(id); return ResponseEntity.ok(id);
		 */
	
		
	}

	@Override
	protected void handleDeletingChilds(User e) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * public List<User> listAll(){ return repo.findAll(); }
	 * 
	 * public User get(Long id) { return repo.findById(id).get(); }
	 * 
	 * public ResponseEntity<?> delete(Long id) { Optional<User>
	 * existingUser=repo.findById(id); if(!existingUser.isPresent()) return
	 * ResponseEntity .badRequest() .body("No record with that ID");
	 * 
	 * List<Product> products=productsRepository.findByUserId(id);
	 * if(products.size()>0) productsRepository.deleteAll(products);
	 * 
	 * List<Employee> employees=empRepository.findByUserId(id);
	 * if(employees.size()>0) empRepository.deleteAll(employees);
	 * 
	 * repo.deleteById(id); return ResponseEntity.ok(id); }
	 */
	
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
