package com.example.inventoryui.Models;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
	

	private Long id;
	
    private String userName;
    
    private String password;	
    
	private Role role;

	private List<Product> products;

	public User(String userName,String password,Role role){
		this.userName=userName;
		this.password=password;
		this.role=role;
	}

	public User() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
    public Role getRole() {
			return role;
	}

	public void setRole(Role role) {
			this.role = role;
	}

	public List<Product> getProducts() {
		if (products==null)
			products=new ArrayList<Product>();
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
}
