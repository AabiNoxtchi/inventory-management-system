package com.inventory.inventory.ViewModels.Shared;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.inventory.inventory.Model.BaseEntity;

@JsonInclude(Include.NON_NULL)
public abstract class BaseEditVM<E extends BaseEntity> implements Serializable{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id ;

     public BaseEditVM() { }

     public abstract void populateModel(E item);
     public abstract void populateEntity(E item);

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
     
}
