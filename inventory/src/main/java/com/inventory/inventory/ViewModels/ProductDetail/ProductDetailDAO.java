package com.inventory.inventory.ViewModels.ProductDetail;

import static java.time.temporal.ChronoUnit.MONTHS;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.DeliveryDetail;
import com.inventory.inventory.Model.ECondition;
import com.inventory.inventory.Model.ProductDetail;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.UserCategory;
import com.inventory.inventory.Model.UserProfile;
import com.querydsl.core.Tuple;
import com.querydsl.core.annotations.QueryInit;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

public class ProductDetailDAO {

	private Long id;
	private String inventoryNumber;
	
	private boolean isDiscarded;

	//private boolean isAvailable;
	private ECondition econdition;
	
	//@Formula("(select p.name from product p inner join delivery_detail dd on p.id = dd.product_id where dd.id = delivery_detail_id)")
	private String productName;
	
	//@Formula("(select p.product_type from product p inner join delivery_detail dd on p.id = dd.product_id where dd.id = delivery_detail_id)")
	private ProductType productType;
	
	//@Formula("(select p.amortization_percent from product p inner join delivery_detail dd on p.id = dd.product_id where dd.id = delivery_detail_id)")
	private double amortizationPercent = 0;
	
	//@Formula("(select d.date from delivery d inner join delivery_detail dd on d.id = dd.delivery_id where dd.id = delivery_detail_id)")
	private LocalDate dateCreated;
	
	//@Formula("(select d.number from delivery d inner join delivery_detail dd on d.id = dd.delivery_id where dd.id = delivery_detail_id)")
	private Long deliveryNumber;
	
	private Long deliveryDetailId;
	
	//@Formula("(select dd.price_per_one from delivery_detail dd where dd.id = delivery_detail_id)")
	private BigDecimal price ;
	private Double totalAmortizationPercent; 
	private BigDecimal totalAmortization;
	
	//@JsonIgnore
	//private String timeZone;
	
	//@Formula("select TIMESTAMPDIFF('MONTH', d.date, NOW())/12 from delivery d inner join delivery_detail dd on d.id = dd.delivery_id where dd.id = delivery_detail_id")
	//private BigDecimal totalAmortization ;
	
	//@Formula("(select DATEDIFF('month',d.date,NOW()) from delivery d inner join delivery_detail dd on d.id = dd.delivery_id where dd.id = delivery_detail_id)")
	//private Long months;
	
	

	/*public ProductDetailDAO(ProductDetail pd, String productName,ProductType productType,
			double amortizationPercent, LocalDate dateCreated, BigDecimal price) {
		this.id=pd.getId();
		this.inventoryNumber = pd.getInventoryNumber();
		this.isDiscarded = pd.isDiscarded();
		this.isAvailable = pd.isAvailable();
		this.productName=productName;
		this.productType=productType;
		this.amortizationPercent=amortizationPercent;
		this.dateCreated=dateCreated;
		this.price=price;
	}*/
	
	
	
	

	/*public ProductDetailDAO(Tuple x) {
		System.out.println(x.toString());
	}

	public ProductDetailDAO(QProductDetail pd, StringPath name, EnumPath<ProductType> productType2,
			DatePath<LocalDate> date, NumberPath<BigDecimal> pricePerOne) {
		System.out.println("pd = "+pd.toString()+" "+ name+" "+productType2+" "+date+" "+pricePerOne);
	}*/

	

	
	public ProductDetailDAO(ProductDetail pd, String name, UserCategory uc, //ProductType type,// Double amortizationPercent,
			LocalDate date, Long number, BigDecimal price) { //, String timeZone) {//, Double totalAmortization) {
		this.id = pd.getId();
		this.inventoryNumber = pd.getInventoryNumber();
		this.isDiscarded = pd.isDiscarded();
		//this.isAvailable = pd.isAvailable();
		this.econdition = pd.getEcondition();
		this.deliveryDetailId = pd.getDeliveryDetailId();
		
		this.productName= name;
		this.productType= uc.getCategory().getProductType();//type;
		this.amortizationPercent= uc.getAmortizationPercent();//amortizationPercent;
		this.dateCreated= date;
		this.deliveryNumber=number;
		this.price= price;
		this.totalAmortizationPercent = pd.getTotalAmortizationPercent();//totalAmortizationPercent();
		//this.timeZone = timeZone;
		//this.totalAmortization = 
		totalAmortization();
	}

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInventoryNumber() {
		return inventoryNumber;
	}

