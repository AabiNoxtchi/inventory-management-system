package com.inventory.inventory.Model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;

@Entity
@Table(name = "delivery_detail")
public class DeliveryDetail extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional = false)
	@Basic(fetch = FetchType.LAZY)
	private Delivery delivery;
	
	@ManyToOne(optional = false)
	@Basic(fetch = FetchType.LAZY)
	private AvailableProduct availableProduct;
	
	private int quantity;
	
	@DecimalMin(value = "0.0", inclusive = false)
	@Column(nullable= false, precision=9, scale=2) 
   // @Digits( fraction=2, integer = Integer.MAX_VALUE)
	//@Column(columnDefinition = "DECIMAL(7,2)")
    private BigDecimal price;
	//private Long price;
	
	
	// ************** //
	public Delivery getDelivery() {
		return delivery;
	}
	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public AvailableProduct getAvailableProduct() {
		return availableProduct;
	}
	public void setAvailableProduct(AvailableProduct availableProduct) {
		this.availableProduct = availableProduct;
	}
	
	
	

}
