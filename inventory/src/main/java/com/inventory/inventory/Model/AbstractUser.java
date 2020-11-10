package com.inventory.inventory.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public class AbstractUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@javax.persistence.Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;	
	
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

	@OneToMany 
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Product> products;

	public AbstractUser() {	}
	
	public AbstractUser(Long id) {	
		super();
		this.id = id;
	}

	public AbstractUser(@NotBlank @Size(max = 150) String userName, @NotBlank @Size(max = 150) String password,
			@NotBlank @Size(max = 150) @Email String email, Role role) {
		super();
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.role = role;
	}
	
	public AbstractUser(Long id, @Size(max = 50) String firstName, @Size(max = 50) String lastName,
			@NotBlank @Size(max = 150) String userName, @NotBlank @Size(max = 150) String password,
			@NotBlank @Size(max = 150) @Email String email, Role role) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.role = role;
	}

	public AbstractUser(AbstractUser other) {
		this(other.getId(),other.getFirstName(), other.getLastName(),
				other.getUserName(), other.getPassword(),
				other.getEmail(),other.getRole());
	}

	public List<Product> getProducts() {
		if (products == null)
			products = new ArrayList<Product>();
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

}