	public void setInventoryNumber(String inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
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
	
	

	public String getProductName() {
		return productName;
	}

	

	public ECondition getEcondition() {
		return econdition;
	}

	public void setEcondition(ECondition econdition) {
		this.econdition = econdition;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public double getAmortizationPercent() {
		return amortizationPercent;
	}

	public void setAmortizationPercent(double amortizationPercent) {
		this.amortizationPercent = amortizationPercent;
	}

	public LocalDate getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDate dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Long getDeliveryNumber() {
		return deliveryNumber;
	}

	public void setDeliveryNumber(Long deliveryNumber) {
		this.deliveryNumber = deliveryNumber;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	

	public Long getDeliveryDetailId() {
		return deliveryDetailId;
	}

	public void setDeliveryDetailId(Long deliveryDetailId) {
		this.deliveryDetailId = deliveryDetailId;
	}

	
	
	
	public Double getTotalAmortizationPercent() {
		return totalAmortizationPercent;
	}
	
	

	public BigDecimal getTotalAmortization() {
		return totalAmortization;
	}



	public void setTotalAmortization(BigDecimal totalAmortization) {
		this.totalAmortization = totalAmortization;
	}



	public void setTotalAmortizationPercent(Double totalAmortizationPercent) {
		this.totalAmortizationPercent = totalAmortizationPercent;
	}
	
	

//	private Double totalAmortizationPercent() {//ProductType type, Double amortization, LocalDate dateCreated) {
//				
//		if(productType != ProductType.LTA ) return 0.0;
//		if(isDiscarded) return 0.0;
//		
//		LocalDate now = LocalDate.now();		 
//		ZonedDateTime zonedDateTime = now.atStartOfDay(ZoneId.of(timeZone));// ???
//		now = zonedDateTime.toLocalDate();		
//		Long months = MONTHS.between(dateCreated, now);
//		
//		Double total = ( amortizationPercent * ( months/12.0 ));
//		return total <= 100 ? total : 100;
//				
//	}
	
//	public String getTimeZone() {
//		return timeZone;
//	}
//
//
//
//	public void setTimeZone(String timeZone) {
//		this.timeZone = timeZone;
//	}



	private BigDecimal totalAmortization() {
		try {
			Double percent = totalAmortizationPercent/100.0;
			//System.out.println("percent = "+percent);
			BigDecimal amount = BigDecimal.valueOf(percent);
			//System.out.println("amount = "+amount);
			this.totalAmortization = price.multiply(amount);
			//System.out.println("totalAmortization = "+totalAmortization);
		//System.out.println("amor = "+amor);
			this.totalAmortization.setScale(3,BigDecimal.ROUND_HALF_UP);
		//System.out.println("amor = "+amor);
		//return amor;
		}catch(Exception e){
			/**********************/
			System.out.println("Exception = "+ e);
			return new BigDecimal("0.0");
		}
		return null;
	}
	
	
	@JsonIgnore
	public ProductDetail getProductDetail() {
		//ProductDetail(String inventoryNumber, boolean isDiscarded, ECondition condition,//boolean isAvailable,
		//DeliveryDetail deliveryDetail) {
		ProductDetail pd = new ProductDetail(inventoryNumber, isDiscarded, econdition, new DeliveryDetail(deliveryDetailId));
		pd.setId(id);
		pd.setTotalAmortizationPercent(totalAmortizationPercent);
		return pd;
	}

	@Override
	public String toString() {
		return "ProductDetailDAO [id=" + id + ", inventoryNumber=" + inventoryNumber + ", isDiscarded=" + isDiscarded
				+ ", econdition=" + econdition + ", productName=" + productName + ", productType=" + productType
				+ ", amortizationPercent=" + amortizationPercent + ", dateCreated=" + dateCreated + ", deliveryNumber="
				+ deliveryNumber + ", deliveryDetailId=" + deliveryDetailId + ", price=" + price
				+ ", totalAmortizationPercent=" + totalAmortizationPercent + "]";
	}


	

}
