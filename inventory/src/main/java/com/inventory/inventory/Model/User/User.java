package com.inventory.inventory.Model.User;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.BaseEntity;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.ProductDetail;
import com.inventory.inventory.Model.Role;
import com.inventory.inventory.Model.Supplier;
import com.inventory.inventory.Model.UserProfile;

@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "User_Type")
@Table( name = "user",
		uniqueConstraints = { 
				@UniqueConstraint(columnNames = "userName"),
				@UniqueConstraint(columnNames = "email") 
			})
public class User extends BaseEntity implements Serializable{
	
	/**
	 * 
	 */
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

	@ManyToOne(optional = false)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private Role role;
	
	/*@OneToMany(mappedBy = "user")
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ProductDetail> productDetails;*/
	
	/*@OneToMany()//cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    @Basic(fetch = FetchType.LAZY)
	@JsonIgnore
    private List<Employee> employees;// for Mol*/
	
	@OneToMany(mappedBy = "user")//cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    @Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Product> products;
	
	@OneToMany(mappedBy = "user")//cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Supplier> suppliers;
	
	@ManyToOne(optional = true)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private User mol; // for employee  //
	
	@OneToMany(mappedBy = "user" , cascade = CascadeType.ALL )//, orphanRemoval = true)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
    private List<UserProfile> userProfiles;
	
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
	
	/*public User(InUser other) {
		this(other.getId(),other.getFirstName(), other.getLastName(),
				other.getUserName(), other.getPassword(),
				other.getEmail(),other.getRole());
	}*/
	
	public User( @Size(max = 50) String firstName, @Size(max = 50) String lastName,
			@NotBlank @Size(max = 150) String userName, @NotBlank @Size(max = 150) String password,
			@NotBlank @Size(max = 150) @Email String email, Role role) {
		super();		
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
	
	/*public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}*/

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
	}

	

	/*public List<ProductDetail> getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(List<ProductDetail> productDetails) {
		this.productDetails = productDetails;
	}*/

	public User getMol() {
		return mol;
	}

	public void setMol(User mol) {
		this.mol = mol;
	}
	
	public void setMol(Long molId) {
		//this.mol = new MOL(molId);
		this.mol = new User(molId);
	}

	public List<UserProfile> getUserProfiles() {
		return userProfiles;
	}

	public void setUserProfiles(List<UserProfile> userProfiles) {
		this.userProfiles = userProfiles;
	}
	
	
	

}
