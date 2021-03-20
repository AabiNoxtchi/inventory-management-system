package com.inventory.inventory.ViewModels.Delivery;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Annotations.DropDownAnnotation;
import com.inventory.inventory.Model.QDelivery;
import com.inventory.inventory.Model.QDeliveryDetail;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.QSupplier;
import com.inventory.inventory.ViewModels.Shared.BaseFilterVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;

//import static com.querydsl.sql.SQLExpressions.select;
//import static com.inventory.inventory.ViewModels.Delivery.SQLExpressions.select;

public class FilterVM extends BaseFilterVM{

	private Boolean all;
	@DropDownAnnotation(target="number",value="number",name="number",title="select number")
	private List<SelectItem> numbers;
	private Long number; 
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
//	private Date dateCreatedBefore;
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")//
//	private Date dateCreatedAfter;
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate dateCreatedBefore;
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")//
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate dateCreatedAfter;
	@DropDownAnnotation(target="supplierId",value="supplier.id",name="supplier.name",title="select supplier")
	private List<SelectItem> suppliers;
	private Long supplierId;
	@DropDownAnnotation(target="productId",value="product.id",name="product.name",title="select product")
	private List<SelectItem> products;
	private Long productId;
	private Double totalBillMoreThan;
	private Double totalBillLessThan;
	
	@JsonIgnore
	private Long userId;
	
	@Override
	public  Predicate getPredicate() {
		
		Predicate p = ((BooleanExpression) mainPredicate())
				.and(
						(dateCreatedBefore == null ? Expressions.asBoolean(true).isTrue() 
				  		: QDelivery.delivery.date.before(dateCreatedBefore)) 
					.and(dateCreatedAfter == null ? Expressions.asBoolean(true).isTrue() 
					  		: QDelivery.delivery.date.after(dateCreatedAfter))
					.and(number == null ? Expressions.asBoolean(true).isTrue()
							: QDelivery.delivery.number.eq(number))
					.and(supplierId == null ? Expressions.asBoolean(true).isTrue()
							: QDelivery.delivery.supplier.id.eq(supplierId))
					.and(totalBillMoreThan == null ? Expressions.asBoolean(true).isTrue() :
						  QDelivery.delivery.total.isNotNull().and(QDelivery.delivery.total.gt(totalBillMoreThan)))
				    .and(totalBillLessThan == null ? Expressions.asBoolean(true).isTrue() :
						 QDelivery.delivery.total.isNotNull().and(QDelivery.delivery.total.lt(totalBillLessThan)))
				    .and(productId == null ? Expressions.asBoolean(true).isTrue()
				    		: QDelivery.delivery.id.in(JPAExpressions
				    				.selectFrom(QDeliveryDetail.deliveryDetail)
				    				.where(QDeliveryDetail.deliveryDetail.product.id.eq(productId))
				    				.distinct()
				    				.select(QDeliveryDetail.deliveryDetail.delivery.id)))
				 );
				
		System.out.println("p = "+p);
		return p;
	}
	
	@JsonIgnore
	public Predicate mainPredicate() {
		BooleanExpression exp = QDelivery.delivery.id.in(JPAExpressions
			    .selectFrom(QDeliveryDetail.deliveryDetail)
			    .where(QDeliveryDetail.deliveryDetail.product.userCategory.userId.eq(userId))			    
			    .select(QDeliveryDetail.deliveryDetail.delivery.id));
		return exp;
	}
	
	@Override
	public void setDropDownFilters() {
		Predicate main = mainPredicate();
			
	  	Predicate suppliers = QSupplier.supplier.user.id.eq(userId);
	  	Predicate products = QProduct.product.userCategory.userId.eq(userId);
			
		dropDownFilters = new HashMap<String, Predicate>() {{		
		  put("numbers", main);
		  put("suppliers", suppliers);
		  put("products", products);
	  
 }};	
		
	}

	@Override
	public Boolean getAll() {
		// TODO Auto-generated method stub
		return all;
	}

	@Override
	public void setAll(Boolean all) {
		this.all = all;
		
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<SelectItem> getNumbers() {
		return numbers;
	}

	public void setNumbers(List<SelectItem> numbers) {
		this.numbers = numbers;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

//	public Date getDateCreatedBefore() {
//		return dateCreatedBefore;
//	}
//
//	public void setDateCreatedBefore(Date dateCreatedBefore) {
//		this.dateCreatedBefore = dateCreatedBefore;
//	}
//
//	public Date getDateCreatedAfter() {
//		return dateCreatedAfter;
//	}
//
//	public void setDateCreatedAfter(Date dateCreatedAfter) {
//		this.dateCreatedAfter = dateCreatedAfter;
//	}
	

	public List<SelectItem> getSuppliers() {
		return suppliers;
	}

	public LocalDate getDateCreatedBefore() {
		return dateCreatedBefore;
	}

	public void setDateCreatedBefore(LocalDate dateCreatedBefore) {
		this.dateCreatedBefore = dateCreatedBefore;
	}

	public LocalDate getDateCreatedAfter() {
		return dateCreatedAfter;
	}

	public void setDateCreatedAfter(LocalDate dateCreatedAfter) {
		this.dateCreatedAfter = dateCreatedAfter;
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
	
	

	public List<SelectItem> getProducts() {
		return products;
	}

	public void setProducts(List<SelectItem> products) {
		this.products = products;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Double getTotalBillMoreThan() {
		return totalBillMoreThan;
	}

	public void setTotalBillMoreThan(Double totalBillMoreThan) {
		this.totalBillMoreThan = totalBillMoreThan;
	}

	public Double getTotalBillLessThan() {
		return totalBillLessThan;
	}

	public void setTotalBillLessThan(Double totalBillLessThan) {
		this.totalBillLessThan = totalBillLessThan;
	}

	

	
}
