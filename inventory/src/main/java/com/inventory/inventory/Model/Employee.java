package com.inventory.inventory.Model;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table( name = "employees",
uniqueConstraints = { 
		@UniqueConstraint(columnNames = "userName"),
		@UniqueConstraint(columnNames = "email") 
	})
public class Employee extends AbstractUser{
	
	@ManyToOne(optional=false)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private User user;
	
	public Employee() {
		
	}
	
	public Employee(Long id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	public Employee(@NotBlank @Size(max = 150) String userName, @NotBlank @Size(max = 150) String password,
			@NotBlank @Size(max = 150) @Email String email, Role role) {
		super(userName, password, email, role);
		
	}
	
	public Employee(Long id, @Size(max = 50) String firstName, @Size(max = 50) String lastName,
			@NotBlank @Size(max = 150) String userName, @NotBlank @Size(max = 150) String password,
			@NotBlank @Size(max = 150) @Email String email, Role role) {
		super(id, firstName, lastName, userName, password, email, role);
		
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	/*
	 * public String getFirstName() { return firstName; } public void
	 * setFirstName(String firstName) { this.firstName = firstName; } public String
	 * getLastName() { return lastName; } public void setLastName(String lastName) {
	 * this.lastName = lastName; }
	 */

}
