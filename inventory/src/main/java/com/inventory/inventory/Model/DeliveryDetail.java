package com.inventory.inventory.Model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "delivery_detail")
public class DeliveryDetail extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int quantity;
	
	@DecimalMin(value = "0.0", inclusive = false)
	@Column(nullable= false, precision=9, scale=2) 
   // @Digits( fraction=2, integer = Integer.MAX_VALUE)
	//@Column(columnDefinition = "DECIMAL(7,2)")
    private BigDecimal price;
	//private Long price;
	
	@ManyToOne(optional = false)
	@Basic(fetch = FetchType.LAZY)
	private Delivery delivery;
	
	@ManyToOne(optional = false)
	@Basic(fetch = FetchType.LAZY)
	private Product product;
	
	@OneToMany()//mappedBy = "deliveryDetail",cascade = CascadeType.ALL , orphanRemoval = true) 
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<AvailableProduct> productDetail;
	
//	@ManyToOne(optional = false)
//	@Basic(fetch = FetchType.LAZY)
//	private AvailableProduct availableProduct;
	
	public DeliveryDetail() {
		super();
		// TODO Auto-generated constructor stub
	}	
	
	public DeliveryDetail(int quantity, @DecimalMin(value = "0.0", inclusive = false) BigDecimal price, Delivery delivery,
			Product product) {
		super();
		this.quantity = quantity;
		this.price = price;
		this.delivery = delivery;
		this.product = product;
	}
	
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
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}

	public List<AvailableProduct> getProductDetail() {
		return productDetail;
	}

	public void setProductDetail(List<AvailableProduct> productDetail) {
		this.productDetail = productDetail;
	}
//	public AvailableProduct getAvailableProduct() {
//		return availableProduct;
//	}
//	public void setAvailableProduct(AvailableProduct availableProduct) {
//		this.availableProduct = availableProduct;
//	}
	
	
	

}
