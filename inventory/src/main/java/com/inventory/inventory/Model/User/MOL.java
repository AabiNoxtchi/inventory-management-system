package com.inventory.inventory.Model.User;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.City;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.Supplier;
import com.inventory.inventory.Model.UserCategory;

@Entity
public class MOL extends User{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LocalDate lastActive;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<UserCategory> userCategory;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Supplier> suppliers;
	
	@ManyToOne()
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
    private City city;
	
	public MOL() {
		super();
	}
	
	public MOL(Long molId) {
		super(molId);
	}
	
	public MOL(String userName, String encode, String email, ERole molRole) {
		super(userName, encode, email, molRole);
	}

	public MOL(@Size(max = 50) String firstName, @Size(max = 50) String lastName,
			@NotBlank @Size(max = 150) String userName, @NotBlank @Size(max = 150) String password,
			@NotBlank @Size(max = 150) @Email String email, ERole role) {
		super(firstName, lastName, userName, password, email, role);
	}

	public List<Supplier> getSuppliers() {
		return suppliers;
	}
	
	public void setSuppliers(List<Supplier> suppliers) {
		this.suppliers = suppliers;
	}

	public List<UserCategory> getUserCategory() {
		return userCategory;
	}

	public void setUserCategory(List<UserCategory> userCategory) {
		this.userCategory = userCategory;
	}
	
	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}
	
	public LocalDate getLastActive() {
		return lastActive;
	}

	public void setLastActive(LocalDate lastActive) {
		this.lastActive = lastActive;
	}


}
