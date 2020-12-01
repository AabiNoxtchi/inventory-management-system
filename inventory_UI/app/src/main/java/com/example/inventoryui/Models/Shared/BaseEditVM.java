package com.example.inventoryui.Models.Shared;

public abstract class BaseEditVM<E extends BaseModel> {
	
	 public Long id ;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
     
}
