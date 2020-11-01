package com.inventory.inventory.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.inventory.Model.AbstractUser;
import com.inventory.inventory.Model.User;

public interface UsersRepository extends JpaRepository<User,Long> {
	
	Optional<AbstractUser> findByUserName(String username);	
	User findByUserNameAndPassword(String userName, String password);
	Boolean existsByUserName(String username);
	Boolean existsByEmail(String email);

}
