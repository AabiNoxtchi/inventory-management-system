package com.inventory.inventory.Repository.Interfaces;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.inventory.inventory.Model.User.User;

@Repository
public interface UsersRepository extends BaseRepository<User>{
	
	Optional<User> findByUserName(String username);
	User findByUserNameAndPassword(String userName, String password);
	Boolean existsByUserName(String username);
	Boolean existsByEmail(String email);

}
