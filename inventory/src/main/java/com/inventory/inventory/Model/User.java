package com.inventory.inventory.Model;

import java.util.List;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import javax.persistence.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table( name = "user",
		uniqueConstraints = { 
				@UniqueConstraint(columnNames = "userName"),
				@UniqueConstraint(columnNames = "email") 
			})

public class User extends AbstractUser{	
	
	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL)//, orphanRemoval = true)
    @Basic(fetch = FetchType.LAZY)
	@JsonIgnore
    private List<Employee> employees;
	
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
	
	/*public String getInventoryName() {
		return inventoryName;
	}

	public void setInventoryName(String inventoryName) {
		this.inventoryName = inventoryName;
	}*/	
}
