package com.inventory.inventory.Model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.User.MOL;
import com.inventory.inventory.Model.User.User;

@Entity
@Table(name = "product")
public class Product extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String name;

	private String description;

	private ProductType productType;	
	
	private double amortizationPercent;
	
	@ManyToOne(optional = true)
	@Basic(fetch = FetchType.LAZY)
	private SubCategory subCategory;
	
	@ManyToOne(optional = false)
	@Basic(fetch = FetchType.LAZY)
	private User mol;
	
	
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
	
	

	public Product(String name, ProductType productType, double amortizationPercent, SubCategory subCategory) {
		super();
		this.name = name;
		this.productType = productType;
		if(!productType.equals(ProductType.MA)) {
		this.amortizationPercent = amortizationPercent;
		this.subCategory = subCategory;
		}
	}

	public Product(String name, ProductType productType, double amortizationPercent, SubCategory subCategory, MOL mol) {
		super();
		this.name = name;
		this.productType = productType;
		if(!productType.equals(ProductType.MA)) {
		this.amortizationPercent = amortizationPercent;
		this.subCategory = subCategory;
		}
		this.mol = mol;
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

	public double getAmortizationPercent() {
		return amortizationPercent;
	}

	public void setAmortizationPercent(double amortizationPercent) {
		this.amortizationPercent = amortizationPercent;
	}

	public User getMol() {
		return mol;
	}

	public void setMol(MOL mol) {
		this.mol = mol;
	}
	
	public void setMol(Long id) {
		this.mol = new MOL(id);
	}


	
	
}
