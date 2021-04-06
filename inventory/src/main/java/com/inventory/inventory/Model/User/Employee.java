package com.inventory.inventory.Model.User;

import javax.persistence.Basic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.ERole;

@Entity
//@DiscriminatorValue("employee")
//@Table( name = "employee")

public class Employee extends User{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ManyToOne()//optional = true)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private MOL mol; // for employee  //
	
	
	
	public Employee(String emp1stName, String emp2ndName, String userName, String encode, String email, ERole empRole) {
		super(emp1stName, emp2ndName, userName, encode, email, empRole);
	}
	
	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Employee(Long id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	public Employee(@NotBlank @Size(max = 150) String userName, @NotBlank @Size(max = 150) String password,
			@NotBlank @Size(max = 150) @Email String email, ERole role) {
		super(userName, password, email, role);
		// TODO Auto-generated constructor stub
	}

	public MOL getMol() {
		return mol;
	}

	public void setMol(MOL mol) {
		this.mol = mol;
	}
	
	public void setMol(Long molId) {
		//this.mol = new MOL(molId);
		this.mol = new MOL(molId);
	}

	
	
	

	
	
	
	
	/*@ManyToOne(optional = true)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private User mol; // for employee  //
	
	public Employee() {}
	
	public Employee(String empName, String encode, String string, Role empRole) {
		super( empName,  encode,  string,  empRole);
	}

	public User getUser_mol() {
		return mol;
	}

	public void setUser_mol(MOL mol) {
		this.mol = mol;
	}

	public void setUser_mol(Long id) {
		this.mol = new MOL(id);
		
	}*/


}
