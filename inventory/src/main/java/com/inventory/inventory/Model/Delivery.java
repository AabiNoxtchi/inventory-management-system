package com.inventory.inventory.Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryInit;


@Entity
@Table(name = "delivery")
public class Delivery extends BaseEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long number; 
	
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate date ;//= new Date();

	@ManyToOne(optional = false)
	//@Basic(fetch = FetchType.EAGER)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	//@Formula("(select supplier_id)")
	private Supplier supplier;
	
	@Formula("(select supplier_id)")
	private Long supplierId;
	
	@Formula("(select s.name from supplier s where s.id = supplier_id)")
	private String supplierName;
	
	//@QueryInit("*.*")
	@OneToMany(mappedBy="delivery", cascade = CascadeType.ALL)//, orphanRemoval = true)
	//@Basic(fetch = FetchType.EAGER)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<DeliveryDetail> deliveryDetails ;//= new ArrayList<>();
	
	//??????????????????????????????????????????
	@Formula("(select sum(dd.price_per_one) from "
			+ "delivery_detail dd inner join product_detail pd on dd.id=pd.delivery_detail_id "
			+ "where dd.delivery_id = id)")		
    private Double total; // totalBill
	
//	@Formula("(select count(dd.id) from "			
//			+ "product_detail pd inner join delivery_detail dd on dd.id=pd.delivery_detail_id "
//			+ "where dd.product_id = id)")
//			
//	private Long count; // total count

	public Delivery() {
		super();
	}
	
	public Delivery(Long id) {
		this.setId(id);
	}

	public Delivery(Supplier supplier, LocalDate date) {
		super();
		this.supplier = supplier;
		this.date = date;		
	}
	
	

	// ************** //
	
	
	public Delivery(Long number, LocalDate date, Supplier supplier) {
		super();
		this.number = number;
		this.date = date;
		this.supplier = supplier;
	}
	
	public void addDeliveryDetail(DeliveryDetail dd){
		//System.out.println("dd == null = "+(dd==null));
		  getDeliveryDetails().add(dd);
		   //deliveryDetails.add(dd);
		   dd.setDelivery(this);
		}

		public void removeDeliveryDetail(DeliveryDetail dd){
		   deliveryDetails.remove(dd);
		   dd.setDelivery(null);
		}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public List<DeliveryDetail> getDeliveryDetails() {
		if(deliveryDetails == null)
			return new ArrayList<>();
		return deliveryDetails;
	}

	public void setDeliveryDetails(List<DeliveryDetail> deliveryDetails) {
		this.deliveryDetails = deliveryDetails;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}
	
	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	@Override
	public String toString() {
		return "Delivery [number=" + number + ", date=" + date + ", supplier=" + supplier + ", supplierId=" + supplierId
				+ ", supplierName=" + supplierName + ", total=" + total + "]";
	}

	

}
