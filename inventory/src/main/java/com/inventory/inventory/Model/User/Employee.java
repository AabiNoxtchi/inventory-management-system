package com.inventory.inventory.Model.User;

import javax.persistence.Basic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.Role;

@Entity
@DiscriminatorValue("employee")
//@Table( name = "employee")
public class Employee extends InUser{
	
	@ManyToOne(optional = true)
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
		
	}


}
