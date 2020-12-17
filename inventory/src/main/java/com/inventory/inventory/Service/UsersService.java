package com.inventory.inventory.Service;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.QUser;
import com.inventory.inventory.Model.User;
import com.inventory.inventory.Repository.BaseRepository;
import com.inventory.inventory.Repository.ProductsRepository;
import com.inventory.inventory.Repository.UsersRepository;
import com.inventory.inventory.ViewModels.User.EditVM;
import com.inventory.inventory.ViewModels.User.FilterVM;
import com.inventory.inventory.ViewModels.User.IndexVM;
import com.inventory.inventory.ViewModels.User.OrderBy;
import com.inventory.inventory.auth.Models.RegisterRequest;
import com.inventory.inventory.auth.Service.UserDetailsServiceImpl;
import com.querydsl.core.types.dsl.Expressions;


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
	
	public ResponseEntity<?> signup(RegisterRequest model){
		return userDetailsService.signup(model);
	}
	
	

	@Override
	protected void handleDeletingChilds(List<User> items) {
		
		for(User u : items) {
			handleDeletingChilds(u);
		}
	}

	@Override
	protected void handleDeletingChilds(User e) {	
		
		
		if(e.getRole().getName().equals(ERole.ROLE_Mol)) {
			
			List<Product> products = (List<Product>) productsRepository.findAll(Expressions.asBoolean(true).isTrue());
//					QProduct.product.user.id.eq(e.getId()));
			System.out.println("product2.size = "+products.size());
			
			List<User> emps = (List<User>) repo.findAll(QUser.user.user_mol.id.eq(e.getId()));
			
			productsRepository.deleteAll(products);			
			repo.deleteAll(emps);
		}
		
		
		if(e.getRole().getName().equals(ERole.ROLE_Employee)) {
			
			List<Product> products = (List<Product>) productsRepository.findAll(Expressions.asBoolean(true).isTrue());
//					QProduct.product.employee.id.eq(e.getId()));
			System.out.println("product2.size = "+products.size());
			
			if(products.size() > 0) {
				for(Product product : products)
				{
//					product.setEmployee(null);
				}
			}
			productsRepository.saveAll(products);		
		
		}
	}
	
}
