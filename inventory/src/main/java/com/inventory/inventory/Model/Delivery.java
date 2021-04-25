package com.inventory.inventory.Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "delivery")
public class Delivery extends BaseEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long number; 
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate date ;

	@ManyToOne(optional = false)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private Supplier supplier;
	
	@OneToMany(mappedBy="delivery", cascade = CascadeType.ALL)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<DeliveryDetail> deliveryDetails ;
	
	@Formula("(select sum(dd.price_per_one) from "
			+ "delivery_detail dd inner join product_detail pd on dd.id=pd.delivery_detail_id "
			+ "where dd.delivery_id = id)")		
    private Double total; 
	
	@Formula("(select supplier_id)")
	private Long supplierId;
	
	@Formula("(select s.name from supplier s where s.id = supplier_id)")
	private String supplierName;
	

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
	
	public Delivery(Long number, LocalDate date, Supplier supplier) {
		super();
		this.number = number;
		this.date = date;
		this.supplier = supplier;
	}
	
	public void addDeliveryDetail(DeliveryDetail dd){
		  getDeliveryDetails().add(dd);
		  dd.setDelivery(this);
	}
	
	public void removeDeliveryDetail(DeliveryDetail dd){
		   deliveryDetails.remove(dd);
		   dd.setDelivery(null);
	}		

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
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
	
	
	public Double getTotal() {
		return total;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}
}


