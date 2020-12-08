package com.inventory.inventory.Model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "product")
public class Product extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	
	
	@ManyToOne(fetch = FetchType.LAZY , optional = false)
	@JsonIgnore
	private User user;
	
	@ManyToOne//(fetch = FetchType.LAZY)
	@JoinColumn(name = "employeeId", nullable = true)
	private User employee;	
	
	@OneToMany(mappedBy = "product",cascade = CascadeType.ALL) // ************** //
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<EventProduct> eventProduct;

	private String name;

	private String inventoryNumber;

	private String description;

	private ProductType productType;

	private int yearsToDiscard;

	private boolean isDiscarded;

	private boolean isAvailable;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
	private Date dateCreated = new Date();

	// for DMA type
	@Column(nullable = true)
	private Integer amortizationPercent;

	// for DMA type
	@Column(nullable = true)
	private Integer yearsToMAConvertion;
	
	public Product() {}
	
	public Product(Long id) {
		this.setId(id);
	}
	
	
	public Product(String name, String inventoryNumber, ProductType productType, int yearsToDiscard,
			boolean isDiscarded, boolean isAvailable, Date dateCreated, Integer amortizationPercent,
			Integer yearsToMAConvertion) {
		super();
		this.name = name;
		this.inventoryNumber = inventoryNumber;
		this.productType = productType;
		this.yearsToDiscard = yearsToDiscard;
		this.isDiscarded = isDiscarded;
		this.isAvailable = isAvailable;
		this.dateCreated = dateCreated;
		this.amortizationPercent = amortizationPercent;
		this.yearsToMAConvertion = yearsToMAConvertion;
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

	public Long getEmployee_id() {
		if (employee != null)
			return employee.getId();
		return null;
	}
	
	@JsonIgnore
	public User getEmployee() {
		return employee;
	}

	public void setEmployee(User employee) {
		this.employee = employee;
	}

	public List<EventProduct> getEventProduct() {
		return eventProduct;
	}

	public void setEventProduct(List<EventProduct> eventProduct) {
		this.eventProduct = eventProduct;
	}
	
}
