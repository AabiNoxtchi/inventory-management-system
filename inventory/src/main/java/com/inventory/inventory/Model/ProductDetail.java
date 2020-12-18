package com.inventory.inventory.Model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "productDetail", 
		uniqueConstraints = { 
		@UniqueConstraint(columnNames = "inventoryNumber")
		})
public class ProductDetail extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional = true)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private AvailableProduct availableProduct;
	
	private String inventoryNumber;
	
	private boolean isDiscarded;

	private boolean isAvailable;
	
//	@ManyToOne(optional = true)
//	@Basic(fetch = FetchType.LAZY)
//	@JsonIgnore
//	private User user;
	
	@OneToMany(mappedBy = "productDetail",cascade = CascadeType.ALL) 
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<EventProduct> eventProduct;
	
	/************/
	
	public List<EventProduct> getEventProduct() {
		return eventProduct;
	}

	public void setEventProduct(List<EventProduct> eventProduct) {
		this.eventProduct = eventProduct;
	}

	
	public String getInventoryNumber() {
		return inventoryNumber;
	}

	public void setInventoryNumber(String inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
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

	public AvailableProduct getAvailableProduct() {
		return availableProduct;
	}

	public void setAvailableProduct(AvailableProduct availableProduct) {
		this.availableProduct = availableProduct;
	}

//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}
//	
	
	
	

}
