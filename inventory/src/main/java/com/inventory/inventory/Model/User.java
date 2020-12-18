package com.inventory.inventory.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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

@Entity
@Table( name = "user",
		uniqueConstraints = { 
				@UniqueConstraint(columnNames = "userName"),
				@UniqueConstraint(columnNames = "email") 
			})
public class User extends BaseEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Size(max = 50)
    private String firstName; 	  
	
	@Size(max = 50)
    private String lastName;
	 
	@NotBlank
	@Size(max = 150)
	private String userName;

	@NotBlank
	@Size(max = 150)
	@JsonIgnore
	private String password;

	@NotBlank
	@Size(max = 150)
	@Email
	private String email;

	@ManyToOne(optional = true)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private Role role;
	
	@OneToMany()
    @Basic(fetch = FetchType.LAZY)
	@JsonIgnore
    private List<User> employees;// for Mol
	
	@ManyToOne(optional = true)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private User user_mol; // for employee  //
	
	@OneToMany()
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ProductDetail> productDetails;
	
	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL) // ************** //
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<EventProduct> eventProduct;

	public User() {	}
	
	public User(Long id) {	
		this.setId(id);
		
	}

	public User(@NotBlank @Size(max = 150) String userName,
			@NotBlank @Size(max = 150) String password,
			@NotBlank @Size(max = 150) @Email String email, 
			Role role) {
		super();
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.role = role;
	}
	
	public User(User other) {
		this(other.getId(),other.getFirstName(), other.getLastName(),
				other.getUserName(), other.getPassword(),
				other.getEmail(),other.getRole());
	}
	
	public User(Long id, @Size(max = 50) String firstName, @Size(max = 50) String lastName,
			@NotBlank @Size(max = 150) String userName, @NotBlank @Size(max = 150) String password,
			@NotBlank @Size(max = 150) @Email String email, Role role) {
		super();
		setId(id);
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.role = role;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
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
	
	public List<User> getEmployees() {
		return employees;
	}

	public void setEmployees(List<User> employees) {
		this.employees = employees;
	}
	
	public User getUser_mol() {
		return user_mol;
	}

	public void setUser_mol(User user_mol) {
		this.user_mol = user_mol;
	}

	public List<EventProduct> getEventProduct() {
		return eventProduct;
	}

	public void setEventProduct(List<EventProduct> eventProduct) {
		this.eventProduct = eventProduct;
	}

	public List<ProductDetail> getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(List<ProductDetail> productDetails) {
		this.productDetails = productDetails;
	}

	

	
}
