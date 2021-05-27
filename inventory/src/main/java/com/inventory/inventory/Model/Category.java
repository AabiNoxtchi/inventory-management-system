package com.inventory.inventory.Model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "category",
		uniqueConstraints = { 
		@UniqueConstraint(columnNames = "name")
		})
public class Category extends BaseEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Size(max = 150)
	private String name;
	
	private ProductType productType;
	
	@OneToMany(mappedBy = "category")
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<UserCategory> userCategories;
	
	public Category() {}
	
	public Category(String name, ProductType productType) {
		super();
		this.name = name;
		this.productType = productType;
	}
	
	public Category(Long id) {
		setId(id);
	}

	public String getName() {
		return name;
	}	

	public void setName(String name) {
		this.name = name;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public List<UserCategory> getUserCategories() {
		return userCategories;
	}

	public void setUserCategories(List<UserCategory> userCategories) {
		this.userCategories = userCategories;
	}

}


