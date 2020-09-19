package com.inventory.inventory.Model;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Employee {
	
	@javax.persistence.Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)	
	private Long id;
	
	private String firstName;
	private String lastName;	
	
	@ManyToOne//(fetch = FetchType.LAZY)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private User user;
	
	@OneToMany//(fetch = FetchType.LAZY )
	@Basic(fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> products;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	

}
