package com.inventory.inventory.Repository;

import java.util.Optional;

import com.inventory.inventory.Model.User;

public interface UsersRepository extends BaseRepository<User>{
	
	Optional<User> findByUserName(String username);
	User findByUserNameAndPassword(String userName, String password);
	Boolean existsByUserName(String username);
	Boolean existsByEmail(String email);

}
