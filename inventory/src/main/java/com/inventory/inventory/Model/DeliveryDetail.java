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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryInit;

@QueryEntity
@Entity
@Table(name = "deliveryDetail")
public class DeliveryDetail extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	@DecimalMin(value = "0.0", inclusive = false)
	@Column(nullable= false, precision=9, scale=2) 
    private BigDecimal pricePerOne;
	
	
	@ManyToOne(optional = false)
	@Basic(fetch = FetchType.LAZY)
	@JoinColumn(name = "delivery_id", nullable = false)
	@JsonIgnore
	private Delivery delivery;
	
//	@QueryInit("*.*")
//	@ManyToOne(optional = false)
//	@Basic(fetch = FetchType.EAGER)
//	@JsonIgnore
//	private AvailableProduct availableProduct;
	
	// @QueryInit("*.*")
	@ManyToOne(optional = false)	
	@Basic(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	@JsonIgnore
	private Product product;
	
	@OneToMany(mappedBy = "deliveryDetail", cascade = CascadeType.ALL)//, orphanRemoval = true)	
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ProductDetail> productDetails;
	
	//@Formula("(select p.name from product p inner join available_product ap on ap.product_id = p.id where ap.id = available_product_id)")	
		@Formula("(select p.name from product p where p.id = product_id)")	
	    private String productName;
	
	@Formula("(select product_id)")	
    private Long productId;
	
	@Formula("(select count(*) from product_detail pd where pd.delivery_detail_id=id)")
	private int quantity;
	
	
	
	
	public DeliveryDetail() {
		super();
	}	
	
	public DeliveryDetail( @DecimalMin(value = "0.0", inclusive = false) BigDecimal price, Delivery delivery,
			Product product) {
		super();
		//this.quantity = quantity;
		this.pricePerOne = price;
		this.delivery = delivery;
		this.product = product;
	}
	
	public DeliveryDetail(Long parentId) {
		this.setId(parentId);
		// TODO Auto-generated constructor stub
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

	public BigDecimal getPricePerOne() {
		return pricePerOne;
	}

	public void setPricePerOne(BigDecimal pricePerOne) {
		this.pricePerOne = pricePerOne;
	}

	

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<ProductDetail> getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(List<ProductDetail> productDetails) {
		this.productDetails = productDetails;
	}
//	public AvailableProduct getAvailableProduct() {
//		return availableProduct;
//	}
//	public void setAvailableProduct(AvailableProduct availableProduct) {
//		this.availableProduct = availableProduct;
//	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	
	

}
