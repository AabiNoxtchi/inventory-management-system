package com.inventory.inventory.Model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.querydsl.core.annotations.QueryInit;

@Entity
@Table(name = "productDetail"/*, 
		uniqueConstraints = { 
		@UniqueConstraint(columnNames = "inventoryNumber")
		}*/)
public class ProductDetail extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String inventoryNumber;
	
	private boolean isDiscarded;
	
	@Column(nullable = true) 
    private ECondition econdition;

	
	//private boolean isAvailable;
		
	//private ECondition condition;
	
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
	
	/*@ManyToOne(optional = true)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private User user;*/
	
	/*@Formula("(select u.user_name from user u where u.id = user_id)")
	private String employeeName;*/
	
//	@Formula("(select p.name from product p inner join delivery_detail dd on p.id = dd.product_id where dd.id = delivery_detail_id)")
//	private String productName;
//	
//	@Formula("(select p.product_type from product p inner join delivery_detail dd on p.id = dd.product_id where dd.id = delivery_detail_id)")
//	private ProductType productType;
//	
//	@Formula("(select p.amortization_percent from product p inner join delivery_detail dd on p.id = dd.product_id where dd.id = delivery_detail_id)")
//	private double amortizationPercent;
//	
//	@Formula("(select d.date from delivery d inner join delivery_detail dd on d.id = dd.delivery_id where dd.id = delivery_detail_id)")
//	private LocalDate dateCreated;
//	
//	@Formula("(select d.number from delivery d inner join delivery_detail dd on d.id = dd.delivery_id where dd.id = delivery_detail_id)")
//	private Long deliveryNumber;
//	
//	@Formula("(select dd.price_per_one from delivery_detail dd where dd.id = delivery_detail_id)")
//	private BigDecimal price ;
//	
//	//@Formula("select TIMESTAMPDIFF('MONTH', d.date, NOW())/12 from delivery d inner join delivery_detail dd on d.id = dd.delivery_id where dd.id = delivery_detail_id")
//	//private BigDecimal totalAmortization ;
//	
//	//@Formula("(select DATEDIFF('month',d.date,NOW()) from delivery d inner join delivery_detail dd on d.id = dd.delivery_id where dd.id = delivery_detail_id)")
//	//private Long months;
//	
	public ProductDetail() {
		super();		
	}

	public ProductDetail(String inventoryNumber, boolean isDiscarded, ECondition condition,//boolean isAvailable,
			DeliveryDetail deliveryDetail) {
		super();
		this.inventoryNumber = inventoryNumber;
		this.isDiscarded = isDiscarded;
		//this.isAvailable = isAvailable;
		this.econdition = condition;
		this.deliveryDetail = deliveryDetail;
		//this.user = user;
	}

	public ProductDetail(Long productDetailId) {
		this.setId(productDetailId);
	}

	public ProductDetail(String inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
	}

	//	@OneToMany(mappedBy = "productUserDetail",cascade = CascadeType.ALL) 
//	@Basic(fetch = FetchType.LAZY)
//	@JsonIgnore
//	private List<EventProduct> eventProduct;
//	
	/************/
	
//	public List<EventProduct> getEventProduct() {
//		return eventProduct;
//	}
//
//	public void setEventProduct(List<EventProduct> eventProduct) {
//		this.eventProduct = eventProduct;
//	}

//	public ECondition getConditionReturned() {
//		return conditionReturned;
//	}
//	public void setConditionReturned(ECondition conditionReturned) {
//		this.conditionReturned = conditionReturned;
//	}
	
	
	
	public String getInventoryNumber() {
		return inventoryNumber;
	}

//	public ECondition getCondition() {
//		return condition;
//	}
//
//	public void setCondition(ECondition condition) {
//		this.condition = condition;
//	}
	
	

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

//	public boolean isAvailable() {
//		return isAvailable;
//	}
//
//	public void setAvailable(boolean isAvailable) {
//		this.isAvailable = isAvailable;
//	}
	
	
	
	public DeliveryDetail getDeliveryDetail() {
		return deliveryDetail;
	}

//	public ECondition getCondition() {
//		return condition;
//	}
//
//	public void setCondition(ECondition condition) {
//		this.condition = condition;
//	}

	public void setDeliveryDetail(DeliveryDetail deliveryDetail) {
		this.deliveryDetail = deliveryDetail;
	}
	
	public void setDeliveryDetail(Long id) {
		this.deliveryDetail = new DeliveryDetail(id);
	}

	/*public User getUser() {
		return user;
	}

	public void setUser(InUser user) {
		this.user= user;
	}*/

