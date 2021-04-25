package com.inventory.inventory.ViewModels.Delivery;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Annotations.DropDownAnnotation;
import com.inventory.inventory.Model.Delivery;
import com.inventory.inventory.Model.DeliveryDetail;
import com.inventory.inventory.Model.Supplier;
import com.inventory.inventory.ViewModels.Shared.BaseEditVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;

public class EditVM extends BaseEditVM<Delivery>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long number; 	

	private LocalDate date ;

	@DropDownAnnotation(target="supplierId",value="supplier.id",name="supplier.name",title="select supplier")
	private List<SelectItem> suppliers;
	private Long supplierId;
	
	@JsonIgnore
	private List<DeliveryDetail> deliveryDetails;
	
	private List<com.inventory.inventory.ViewModels.DeliveryDetail.EditVM> deliveryDetailEditVMs;
	
	
	private List<Long> deletedDetailsIds;
	private String[] ddDeleteErrors;
	
	@DropDownAnnotation(target="productId",value="supplier.id",name="supplier.name",title="select supplier")
	private List<SelectItem> products;
	
	//for post
	@JsonIgnore
	private Supplier supplier;
	
	@Override
	public void populateModel(Delivery item) {
		setId(item.getId());
		number = item.getNumber();
		date = item.getDate();
		supplierId = item.getSupplierId();
		deliveryDetails = item.getDeliveryDetails();		
	}

	@Override
	public void populateEntity(Delivery item) {
		item.setId(getId());
		item.setNumber(number);
		item.setDate(date);
		item.setSupplier(supplier);		
	}

	public Long getNumber() {
		return number;
	}
	public void setNumber(Long number) {
		this.number = number;
	}
	public List<SelectItem> getSuppliers() {
		return suppliers;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public void setSuppliers(List<SelectItem> suppliers) {
		this.suppliers = suppliers;
	}
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}
	public List<com.inventory.inventory.ViewModels.DeliveryDetail.EditVM> getDeliveryDetailEditVMs() {
		return deliveryDetailEditVMs;
	}
	public void setDeliveryDetailEditVMs(
			List<com.inventory.inventory.ViewModels.DeliveryDetail.EditVM> deliveryDetailEditVMs) {
		this.deliveryDetailEditVMs = deliveryDetailEditVMs;
	}
	public List<DeliveryDetail> getDeliveryDetails() {
		if(deliveryDetails == null)
			return new ArrayList<>();
		return deliveryDetails;
	}
	public void setDeliveryDetails(List<DeliveryDetail> deliveryDetails) {
		this.deliveryDetails = deliveryDetails;
	}	
	public List<SelectItem> getProducts() {
		return products;
	}
	public void setProducts(List<SelectItem> products) {
		this.products = products;
	}
	public Supplier getSupplier() {
		return supplier;
	}
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	public List<Long> getDeletedDetailsIds() {
		return deletedDetailsIds;
	}
	public void setDeletedDetailsIds(List<Long> deletedDetailsIds) {
		this.deletedDetailsIds = deletedDetailsIds;
	}
	public String[] getDdDeleteErrors() {
		return ddDeleteErrors;
	}
	public void setDdDeleteErrors(String[] ddDeleteErrors) {
		this.ddDeleteErrors = ddDeleteErrors;
	}
	
}


