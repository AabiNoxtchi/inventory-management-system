package com.inventory.inventory.Service;

import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inventory.inventory.Exception.DuplicateNumbersException;
import com.inventory.inventory.Exception.NoParentFoundException;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.Repository.PendingUserRepositoryImpl;
import com.inventory.inventory.Repository.Interfaces.BaseRepository;
import com.inventory.inventory.Repository.Interfaces.PendingUsersRepository;
import com.inventory.inventory.Repository.Interfaces.UsersRepository;
import com.inventory.inventory.ViewModels.PendingUser.EditVM;
import com.inventory.inventory.ViewModels.PendingUser.FilterVM;
import com.inventory.inventory.ViewModels.PendingUser.IndexVM;
import com.inventory.inventory.ViewModels.PendingUser.OrderBy;
import com.inventory.inventory.auth.Models.QRegisterRequest;
import com.inventory.inventory.auth.Models.RegisterRequest;
import com.inventory.inventory.auth.Models.RegisterResponse;
import com.inventory.inventory.auth.Service.UserDetailsServiceImpl;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;

@Service
public class PendingUsersService extends BaseService<RegisterRequest, FilterVM, OrderBy, IndexVM, EditVM>{

	@Autowired
	PendingUsersRepository repo;
	
	@Autowired
	PendingUserRepositoryImpl repoImpl;
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	UsersRepository usersRepo;
	
	@Autowired
	EmailService emailService;

	private String title = "successfully registered";

	private String text = "Your registration is complete, you can begin to track your inventory. ";
	
	@Override
	protected BaseRepository<RegisterRequest> repo() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	protected RegisterRequest newItem() {
		// TODO Auto-generated method stub
		return new RegisterRequest();
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
	protected Long setDAOItems(IndexVM model, Predicate predicate, Long offset, Long limit,
			OrderSpecifier<?> orderSpecifier) {
		// TODO Auto-generated method stub
		
		model.setDAOItems(repoImpl.getDAOs(predicate, offset, limit));
		
		return repoImpl.DAOCount(predicate);
	}

	@Override
	protected void populateModel(IndexVM model) {
		// TODO Auto-generated method stub
		//model.getFilter().getCities().remo.removeIf(x -> x.getValue().equals(""));
		
	}

	@Override
	protected void populateEditGetModel(EditVM model) {
		
		
	}

	@Override
	protected void populateEditPostModel(@Valid EditVM model)
			throws DuplicateNumbersException, NoParentFoundException, Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean checkGetAuthorization() {
		// TODO Auto-generated method stub
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
	
	protected void dealWithEnumDropDowns(IndexVM model) {
		model.getFilter().getCities().remove(0);
	}
	
	public ResponseEntity<?> save(EditVM model) throws Exception{

		//furtherAuthorize(model.getId());
		System.out.println("in save pending request ******************************** = ");
		Long id = model.getId();
        RegisterRequest item = repo.findOne(QRegisterRequest.registerRequest.id.eq(id)).get();
        System.out.println("register request = "+item.toString());
        
        item.setCityId(model.getCityId());
        User user = item.getMol(ERole.ROLE_Mol, LocalDate.now());
        usersRepo.save(user);
        
        //if(registerRequest.getCityId() == null) throw new Exception("city is required !!!");
		//user = registerRequest.getMol(role);
        repo.delete(item);
       // return userDetailsService.signup(item);
        
        emailService.sendEmail(user.getEmail(), title, text);
        return ResponseEntity.ok(new RegisterResponse("User registered successfully!"));
	}

}
