package com.inventory.inventory.Model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {
	
	@javax.persistence.Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)	
	private Long id;
	
    private String userName;
    
    private String password;	
    
	private Role role;   
   
    @OneToMany//(fetch = FetchType.LAZY )
    @Basic(fetch = FetchType.LAZY)
	@JsonIgnore
    private List<Product> products;
    
    @OneToMany//(fetch = FetchType.LAZY )
    @Basic(fetch = FetchType.LAZY)
	@JsonIgnore
    private List<Employee> employees;

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public List<Product> getProducts() {
		if (products==null)
			products=new ArrayList<Product>();
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
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

}
