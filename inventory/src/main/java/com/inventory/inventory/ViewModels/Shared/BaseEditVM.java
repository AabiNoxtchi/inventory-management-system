package com.inventory.inventory.ViewModels.Shared;

import java.io.Serializable;

import com.inventory.inventory.Model.BaseEntity;

public abstract class BaseEditVM<E extends BaseEntity> implements Serializable{
	
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
