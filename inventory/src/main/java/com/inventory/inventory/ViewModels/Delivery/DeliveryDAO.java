package com.inventory.inventory.ViewModels.Delivery;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.Delivery;
import com.inventory.inventory.Model.DeliveryDetail;
import com.inventory.inventory.Model.Supplier;

public class DeliveryDAO {	
 
	//private Delivery delivery;
	private Long id;
	
	private Long number; 
	
	private LocalDate date ;//= new Date();

	private Long supplierId;
	
	private String supplierName;
		
    private Double total; // totalBill
	
	private List<DeliveryDetail> deliveryDetails ;//= new ArrayList<>();
	
	public DeliveryDAO(Delivery delivery, List<DeliveryDetail> deliveryDetails) {
		super();
		this.id = delivery.getId();
		this.number = delivery.getNumber();
		this.date = delivery.getDate();
		this.supplierId = delivery.getSupplierId();
		this.supplierName = delivery.getSupplierName();
		this.total = delivery.getTotal() != null ? delivery.getTotal() : 0;
		this.deliveryDetails = deliveryDetails;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getNumber() {
		return number;
	}
	public void setNumber(Long number) {
		this.number = number;
	}
//	public Date getDate() {
//		return date;
//	}
//	public void setDate(Date date) {
//		this.date = date;
//	}
	
	public Long getSupplierId() {
		return supplierId;
	}
	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
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
	public double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total != null ? total : 0;
	}
	public List<DeliveryDetail> getDeliveryDetails() {
		return deliveryDetails;
	}
	public void setDeliveryDetails(List<DeliveryDetail> deliveryDetails) {
		this.deliveryDetails = deliveryDetails;
	}
		
  
}
