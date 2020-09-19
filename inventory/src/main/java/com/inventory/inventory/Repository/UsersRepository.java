package com.inventory.inventory.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.inventory.Model.User;

public interface UsersRepository extends JpaRepository<User,Long> {
	User findByUserNameAndPassword(String userName, String password);

}
