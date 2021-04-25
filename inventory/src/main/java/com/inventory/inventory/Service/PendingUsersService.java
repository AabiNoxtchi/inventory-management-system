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
		return repo;
	}

	@Override
	protected RegisterRequest newItem() {
		return new RegisterRequest();
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
	protected Long setDAOItems(IndexVM model, Predicate predicate, Long offset, Long limit,
			OrderSpecifier<?> orderSpecifier) {
		model.setDAOItems(repoImpl.getDAOs(predicate, offset, limit));		
		return repoImpl.DAOCount(predicate);
	}

	@Override
	protected void populateModel(IndexVM model) {
		}

	@Override
	protected void populateEditGetModel(EditVM model) {
	}

	@Override
	protected void populateEditPostModel(@Valid EditVM model)
			throws DuplicateNumbersException, NoParentFoundException, Exception {
	}
	
	protected void dealWithEnumDropDowns(IndexVM model) {
		model.getFilter().getCities().remove(0);
	}

	@Override
	public Boolean checkGetAuthorization() {
		return checkRole().equals(ERole.ROLE_Admin);
	}

	@Override
	public Boolean checkSaveAuthorization() {
		return checkRole().equals(ERole.ROLE_Admin);
	}

	@Override
	public Boolean checkDeleteAuthorization() {
		return checkRole().equals(ERole.ROLE_Admin);
	}
	
	public ResponseEntity<?> save(EditVM model) throws Exception{

		Long id = model.getId();
        RegisterRequest item = repo.findOne(QRegisterRequest.registerRequest.id.eq(id)).get();
         item.setCityId(model.getCityId());
        User user = item.getMol(ERole.ROLE_Mol, LocalDate.now());
        usersRepo.save(user);
        
        repo.delete(item);
        emailService.sendEmail(user.getEmail(), title, text);
        return ResponseEntity.ok(new RegisterResponse("User registered successfully!"));
	}

}
