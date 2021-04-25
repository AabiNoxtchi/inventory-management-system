package com.inventory.inventory.Model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "product",
uniqueConstraints =
 @UniqueConstraint(columnNames={"name", "user_category_id"},name="product-name")	
)
public class Product extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String name;

	private String description;	
	
	@ManyToOne(optional = true)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private UserCategory userCategory;
	
	@Formula("(select user_category_id)")
	private Long userCategoryId;
	
	@OneToMany(mappedBy = "product")
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<DeliveryDetail> deliveryDetails;
	
	@Formula("(select count(dd.id) from "			
			+ "product_detail pd inner join delivery_detail dd on dd.id=pd.delivery_detail_id "
			+ "where dd.product_id = id)")			
	private Long total;
	
	
	// ************** //
	public Product() {}
	
	public Product(Long id) {
		this.setId(id);
	}
	
	public Product(String name) {
		super();
		this.name = name;
	}
	
	public Product(String name, UserCategory userCategory) {
		super();
		this.name = name;		
		this.userCategory = userCategory;		
	}

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

	public void setUserCategory(Long userCategoryId2) {
		this.userCategory=new UserCategory(userCategoryId2);
		
	}
	
}


