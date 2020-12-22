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
import com.inventory.inventory.Model.User.InUser;
import com.inventory.inventory.Model.User.User;

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
	
	
	
	private String inventoryNumber;
	
	private boolean isDiscarded;

	private boolean isAvailable;
	
//	@ManyToOne(optional = true)
//	@Basic(fetch = FetchType.LAZY)
//	@JsonIgnore
//	private AvailableProduct availableProduct;
	
	@ManyToOne(optional = false)
	@Basic(fetch = FetchType.LAZY)
	private DeliveryDetail deliveryDetail;
	
	@ManyToOne(optional = true)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private User inUser;
	
	
	
	public ProductDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public ProductDetail(String inventoryNumber, boolean isDiscarded, boolean isAvailable,
			DeliveryDetail deliveryDetail, User inUser) {
		super();
		this.inventoryNumber = inventoryNumber;
		this.isDiscarded = isDiscarded;
		this.isAvailable = isAvailable;
		this.deliveryDetail = deliveryDetail;
		this.inUser = inUser;
	}



	//	@OneToMany(mappedBy = "productUserDetail",cascade = CascadeType.ALL) 
//	@Basic(fetch = FetchType.LAZY)
//	@JsonIgnore
//	private List<EventProduct> eventProduct;
//	
	/************/
	
//	public List<EventProduct> getEventProduct() {
//		return eventProduct;
//	}
//
//	public void setEventProduct(List<EventProduct> eventProduct) {
//		this.eventProduct = eventProduct;
//	}

	
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

//	public AvailableProduct getAvailableProduct() {
//		return availableProduct;
//	}
//
//	public void setAvailableProduct(AvailableProduct availableProduct) {
//		this.availableProduct = availableProduct;
//	}
	
	public DeliveryDetail getDeliveryDetail() {
		return deliveryDetail;
	}

	public void setDeliveryDetail(DeliveryDetail deliveryDetail) {
		this.deliveryDetail = deliveryDetail;
	}

	public User getInUser() {
		return inUser;
	}

	public void setInUser(User inUser) {
		this.inUser = inUser;
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
