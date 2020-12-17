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
@Table(name = "availableProduct", 
		uniqueConstraints = { 
		@UniqueConstraint(columnNames = "inventoryNumber")
		})
public class AvailableProduct extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional = false)
	@Basic(fetch = FetchType.LAZY)
	private DeliveryDetail deliveryDetail;
	
	private String inventoryNumber;
	
	private boolean isDiscarded;

	private boolean isAvailable;
	
	@ManyToOne(fetch = FetchType.LAZY , optional = false)
	@JsonIgnore
	private User user;
	
	@OneToMany(mappedBy = "availableProduct",cascade = CascadeType.ALL) 
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<EventProduct> eventProduct;
	
	// ************** //
	public DeliveryDetail getDeliveryDetail() {
		return deliveryDetail;
	}

	public void setDeliveryDetail(DeliveryDetail deliveryDetail) {
		this.deliveryDetail = deliveryDetail;
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
	
	public List<EventProduct> getEventProduct() {
		return eventProduct;
	}

	public void setEventProduct(List<EventProduct> eventProduct) {
		this.eventProduct = eventProduct;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
