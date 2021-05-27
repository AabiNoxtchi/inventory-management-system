package com.inventory.inventory.Model.User;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.BaseEntity;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.UserProfile;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Type")
@Table( name = "user",
		uniqueConstraints = { 
				@UniqueConstraint(columnNames = "userName", name = "userName"),
				@UniqueConstraint(columnNames = "email", name="email") 
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
	@JsonIgnore
	private String password;

	@NotBlank
	@Size(max = 150)
	@Email
	private String email;
	
	private LocalDate deleted;
	
	@Enumerated()
	@Column(nullable = false)	
	@JsonIgnore
	private ERole erole;
	
	
	@OneToMany(mappedBy = "user" )
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
			ERole role) {
		super();
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.erole = role;
	}	
	
	public User( @Size(max = 50) String firstName, @Size(max = 50) String lastName,
			@NotBlank @Size(max = 150) String userName, @NotBlank @Size(max = 150) String password,
			@NotBlank @Size(max = 150) @Email String email, ERole role) {
		super();		
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.erole = role;
	}	
	
	/***** getters and setter *****/
	
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

	public ERole getErole() {
		return erole;
	}

	public void setErole(ERole erole) {
		this.erole = erole;
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

	public LocalDate getDeleted() {
		return deleted;
	}

	public void setDeleted(LocalDate deleted) {
		this.deleted = deleted;
	}
	
	public List<UserProfile> getUserProfiles() {
		return userProfiles;
	}

	public void setUserProfiles(List<UserProfile> userProfiles) {
		this.userProfiles = userProfiles;
	}

}