//	public String getProductName() {
//		return productName;
//	}
//
//	public void setProductName(String productName) {
//		this.productName = productName;
//	}

	/*public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}*/
		
	public List<UserProfile> getUserProfiles() {
		if(userProfiles == null)
			userProfiles = new ArrayList<>();
		return userProfiles;
	}
	
	/*public void addToUserProfiles(UserProfile child) {
        child.setProductDetail(this);
        this.getUserProfiles().add(child);
    }*/

	public void setUserProfiles(List<UserProfile> userProfiles) {
		this.userProfiles = userProfiles;
		
		
	}
	
//	public Date getDateCreated() {
//		return dateCreated;
//	}
//
//	public void setDateCreated(Date dateCreated) {
//		this.dateCreated = dateCreated;
//	}
	

//	public Long getDeliveryNumber() {
//		return deliveryNumber;
//	}
//
//	public LocalDate getDateCreated() {
//		return dateCreated;
//	}
//
//	public void setDateCreated(LocalDate dateCreated) {
//		this.dateCreated = dateCreated;
//	}
//
//	public void setDeliveryNumber(Long deliveryNumber) {
//		this.deliveryNumber = deliveryNumber;
//	}
//	
//	
//
//	public BigDecimal getPrice() {
//		return price;
//	}
//
//	public void setPrice(BigDecimal price) {
//		this.price = price;
//	}
	
	

//	public double getAmortizationPercent() {
//		return amortizationPercent;
//	}
//
//	public void setAmortizationPercent(double amortizationPercent) {
//		this.amortizationPercent = amortizationPercent;
//	}
//	
//	
//
//	public ProductType getProductType() {
//		return productType;
//	}
//
//	public void setProductType(ProductType productType) {
//		this.productType = productType;
//	}

//	@JsonIgnore
//	public Long getMonths() {
//		
//		LocalDate now = LocalDate.now();		 
//		ZonedDateTime zonedDateTime = now.atStartOfDay(ZoneId.of("UTC"));
//		now = zonedDateTime.toLocalDate();
//		System.out.println("utc time = "+now);
//				
//		
//		return 
//			//ChronoUnit.MONTHS.between(dateCreated.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now());
//				//MONTHS.between(dateCreated, LocalDate.now());// ????????????????????????????
//				MONTHS.between(dateCreated, now);
//				
//	}
//	
//	public BigDecimal getTotalAmortization() {
//		//BigDecimal amor = BigDecimal.ZERO;
//		System.out.println("productDetails.getTotalAmortization() : ");
//		System.out.println("months = "+getMonths());
//		System.out.println("amortizationPercent = "+amortizationPercent);
//		try {
//		Double calc = (amortizationPercent/100.0)*(getMonths()/12.0);
//		System.out.println("calc = "+calc);
//		System.out.println("price = "+price);
//		BigDecimal amor = price.multiply(new BigDecimal(calc.toString()));
//		System.out.println("amor = "+amor);
//		amor.setScale(3,BigDecimal.ROUND_HALF_UP);
//		System.out.println("amor = "+amor);
//		return amor;
//		}catch(Exception e){
//			/**********************/
//			System.out.println("Exception = "+e);
//			return new BigDecimal("0.0");
//		}
//		
//	}
	
	

	public Long getDeliveryDetailId() {
		return deliveryDetailId;
	}

	

	public void setDeliveryDetailId(Long deliveryDetailId) {
		this.deliveryDetailId = deliveryDetailId;
	}
	
	

//	@Override
//	public String toString() {
//		return "ProductDetail [inventoryNumber=" + inventoryNumber + ", isDiscarded=" + isDiscarded + ", condition="
//				+ condition + ", deliveryDetail=" + deliveryDetail + ", userProfiles=" + userProfiles
//				+ ", deliveryDetailId=" + deliveryDetailId + ", productId=" + productId + "]";
//	}

//	@Override
//	public String toString() {
//		return "ProductDetail [inventoryNumber=" + inventoryNumber + ", isDiscarded=" + isDiscarded + ", isAvailable="
//				+ isAvailable + "]";
//	}
	
	
	
	
}
