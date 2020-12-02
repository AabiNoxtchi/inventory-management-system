package com.example.inventoryui.Models.User;


public class User extends AbstractUser {

	public User() {}

	public User(Long id,String userName,String role){
		setId(id);
		setUserName(userName);
		setRole(Role.valueOf(role));
	}
}