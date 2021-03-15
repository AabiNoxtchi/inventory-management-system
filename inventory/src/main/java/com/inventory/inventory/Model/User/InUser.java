package com.inventory.inventory.Model.User;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.ProductDetail;
import com.inventory.inventory.Model.Role;

//@MappedSuperclass

//@Entity
//@DiscriminatorValue("inUser")
//@Table( name = "inUser")
public class InUser extends User{
	
	private static final long serialVersionUID = 1L;

	
	
	/*@OneToMany()
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ProductDetail> productDetails;
	
	public InUser() {
		
	}	
	
	public InUser(Long id) {
		super(id);
		
	}
	
	public InUser(@NotBlank @Size(max = 150) String userName, @NotBlank @Size(max = 150) String password,
			@NotBlank @Size(max = 150) @Email String email, Role role) {
		super(userName, password, email, role);
		
	}
	
	public InUser(Long id, @Size(max = 50) String firstName, @Size(max = 50) String lastName,
			@NotBlank @Size(max = 150) String userName, @NotBlank @Size(max = 150) String password,
			@NotBlank @Size(max = 150) @Email String email, Role role) {
		super(id, firstName, lastName, userName, password, email, role);
		// TODO Auto-generated constructor stub
	}

	public List<ProductDetail> getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(List<ProductDetail> productDetails) {
		this.productDetails = productDetails;
	}	*/
	

	
//	@OneToMany(mappedBy = "baseUser",cascade = CascadeType.ALL) // ************** //
//	@Basic(fetch = FetchType.LAZY)
//	@JsonIgnore
//	private List<EventProduct> eventProduct;
	
	
	
	
}
