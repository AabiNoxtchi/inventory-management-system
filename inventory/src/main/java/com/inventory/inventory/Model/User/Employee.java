package com.inventory.inventory.Model.User;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.ERole;

@Entity
public class Employee extends User{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ManyToOne()
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private MOL mol; 
	
	public Employee() {
		super();
	}

	public Employee(Long id) {
		super(id);
	}
	
	public Employee(String emp1stName, String emp2ndName, String userName, String encode, String email, ERole empRole) {
		super(emp1stName, emp2ndName, userName, encode, email, empRole);
	}

	public Employee(@NotBlank @Size(max = 150) String userName, @NotBlank @Size(max = 150) String password,
			@NotBlank @Size(max = 150) @Email String email, ERole role) {
		super(userName, password, email, role);
	}

	public MOL getMol() {
		return mol;
	}

	public void setMol(MOL mol) {
		this.mol = mol;
	}
	
	public void setMol(Long molId) {
		this.mol = new MOL(molId);
	}

}
