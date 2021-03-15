package com.inventory.inventory.Model;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Nullable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.User.MOL;
import com.inventory.inventory.Model.User.User;
import com.querydsl.core.annotations.QueryInit;

@Entity
@Table(name = "supplier")//, should check for every mol seperatly 
/*uniqueConstraints = { 
		@UniqueConstraint(columnNames = "DDCnumber"),
		@UniqueConstraint(columnNames = "phoneNumber") ,
		@UniqueConstraint(columnNames = "email") 
	})*/
public class Supplier extends BaseEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Size(max = 50)
	@NotBlank
	private String name;
	@Size(max = 150)
	private String email;	
	@Size(max = 12)
	private String phoneNumber;
	@Size(min = 11, max = 11)
	private String DDCnumber;
	//private boolean deleted;
	
	
	@ManyToOne(optional = false)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private User user;
	
	@OneToMany(mappedBy="supplier")//cascade = CascadeType.ALL, mappedBy = "supplier", orphanRemoval = true)
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
	
	public Supplier(Long supplierId) {
		setId(supplierId);
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
	public List<Delivery> getDeliveries() {
		return deliveries;
	}
	public void setDeliveries(List<Delivery> deliveries) {
		this.deliveries = deliveries;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
}
