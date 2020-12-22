package com.inventory.inventory.Model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "sub_category",
uniqueConstraints = { 
		@UniqueConstraint(columnNames = "name")
		})
public class SubCategory extends BaseEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private String name;
	
	@ManyToOne(optional = false)
	@Basic(fetch = FetchType.LAZY)
	private Category category;
	
	public SubCategory() {}
	
	public SubCategory(String name, Category category) {
		super();
		this.name = name;
		this.category = category;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
