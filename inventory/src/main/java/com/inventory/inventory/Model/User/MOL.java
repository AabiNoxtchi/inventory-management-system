package com.inventory.inventory.Model.User;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.City;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.Supplier;
import com.inventory.inventory.Model.UserCategory;

@Entity
//@DiscriminatorValue("mol")
//@Table( name = "mol")
public class MOL extends User{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)// mappedBy = "user", orphanRemoval = true)
    @Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<UserCategory> userCategory;
	
	@OneToMany(mappedBy = "user")//cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Supplier> suppliers;
	
	private LocalDate lastActive;
	
	public MOL(String userName, String encode, String email, ERole molRole) {
		super(userName, encode, email, molRole);
	}
	
	

	public MOL() {
		super();
		// TODO Auto-generated constructor stub
	}



	public MOL(@Size(max = 50) String firstName, @Size(max = 50) String lastName,
			@NotBlank @Size(max = 150) String userName, @NotBlank @Size(max = 150) String password,
			@NotBlank @Size(max = 150) @Email String email, ERole role) {
		super(firstName, lastName, userName, password, email, role);
		// TODO Auto-generated constructor stub
	}



	public MOL(Long molId) {
		super(molId);
	}

	public List<Supplier> getSuppliers() {
		return suppliers;
	}

	public List<UserCategory> getUserCategory() {
		return userCategory;
	}

	public void setUserCategory(List<UserCategory> userCategory) {
		this.userCategory = userCategory;
	}

	public void setSuppliers(List<Supplier> suppliers) {
		this.suppliers = suppliers;
	}
	
	@ManyToOne()//optional = false)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
    private City city;
	
	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}



	@Override
	public String toString() {
		return "MOL [id = "+getId()+ ", lastactive = "+lastActive+" ]";
	}



	public LocalDate getLastActive() {
		return lastActive;
	}



	public void setLastActive(LocalDate lastActive) {
		this.lastActive = lastActive;
	}
	
	
	
	
	
//	private static final long serialVersionUID = 1L;
//	
//	@Id
//    private Long id; 
//    
// 
//    @OneToOne(fetch = FetchType.LAZY)
//    @MapsId
//    private User user;
//    
//    
//    
//    @ManyToOne(optional = false)
//	@Basic(fetch = FetchType.LAZY)
//	@JsonIgnore
//    private City city;
//
//
//
//	public MOL() {
//		super();
//		// TODO Auto-generated constructor stub
//	}
//
//
//
//	public MOL(City city) {
//		super();
//		this.city = city;
//	}
//
//
//
//	public MOL(User molUser, City city) {
//		super();
//		this.user = molUser;
//		this.city = city;
//	}
//
//
//
//	public Long getId() {
//		return id;
//	}
//
//
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//
//
//	
//	public User getUser() {
//		return user;
//	}
//
//
//
//	public void setUser(User user) {
//		this.user = user;
//	}
//
//
//
//	public City getCity() {
//		return city;
//	}
//
//
//
//	public void setCity(City city) {
//		this.city = city;
//	}
//
//
//
//	@Override
//	public String toString() {
//		return "MOL [id=" + id + "]";
//	}
//    
//	
//    
//    
//	/*@OneToMany()
//    @Basic(fetch = FetchType.LAZY)
//	@JsonIgnore
//    private List<Employee> employees;// for Mol
//	
//	@OneToMany(mappedBy = "mol")
//    @Basic(fetch = FetchType.LAZY)
//	@JsonIgnore
//	private List<Product> products;
//	
//	@OneToMany()
//	@Basic(fetch = FetchType.LAZY)
//	@JsonIgnore
//	private List<Supplier> suppliers;*/
//	
//	/*public MOL() {}
//	
//	public MOL(Long id) {
//		super(id);
//	}
//
//	public MOL(String userName, String encode, String string, Role molRole) {
//		super(userName, encode, string,  molRole);
//	}*/
//	
//	
//
//	/*public List<Employee> getEmployees() {
//		return employees;
//	}
//
//	public void setEmployees(List<Employee> employees) {
//		this.employees = employees;
//	}
//
//	public List<Product> getProducts() {
//		return products;
//	}
//
//	public void setProducts(List<Product> products) {
//		this.products = products;
//	}
//
//	public List<Supplier> getSuppliers() {
//		return suppliers;
//	}
//
//	public void setSuppliers(List<Supplier> suppliers) {
//		this.suppliers = suppliers;
//	}*/
//	
//	
//	
//	
}
