package com.inventory.inventory.Model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "product",
		uniqueConstraints = { 
		@UniqueConstraint(columnNames = "name")
		})
public class Product extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String name;

	private String description;

	private ProductType productType;

	@ManyToOne(optional = false)
	@Basic(fetch = FetchType.LAZY)
	private SubCategory subCategory;
	
	private int amortizationPercent;
	
	
	// ************** //
	public Product() {}
	
	public Product(Long id) {
		this.setId(id);
	}
	
	public Product(String name, ProductType productType) {
		super();
		this.name = name;
		this.productType = productType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}


	public SubCategory getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(SubCategory subCategory) {
		this.subCategory = subCategory;
	}

	public int getAmortizationPercent() {
		return amortizationPercent;
	}

	public void setAmortizationPercent(int amortizationPercent) {
		this.amortizationPercent = amortizationPercent;
	}
	
	
}
