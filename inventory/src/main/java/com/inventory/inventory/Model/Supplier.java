package com.inventory.inventory.Model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.User.MOL;

@Entity
@Table(name = "supplier",
uniqueConstraints=
	{@UniqueConstraint(columnNames={"name", "user_id"}, name="name"),
		@UniqueConstraint(columnNames={"ddcnumber", "user_id"},name="ddcNumber"),
		@UniqueConstraint(columnNames={"email", "user_id"},name="email")}
)
public class Supplier extends BaseEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Size(max = 50)
	@NotBlank
	private String name;
	
	@Size(max = 150)
	@Email
	private String email;	
	
	@Size(max = 18)
	private String phoneNumber;
	
	@Size(min = 4, max = 15)
	private String DDCnumber;	
	
	@ManyToOne(optional = false)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private MOL user;
	
	@OneToMany(mappedBy="supplier")
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Delivery> deliveries;
	
	public Supplier() {
		super();
	}
	
	public Supplier(Long supplierId) {
		setId(supplierId);
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
	public List<Delivery> getDeliveries() {
		return deliveries;
	}
	public void setDeliveries(List<Delivery> deliveries) {
		this.deliveries = deliveries;
	}
	public MOL getUser() {
		return user;
	}
	public void setUser(MOL user) {
		this.user = user;
	}	
	public void setUser(Long userId) {
		this.user = new MOL(userId);
	}
	
	
}
