package com.inventory.inventory.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "category",
		uniqueConstraints = { 
		@UniqueConstraint(columnNames = "number")
		})
public class Category extends BaseEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int number;// category 1,2,.... 7	
	private String name;
	private double amortizationPercent;
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
	@OneToMany(mappedBy = "category")
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<SubCategory> subCategories;
	
	public Category() {}
	
	public Category(int number, String name, double percent) {
		this.number = number;
		this.name = name;
		this.amortizationPercent = percent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getAmortizationPercent() {
		return amortizationPercent;
	}
	public void setAmortizationPercent(double amortizationPercent) {
		this.amortizationPercent = amortizationPercent;
	}
	public List<SubCategory> getSubCategories() {
		if(subCategories == null)
			subCategories = new ArrayList<>();
		return subCategories;
	}
	public void setSubCategories(List<SubCategory> subCategories) {
		this.subCategories = subCategories;
	}

}
