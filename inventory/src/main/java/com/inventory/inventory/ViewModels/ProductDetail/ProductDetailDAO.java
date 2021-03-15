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
import com.inventory.inventory.Model.ProductDetail;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.QProductDetail;
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

	private boolean isAvailable;
	
	//@Formula("(select p.name from product p inner join delivery_detail dd on p.id = dd.product_id where dd.id = delivery_detail_id)")
	private String productName;
	
	//@Formula("(select p.product_type from product p inner join delivery_detail dd on p.id = dd.product_id where dd.id = delivery_detail_id)")
	private ProductType productType;
	
	//@Formula("(select p.amortization_percent from product p inner join delivery_detail dd on p.id = dd.product_id where dd.id = delivery_detail_id)")
	private double amortizationPercent=0;
	
	//@Formula("(select d.date from delivery d inner join delivery_detail dd on d.id = dd.delivery_id where dd.id = delivery_detail_id)")
	private LocalDate dateCreated;
	
	//@Formula("(select d.number from delivery d inner join delivery_detail dd on d.id = dd.delivery_id where dd.id = delivery_detail_id)")
	private Long deliveryNumber;
	
	private Long deliveryDetailId;
	
	//@Formula("(select dd.price_per_one from delivery_detail dd where dd.id = delivery_detail_id)")
	private BigDecimal price ;
	private Double totalAmortization; 
	
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

	

	
	public ProductDetailDAO(ProductDetail pd, String name, ProductType type, Double amortizationPercent,
			LocalDate date, Long number, BigDecimal price) {//, Double totalAmortization) {
		this.id = pd.getId();
		this.inventoryNumber = pd.getInventoryNumber();
		this.isDiscarded = pd.isDiscarded();
		this.isAvailable = pd.isAvailable();
		this.deliveryDetailId = pd.getDeliveryDetailId();
		
		this.productName= name;
		this.productType= type;
		this.amortizationPercent= amortizationPercent;
		this.dateCreated= date;
		this.deliveryNumber=number;
		this.price= price;
		this.totalAmortization = totalAmortizationPercent();
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

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public String getProductName() {
		return productName;
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

	public Double getTotalAmortization() {
		return totalAmortization;
	}

	public void setTotalAmortization(Double totalAmortization) {
		this.totalAmortization = totalAmortization;
	}
	
	
	private Double totalAmortizationPercent() {//ProductType type, Double amortization, LocalDate dateCreated) {
				
		if(productType != ProductType.DMA ) return 0.0;
		
		LocalDate now = LocalDate.now();		 
		ZonedDateTime zonedDateTime = now.atStartOfDay(ZoneId.of("UTC"));// ???
		now = zonedDateTime.toLocalDate();		
		Long months = MONTHS.between(dateCreated, now);
		
		Double total = ( amortizationPercent * ( months/12.0 ));
		return total <= 100 ? total : 100;
				
	}

	@Override
	public String toString() {
		return "ProductDetailDAO [id=" + id + ", inventoryNumber=" + inventoryNumber + ", isDiscarded=" + isDiscarded
				+ ", isAvailable=" + isAvailable + ", productName=" + productName + ", productType=" + productType
				+ ", amortizationPercent=" + amortizationPercent + ", dateCreated=" + dateCreated + ", deliveryNumber="
				+ deliveryNumber + ", deliveryDetailId=" + deliveryDetailId + ", price=" + price
				+ ", totalAmortization=" + totalAmortization + "]";
	}

	

	/*@Override
	public String toString() {
		return "ProductDetailDAO [id=" + id + ", inventoryNumber=" + inventoryNumber + ", isDiscarded=" + isDiscarded
				+ ", isAvailable=" + isAvailable + ", productName=" + productName + ", productType=" + productType
				+ ", amortizationPercent=" + amortizationPercent + ", dateCreated=" + dateCreated + ", deliveryNumber="
				+ deliveryNumber + ", price=" + price + "]";
	}*/
	
	
	

}
