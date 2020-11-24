package com.example.inventoryui.Models.Product;

import com.example.inventoryui.Annotations.ChechBoxAnnotation;
import com.example.inventoryui.Annotations.DateAnnotation;
import com.example.inventoryui.Annotations.DropDownAnnotation;
import com.example.inventoryui.Annotations.EnumAnnotation;
import com.example.inventoryui.Annotations.SkipAnnotation;
import com.example.inventoryui.Models.Shared.BaseFilterVM;
import com.example.inventoryui.Models.Shared.SelectItem;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class FilterVM extends BaseFilterVM implements Serializable {


	@SkipAnnotation
	private Boolean employeeIdOrFree;

	@ChechBoxAnnotation(title="all", target = "all")
	private Boolean all;

	@ChechBoxAnnotation(title="discarded", target = "isDiscarded")
	private Boolean isDiscarded;

	@ChechBoxAnnotation(title="available", target = "isAvailable")
	private Boolean isAvailable;

	@ChechBoxAnnotation(title="free Products", target = "freeProducts")
	private Boolean freeProducts;

	@SkipAnnotation()
	private String name;

	@SkipAnnotation()
	private Long userId;

	@SkipAnnotation()
	private Long employeeId;

	@SkipAnnotation()
	private String inventoryNumber;

	@DropDownAnnotation(target = "name", name="name", value = "name", title="select name")
	private List<SelectItem> names;

	@DropDownAnnotation(target="employeeId",value="employee.id",name="employee.userName",title="select employee")
	private List<SelectItem> employeenames;

	@DropDownAnnotation(target="inventoryNumber",value="inventoryNumber",name="inventoryNumber",title="select number")
	private List<SelectItem> inventoryNumbers;

	@EnumAnnotation(target="productType",title="product type")
	private List<SelectItem> productTypes;

	@SkipAnnotation
	//@EnumAnnotation(target="productType",title="product type")
	//@Nullable
	private ProductType productType;

	@DateAnnotation(target="dateCreatedBefore",title="created before")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
	private Date dateCreatedBefore;
	@DateAnnotation(target="dateCreatedAfter",title="created after")
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

	public Boolean getAll() {
		return all;
	}

	public void setAll(Boolean all) {
		this.all = all;
	}

	public List<SelectItem> getNames() {
		return names;
	}

	public void setNames(List<SelectItem> names) {
		this.names = names;
	}

	public List<SelectItem> getEmployeenames() {
		return employeenames;
	}

	public void setEmployeenames(List<SelectItem> employeenames) {
		this.employeenames = employeenames;
	}

	public List<SelectItem> getInventoryNumbers() {
		return inventoryNumbers;
	}

	public void setInventoryNumbers(List<SelectItem> inventoryNumbers) {
		this.inventoryNumbers = inventoryNumbers;
	}

	public Boolean getEmployeeIdOrFree() {
		return employeeIdOrFree;
	}

	public void setEmployeeIdOrFree(Boolean employeeIdOrFree) {
		this.employeeIdOrFree = employeeIdOrFree;
	}

	public List<SelectItem> getProductTypes() {
		return productTypes;
	}

	public void setProductTypes(List<SelectItem> productTypes) {
		this.productTypes = productTypes;
	}
}

