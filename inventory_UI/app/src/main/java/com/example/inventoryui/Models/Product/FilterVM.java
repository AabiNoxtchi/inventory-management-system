package com.example.inventoryui.Models.Product;

import com.example.inventoryui.Models.Shared.BaseFilterVM;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class FilterVM extends BaseFilterVM implements Serializable {
	
	private String name;
	private Long userId;
	private Long employeeId;
	private Boolean freeProducts;
	private String inventoryNumber;
	// private String description;
	private ProductType productType;
	private Boolean isDiscarded;
	private Boolean isAvailable;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
	private Date dateCreatedBefore;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
	private Date dateCreatedAfter;
	private Integer yearsToDiscardFromStartMoreThan;
	private Integer yearsToDiscardFromStartLessThan;
	private Integer yearsLeftToDiscardMoreThan;
	private Integer yearsLeftToDiscardLessThan;
	// for DMA type
	private Integer amortizationPercentMoreThan;
	private Integer amortizationPercentLessThan;
	// for DMA type
	private Integer yearsToMAConvertionMoreThan;
	private Integer yearsToMAConvertionLessThan;
	private Integer yearsLeftToMAConvertionMoreThan;
	private Integer yearsLeftToMAConvertionLessThan;
	private List<Long> ids;

	public FilterVM() {}

	//******** getters and setters ********//

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getEmployeeId() { return employeeId;}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Boolean getFreeProducts() {
		return freeProducts;
	}

	public void setFreeProducts(Boolean freeProducts) {
		this.freeProducts = freeProducts;
	}

	public String getInventoryNumber() {
		return inventoryNumber;
	}

	public void setInventoryNumber(String inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public Boolean getIsDiscarded() {
		return isDiscarded;
	}
	public void setIsDiscarded(Boolean isDiscarded) {
		this.isDiscarded = isDiscarded;
	}
	public Boolean getIsAvailable() {
		return isAvailable;
	}
	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
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

	public Integer getYearsToDiscardFromStartMoreThan() {
		return yearsToDiscardFromStartMoreThan;
	}

	public void setYearsToDiscardFromStartMoreThan(Integer yearsToDiscardFromStartMoreThan) {
		this.yearsToDiscardFromStartMoreThan = yearsToDiscardFromStartMoreThan;
	}

	public Integer getYearsToDiscardFromStartLessThan() {
		return yearsToDiscardFromStartLessThan;
	}

	public void setYearsToDiscardFromStartLessThan(Integer yearsToDiscardFromStartLessThan) {
		this.yearsToDiscardFromStartLessThan = yearsToDiscardFromStartLessThan;
	}

	public Integer getYearsLeftToDiscardMoreThan() {
		return yearsLeftToDiscardMoreThan;
	}

	public void setYearsLeftToDiscardMoreThan(Integer yearsLeftToDiscardMoreThan) {
		this.yearsLeftToDiscardMoreThan = yearsLeftToDiscardMoreThan;
	}

	public Integer getYearsLeftToDiscardLessThan() {
		return yearsLeftToDiscardLessThan;
	}

	public void setYearsLeftToDiscardLessThan(Integer yearsLeftToDiscardLessThan) {
		this.yearsLeftToDiscardLessThan = yearsLeftToDiscardLessThan;
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

	public Integer getYearsToMAConvertionMoreThan() {
		return yearsToMAConvertionMoreThan;
	}

	public void setYearsToMAConvertionMoreThan(Integer yearsToMAConvertionMoreThan) {
		this.yearsToMAConvertionMoreThan = yearsToMAConvertionMoreThan;
	}

	public Integer getYearsToMAConvertionLessThan() {
		return yearsToMAConvertionLessThan;
	}

	public void setYearsToMAConvertionLessThan(Integer yearsToMAConvertionLessThan) {
		this.yearsToMAConvertionLessThan = yearsToMAConvertionLessThan;
	}

	public Integer getYearsLeftToMAConvertionMoreThan() {
		return yearsLeftToMAConvertionMoreThan;
	}

	public void setYearsLeftToMAConvertionMoreThan(Integer yearsLeftToMAConvertionMoreThan) {
		this.yearsLeftToMAConvertionMoreThan = yearsLeftToMAConvertionMoreThan;
	}

	public Integer getYearsLeftToMAConvertionLessThan() {
		return yearsLeftToMAConvertionLessThan;
	}

	public void setYearsLeftToMAConvertionLessThan(Integer yearsLeftToMAConvertionLessThan) {
		this.yearsLeftToMAConvertionLessThan = yearsLeftToMAConvertionLessThan;
	}

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
}

