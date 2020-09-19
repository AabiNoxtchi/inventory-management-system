package com.inventory.inventory.Model;

import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Product {
	
	@javax.persistence.Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)	
	private Long id;
	
	private String name;

	@ManyToOne(optional=false)
	//@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private User user;
	
	@ManyToOne//(fetch = FetchType.LAZY)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Employee employee;
	
	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	private String inventoryNumber;
	
	private String description;
	
	private ProductType productType;
	
	private int yearsToDiscard;
	
	private boolean isDiscarded;
	
	private boolean isAvailable;	

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "M/dd/yy")	
	private Date dateCreated=new Date();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getInventoryNumber() {
		return inventoryNumber;
	}

	public void setInventoryNumber(String inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
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

	public int getYearsToDiscard() {
		return yearsToDiscard;
	}

	public void setYearsToDiscard(int yearsToDiscard) {
		this.yearsToDiscard = yearsToDiscard;
	}

	public boolean isDiscarded() {
		return isDiscarded;
	}

	public void setDiscarded(boolean isDiscarded) {
		this.isDiscarded = isDiscarded;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	
	public Date getDateCreated() {
		return dateCreated;
	}

	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Integer getAmortizationPercent() {
		return amortizationPercent;
	}

	public void setAmortizationPercent(Integer amortizationPercent) {
		this.amortizationPercent = amortizationPercent;
	}

	public Integer getYearsToMAConvertion() {
		return yearsToMAConvertion;
	}

	public void setYearsToMAConvertion(Integer yearsToMAConvertion) {
		this.yearsToMAConvertion = yearsToMAConvertion;
	}

	//for DMA type
	@Column(nullable = true)
	private Integer amortizationPercent; 
	
	//for DMA type
	@Column(nullable = true)
	private Integer yearsToMAConvertion; 
	
	
	
	
	
	

}
