package com.inventory.inventory.Model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryEntity;

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

	@ManyToOne(optional = false)	
	@Basic(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	@JsonIgnore
	private Product product;
	
	@OneToMany(mappedBy = "deliveryDetail", cascade = CascadeType.ALL)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ProductDetail> productDetails;
	
	@Formula("(select p.name from product p where p.id = product_id)")	
    private String productName;
	
	@Formula("(select product_id)")	
    private Long productId;
	
	@Formula("(select count(*) from product_detail pd where pd.delivery_detail_id=id)")
	private int quantity = 0;
	
	public DeliveryDetail() {
		super();
	}	
	
	public DeliveryDetail(Long parentId) {
		this.setId(parentId);
	}
	
	public DeliveryDetail( @DecimalMin(value = "0.0", inclusive = false) BigDecimal price, Delivery delivery,
			Product product) {
		super();
		this.pricePerOne = price;
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

	public String getProductName() {
		return productName;
	}

	public Long getProductId() {
		return productId;
	}

}


