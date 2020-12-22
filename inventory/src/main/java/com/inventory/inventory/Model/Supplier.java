package com.inventory.inventory.Model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.User.MOL;
import com.inventory.inventory.Model.User.User;

@Entity
@Table(name = "supplier")
public class Supplier extends BaseEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String email;
	private String phoneNumber;
	private String DDCnumber;
	
	@ManyToOne(optional = true)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private User mol;
	
	@OneToMany()//cascade = CascadeType.ALL, mappedBy = "supplier", orphanRemoval = true)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Delivery> deliveries;
	
	
	
	public Supplier() {
		super();
	}
	public Supplier(String name, String email, String phoneNumber, String dDCnumber) {
		super();
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		DDCnumber = dDCnumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getDDCnumber() {
		return DDCnumber;
	}
	public void setDDCnumber(String dDCnumber) {
		DDCnumber = dDCnumber;
	}
	public User getMol() {
		return mol;
	}
	public void setMol(MOL mol) {
		this.mol = mol;
	}
	public void setMol(Long id) {
		this.mol = new MOL(id);
	}
	public List<Delivery> getDeliveries() {
		return deliveries;
	}
	public void setDeliveries(List<Delivery> deliveries) {
		this.deliveries = deliveries;
	}
	public void setMol(User mol) {
		this.mol = mol;
	}
	
	
	
}
