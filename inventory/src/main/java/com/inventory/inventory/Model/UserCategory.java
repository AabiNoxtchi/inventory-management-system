package com.inventory.inventory.Model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.User.MOL;

@Entity
@Table(name = "userCategory",
uniqueConstraints=
@UniqueConstraint(columnNames={"category_id", "user_id"})
)
public class UserCategory extends BaseEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional = false)
	private Category category;	
	
	@Column(precision=4, scale=2)
	private double amortizationPercent;	
	
	@ManyToOne(optional = false)
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private MOL user;
	
	@Formula("(select user_id)")
	private Long userId;
	
	public UserCategory() {
		super();
	}

	public UserCategory(Long userCategoryId2) {
		setId(userCategoryId2);
	}
	
	public UserCategory(Category category, MOL user, double amortizationPercent) {
		super();
		this.category = category;
		this.user = user;
		this.amortizationPercent=   Math.round(amortizationPercent*100.0)/100.0;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public MOL getUser() {
		return user;
	}

	public void setUser(MOL user) {
		this.user = user;
	}
	
	public Long getUserId() {
		return userId;
	}

	public double getAmortizationPercent() {
		return amortizationPercent;
	}

	public void setAmortizationPercent(double amortizationPercent) {
		this.amortizationPercent = Math.round(amortizationPercent*100.0)/100.0;
	}	

}


