package com.inventory.inventory.ViewModels.Shared;

import com.inventory.inventory.Model.BaseEntity;

public abstract class BaseEditVM<E extends BaseEntity> {
	
	 private Long id ;

     public BaseEditVM() { }

     public abstract void PopulateModel(E item);
     public abstract void PopulateEntity(E item);

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
     
}
