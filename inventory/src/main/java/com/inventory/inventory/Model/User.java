package com.inventory.inventory.Model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table( name = "user",
		uniqueConstraints = { 
				@UniqueConstraint(columnNames = "userName"),
				@UniqueConstraint(columnNames = "email") 
			})

public class User extends AbstractUser{	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL)//, orphanRemoval = true)
    @Basic(fetch = FetchType.LAZY)
	@JsonIgnore
    private List<Employee> employees;
	
	//@OneToMany 
	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Product> products;
	
	//@Size(max = 150)
	   // private String inventoryName;  	    

	public User() {
		
	}	
	
	public User(Long id) {
		super(id);
		
	}
	
	public User(AbstractUser abstractUser) {
		super(abstractUser);
	}
	
	public User(@NotBlank @Size(max = 150) String userName, @NotBlank @Size(max = 150) String password,
			@NotBlank @Size(max = 150) @Email String email, Role role) {
		super(userName, password, email, role);
		
	}
	
	public User(Long id, @Size(max = 50) String firstName, @Size(max = 50) String lastName,
			@NotBlank @Size(max = 150) String userName, @NotBlank @Size(max = 150) String password,
			@NotBlank @Size(max = 150) @Email String email, Role role) {
		super(id, firstName, lastName, userName, password, email, role);
		// TODO Auto-generated constructor stub
	}	

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
	
	public List<Product> getProducts() {
		if (products == null)
			products = new ArrayList<Product>();
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	/*public String getInventoryName() {
		return inventoryName;
	}

	public void setInventoryName(String inventoryName) {
		this.inventoryName = inventoryName;
	}*/	
}
