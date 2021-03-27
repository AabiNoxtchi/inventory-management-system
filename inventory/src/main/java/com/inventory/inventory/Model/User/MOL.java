package com.inventory.inventory.Model.User;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.City;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.Supplier;

@Entity
//@DiscriminatorValue("mol")
@Table( name = "mol")
public class MOL{
	
	private static final long serialVersionUID = 1L;
	
	@Id
    private Long id; 
    
 
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private User user;
    
    
    
    @ManyToOne(optional = false)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
    private City city;



	public MOL() {
		super();
		// TODO Auto-generated constructor stub
	}



	public MOL(City city) {
		super();
		this.city = city;
	}



	public MOL(User molUser, City city) {
		super();
		this.user = molUser;
		this.city = city;
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	
	public User getUser() {
		return user;
	}



	public void setUser(User user) {
		this.user = user;
	}



	public City getCity() {
		return city;
	}



	public void setCity(City city) {
		this.city = city;
	}



	@Override
	public String toString() {
		return "MOL [id=" + id + "]";
	}
    
	
    
    
	/*@OneToMany()
    @Basic(fetch = FetchType.LAZY)
	@JsonIgnore
    private List<Employee> employees;// for Mol
	
	@OneToMany(mappedBy = "mol")
    @Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Product> products;
	
	@OneToMany()
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Supplier> suppliers;*/
	
	/*public MOL() {}
	
	public MOL(Long id) {
		super(id);
	}

	public MOL(String userName, String encode, String string, Role molRole) {
		super(userName, encode, string,  molRole);
	}*/
	
	

	/*public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public List<Supplier> getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(List<Supplier> suppliers) {
		this.suppliers = suppliers;
	}*/
	
	
	
	
}
