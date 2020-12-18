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
@Table(name = "availableProduct")
public class AvailableProduct extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional = false)
	@Basic(fetch = FetchType.LAZY)
	private Product product;
	
	@ManyToOne(optional = false)
	@Basic(fetch = FetchType.LAZY)
	private DeliveryDetail deliveryDetail;
	
	@OneToMany(mappedBy = "availableProduct",cascade = CascadeType.ALL) 
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ProductDetail> productDetail;
	
	
	
	
	
	// ************** //
	public DeliveryDetail getDeliveryDetail() {
		return deliveryDetail;
	}

	public void setDeliveryDetail(DeliveryDetail deliveryDetail) {
		this.deliveryDetail = deliveryDetail;
	}

	
	

	
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}

	public List<ProductDetail> getProductDetail() {
		return productDetail;
	}

	public void setProductDetail(List<ProductDetail> productDetail) {
		this.productDetail = productDetail;
	}
	
	
	
	
}
