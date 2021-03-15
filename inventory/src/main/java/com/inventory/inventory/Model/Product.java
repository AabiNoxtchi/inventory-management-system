package com.inventory.inventory.Model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Formula;

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
	
	@Formula("(select count(dd.id) from "			
			+ "product_detail pd inner join delivery_detail dd on dd.id=pd.delivery_detail_id "
			+ "where dd.product_id = id)")
			
	private Long total; // total count
	
	@ManyToOne(optional = true)
	//@Basic(fetch = FetchType.EAGER)
	private SubCategory subCategory;
	
	@ManyToOne(optional = false)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private User user;
	
	@OneToMany(mappedBy = "product")
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<DeliveryDetail> deliveryDetails;
	
	
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
		this.user = mol;
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

	public User getUser() {
		return user;
	}

	public void setUser(MOL mol) {
		this.user = mol;
	}
	
	public void setUser(Long id) {
		//this.user = new MOL(id);
		this.user = new User(id);
	}

	public List<DeliveryDetail> getDeliveryDetails() {
		return deliveryDetails;
	}

	public void setDeliveryDetails(List<DeliveryDetail> deliveryDetails) {
		this.deliveryDetails = deliveryDetails;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	
}
