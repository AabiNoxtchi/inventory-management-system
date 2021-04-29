package com.example.inventoryui.Models.Inventory;


import com.example.inventoryui.Annotations.CheckBoxAnnotation;
import com.example.inventoryui.Annotations.DateAnnotation;
import com.example.inventoryui.Annotations.DropDownAnnotation;
import com.example.inventoryui.Annotations.EnumAnnotation;
import com.example.inventoryui.Annotations.IntegerInputAnnotation;
import com.example.inventoryui.Annotations.RadioGroupAnnotation;
import com.example.inventoryui.Annotations.SkipAnnotation;
import com.example.inventoryui.Models.Shared.BaseFilterVM;
import com.example.inventoryui.Models.Shared.SelectItem;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterVM extends BaseFilterVM implements Serializable {

	@CheckBoxAnnotation(title="all", target = "all")
	private Boolean all;

	@SkipAnnotation
	private Long employeeId;

	@IntegerInputAnnotation(target="priceMoreThan", title ="price more than")
	private BigDecimal priceMoreThan;
	@IntegerInputAnnotation(target="priceLessThan", title ="price less than")
	private BigDecimal priceLessThan;

	@RadioGroupAnnotation(title="discarded", target = "isDiscarded")
	private Boolean isDiscarded;
	
	@EnumAnnotation(target="econdition",title="select condition")
	private List<SelectItem> econditions;
	@SkipAnnotation
	private ECondition econdition;

	@SkipAnnotation
	private Long deliveryDetailId;
	
	@DropDownAnnotation(target="deliveryId",value="delivery.id",name="delivery.number",title="select delivery")
	private List<SelectItem> deliveryNumbers;
	@SkipAnnotation
	private Long deliveryId;

	@DropDownAnnotation(target="producId",value="product.id",name="product.name",title="select product")
	private List<SelectItem> productNames;
	@SkipAnnotation
	private Long productId;
	
	@DropDownAnnotation(target="id",value="id",name="inventoryNumber",title="select number", filterBy="productId")
	private List<SelectItem> inventoryNumbers;
	@SkipAnnotation
	private Long id;

	@SkipAnnotation
	private List<Long> ids;
	
	@EnumAnnotation(target="productType",title="product type")
	private List<SelectItem> productTypes;
	@SkipAnnotation
	private ProductType productType;

	@DateAnnotation(target="dateCreatedBefore",title="date created before")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date dateCreatedBefore;

	@DateAnnotation(target="dateCreatedAfter",title="date created after")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date dateCreatedAfter;

	// for DMA type
	@IntegerInputAnnotation(target="amortizationPercentMoreThan", title ="amortization percent more than")
	private Integer amortizationPercentMoreThan;
	@IntegerInputAnnotation(target="amortizationPercentLessThan", title ="amortization percent less than")
	private Integer amortizationPercentLessThan;

	@SkipAnnotation
	private Boolean freeInventory;
	@SkipAnnotation
	private List<Long> notIn;

	@SkipAnnotation
	private Map<String,Object> urlParameters;

	/*************************************************/

	public Map<String, Object> getUrlParameters() {
		if(urlParameters == null)
			urlParameters = new HashMap<>();
		return urlParameters;
	}
	public void setUrlParameters(Map<String, Object> urlParameters) {
		this.urlParameters = urlParameters;
	}
	@Override
	public Boolean getAll() {
		return all;
	}

	@Override
	public void setAll(Boolean all) {
		this.all = all;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public BigDecimal getPriceMoreThan() {
		return priceMoreThan;
	}

	public void setPriceMoreThan(BigDecimal priceMoreThan) {
		this.priceMoreThan = priceMoreThan;
	}

	public BigDecimal getPriceLessThan() {
		return priceLessThan;
	}

	public void setPriceLessThan(BigDecimal priceLessThan) {
		this.priceLessThan = priceLessThan;
	}

	public Boolean getDiscarded() {
		return isDiscarded;
	}

	public void setDiscarded(Boolean discarded) {
		isDiscarded = discarded;
	}

	public List<SelectItem> getEconditions() {
		return econditions;
	}

	public void setEconditions(List<SelectItem> econditions) {
		this.econditions = econditions;
	}

	public ECondition getEcondition() {
		return econdition;
	}

	public void setEcondition(ECondition econdition) {
		this.econdition = econdition;
	}

	public Long getDeliveryDetailId() {
		return deliveryDetailId;
	}

	public void setDeliveryDetailId(Long deliveryDetailId) {
		this.deliveryDetailId = deliveryDetailId;
	}

	public List<SelectItem> getDeliveryNumbers() {
		return deliveryNumbers;
	}

	public void setDeliveryNumbers(List<SelectItem> deliveryNumbers) {
		this.deliveryNumbers = deliveryNumbers;
	}

	public Long getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(Long deliveryId) {
		this.deliveryId = deliveryId;
	}

	public List<SelectItem> getProductNames() {
		return productNames;
	}

	public void setProductNames(List<SelectItem> productNames) {
		this.productNames = productNames;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public List<SelectItem> getInventoryNumbers() {
		return inventoryNumbers;
	}

	public void setInventoryNumbers(List<SelectItem> inventoryNumbers) {
		this.inventoryNumbers = inventoryNumbers;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public List<SelectItem> getProductTypes() {
		return productTypes;
	}

	public void setProductTypes(List<SelectItem> productTypes) {
		this.productTypes = productTypes;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public Date getDateCreatedBefore() {
		return dateCreatedBefore;
	}

	public void setDateCreatedBefore(Date dateCreatedBefore) {
		this.dateCreatedBefore = dateCreatedBefore;
	}

	public Date getDateCreatedAfter() {
		return dateCreatedAfter;
	}

	public void setDateCreatedAfter(Date dateCreatedAfter) {
		this.dateCreatedAfter = dateCreatedAfter;
	}

	public Integer getAmortizationPercentMoreThan() {
		return amortizationPercentMoreThan;
	}

	public void setAmortizationPercentMoreThan(Integer amortizationPercentMoreThan) {
		this.amortizationPercentMoreThan = amortizationPercentMoreThan;
	}

	public Integer getAmortizationPercentLessThan() {
		return amortizationPercentLessThan;
	}

	public void setAmortizationPercentLessThan(Integer amortizationPercentLessThan) {
		this.amortizationPercentLessThan = amortizationPercentLessThan;
	}

	public Boolean getFreeInventory() {
		return freeInventory;
	}

	public void setFreeInventory(Boolean freeInventory) {
		this.freeInventory = freeInventory;
	}

	public List<Long> getNotIn() {
		return notIn;
	}

	public void setNotIn(List<Long> notIn) {
		this.notIn = notIn;
	}


}

