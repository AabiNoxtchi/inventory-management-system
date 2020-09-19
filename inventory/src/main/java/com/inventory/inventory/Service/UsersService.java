package com.inventory.inventory.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventory.inventory.Model.User;
import com.inventory.inventory.Repository.UsersRepository;



@Service
public class UsersService {
	
	@Autowired
	private UsersRepository repo;
	
	public List<User> listAll(){
		return repo.findAll();
	}
	
	public User get(Long id) {
		return repo.findById(id).get();
		
	}
	
	public User login(String userName,String password) {
		
		return repo.findByUserNameAndPassword(userName, password);
	}
	
	public void save(User user) {
		repo.save(user);
	}
	
	public void delete(Long id) {		
		repo.deleteById(id);
	}

}
