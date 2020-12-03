package com.inventory.inventory.Service;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.User;
import com.inventory.inventory.Repository.BaseRepository;
import com.inventory.inventory.Repository.ProductsRepository;
import com.inventory.inventory.Repository.UsersRepository;
import com.inventory.inventory.ViewModels.User.EditVM;
import com.inventory.inventory.ViewModels.User.FilterVM;
import com.inventory.inventory.ViewModels.User.IndexVM;
import com.inventory.inventory.ViewModels.User.OrderBy;
import com.inventory.inventory.auth.Service.UserDetailsServiceImpl;


@Service
public class UsersService extends BaseService<User, FilterVM, OrderBy, IndexVM, EditVM>{
	
	@Autowired
	private UsersRepository repo;
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	
	
	@Autowired
	ProductsRepository productsRepository;

	@Override
	protected BaseRepository<User> repo() {
		return repo;
	}

	@Override
	protected User newItem() {
		return new User();
	}

	@Override
	protected FilterVM filter() {
		return new FilterVM();
	}

	@Override
	protected EditVM editVM() {
		return new EditVM();
	}

	@Override
	protected OrderBy orderBy() {
		return new OrderBy();
	}
	
	@Override
	public Boolean checkGetAuthorization() {
		
		return checkRole().equals(ERole.ROLE_Admin) || checkRole().equals(ERole.ROLE_Mol);
	}

	@Override
	public Boolean checkSaveAuthorization() {
		
		return checkRole().equals(ERole.ROLE_Admin) || checkRole().equals(ERole.ROLE_Mol);
	}

	@Override
	public Boolean checkDeleteAuthorization() {
		
		return checkRole().equals(ERole.ROLE_Admin) || checkRole().equals(ERole.ROLE_Mol);
	}
	
	protected void populateModel(IndexVM model) {
		
		model.getFilter().setWhosAskingRole(checkRole());
		model.getFilter().setWhosAskingId(getLoggedUser().getId());
	}

	@Override
	protected void PopulateEditPostModel(@Valid EditVM model) {
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
		
//		Optional<Employee> existingEmployee=repo.findById(id);
//		if(!existingEmployee.isPresent())
//			return ResponseEntity
//					.badRequest()
//					.body("No record with that ID");
//		
//		List<Product> products=productsRepository.findByEmployeeId(id);
//		if(products.size()>0) {
//			for(Product product:products)
//			{
//				product.setEmployee((Employee)null);
//			}
//		}
//		
//		repo.deleteById(id);
		
	}
	
}
