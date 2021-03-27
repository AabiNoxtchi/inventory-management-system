package com.inventory.inventory.Model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.User.MOL;
import com.inventory.inventory.Model.User.User;
import com.querydsl.core.annotations.QueryInit;

@Entity
@Table(name = "product",
uniqueConstraints =
 @UniqueConstraint(columnNames={"name", "user_category_id"},name="name")
	
)
public class Product extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String name;

	private String description;
	
	//@QueryInit("*.*")
	@ManyToOne(optional = true)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private UserCategory userCategory;
	
	//@Formula("(select id from user_category uc where uc.id = user_category_id)")
	@Formula("(select user_category_id)")
	private Long userCategoryId;
	
	@OneToMany(mappedBy = "product")
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<DeliveryDetail> deliveryDetails;
	
	//@Formula("(select product_type from category c inner join user_category uc on c.id = uc.category_id where uc.id = user_category_id)")
	//private ProductType productType;	
	
	//@Formula("(select amortization_percent from user_category uc where uc.id = user_category_id)")
	//private double amortizationPercent;
	
	/*@ManyToOne(optional = false)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private User user;*/
		
		@Formula("(select count(dd.id) from "			
				+ "product_detail pd inner join delivery_detail dd on dd.id=pd.delivery_detail_id "
				+ "where dd.product_id = id)")
				
		private Long total; // total count
	
	
	// ************** //
	public Product() {}
	
	public Product(Long id) {
		this.setId(id);
	}
	
	public Product(String name/*, ProductType productType*/) {
		super();
		this.name = name;
		//this.productType = productType;
	}
	
	public Product(String name, /*ProductType productType, double amortizationPercent,*/ UserCategory userCategory) {
		super();
		this.name = name;
		//this.productType = productType;
		//if(!productType.equals(ProductType.MA)) {
		//this.amortizationPercent = amortizationPercent;
		this.userCategory = userCategory;
		//}
	}

	/*public Product(String name,UserCategory userCategory, ProductType productType, double amortizationPercent, UserCategory userCategory, MOL mol) {
		super();
		this.name = name;
		//this.productType = productType;
		//if(!productType.equals(ProductType.MA)) {
		//this.amortizationPercent = amortizationPercent;
		this.userCategory = userCategory;
		//}
		//this.user = mol;
	}*/

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public UserCategory getUserCategory() {
		return userCategory;
	}
	
	

	public Long getUserCategoryId() {
		return userCategoryId;
	}

//	public void setUserCategoryId(Long userCategoryId) {
//		this.userCategoryId = userCategoryId;
//	}

	public void setUserCategory(UserCategory userCategory) {
		this.userCategory = userCategory;
	}
	public List<DeliveryDetail> getDeliveryDetails() {
		return deliveryDetails;
	}

	public void setDeliveryDetails(List<DeliveryDetail> deliveryDetails) {
		this.deliveryDetails = deliveryDetails;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public void setUserCategory(Long userCategoryId2) {
		this.userCategory=new UserCategory(userCategoryId2);
		
	}

//	public ProductType getProductType() {
//		return productType;
//	}
//
//	public void setProductType(ProductType productType) {
//		this.productType = productType;
//	}

//	public double getAmortizationPercent() {
//		return amortizationPercent;
//	}
//
//	public void setAmortizationPercent(double amortizationPercent) {
//		this.amortizationPercent = amortizationPercent;
//	}
	
	


	/*public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}*/


	/*public SubCategory getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(SubCategory subCategory) {
		this.subCategory = subCategory;
	}

	public double getAmortizationPercent() {
		return amortizationPercent;
	}

	public void setAmortizationPercent(double amortizationPercent) {
		this.amortizationPercent = amortizationPercent;
	}*/
	
	

//	public User getUser() {
//		return user;
//	}

	

//	public void setUser(MOL mol) {
//		this.user = mol;
//	}
	
//	public void setUser(Long id) {
//		//this.user = new MOL(id);
//		this.user = new User(id);
//	}

	
	
}
