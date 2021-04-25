package com.inventory.inventory.Model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryInit;

@Entity
@Table(name = "productDetail")
public class ProductDetail extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String inventoryNumber;
	
	private boolean isDiscarded;
	
	private double totalAmortizationPercent;
	
	@Column(nullable = false) 
    private ECondition econdition = ECondition.Available;
	
	@QueryInit("*.*")
	@ManyToOne(optional = false)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private DeliveryDetail deliveryDetail;
	
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	@OneToMany(mappedBy = "productDetail", cascade = CascadeType.ALL)
    private List<UserProfile> userProfiles;
	
	@Formula("(select delivery_detail_id)")
	private Long deliveryDetailId;
	
	@Formula("(select p.id from product p inner join delivery_detail dd on p.id = dd.product_id where dd.id = delivery_detail_id)")
	private Long productId;

	public ProductDetail() {
		super();		
	}
	
	public ProductDetail(Long productDetailId) {
		this.setId(productDetailId);
	}

	public ProductDetail(String inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
	}

	public ProductDetail(String inventoryNumber, boolean isDiscarded, ECondition condition,//boolean isAvailable,
			DeliveryDetail deliveryDetail) {
		super();
		this.inventoryNumber = inventoryNumber;
		this.isDiscarded = isDiscarded;
		this.econdition = condition;
		this.deliveryDetail = deliveryDetail;		
	}
	
	public String getInventoryNumber() {
		return inventoryNumber;
	}
	
	public void setInventoryNumber(String inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
	}

	public ECondition getEcondition() {
		return econdition;
	}

	public void setEcondition(ECondition econdition) {
		this.econdition = econdition;
	}

	public boolean isDiscarded() {
		return isDiscarded;
	}

	public void setDiscarded(boolean isDiscarded) {
		this.isDiscarded = isDiscarded;
	}

	public DeliveryDetail getDeliveryDetail() {
		return deliveryDetail;
	}
	
	public void setDeliveryDetail(DeliveryDetail deliveryDetail) {
		this.deliveryDetail = deliveryDetail;
	}
	
	public void setDeliveryDetail(Long id) {
		this.deliveryDetail = new DeliveryDetail(id);
	}
		
	public List<UserProfile> getUserProfiles() {
		if(userProfiles == null)
			userProfiles = new ArrayList<>();
		return userProfiles;
	}

	public void setUserProfiles(List<UserProfile> userProfiles) {
		this.userProfiles = userProfiles;
	}


	public Double getTotalAmortizationPercent() {
		return totalAmortizationPercent;
	}

	public void setTotalAmortizationPercent(Double totalAmortizationPercent) {
		this.totalAmortizationPercent = totalAmortizationPercent;
	}

	public Long getDeliveryDetailId() {
		return deliveryDetailId;
	}

	public void setDeliveryDetailId(Long deliveryDetailId) {
		this.deliveryDetailId = deliveryDetailId;
	}
	
}


