package com.example.inventoryui.Models.Shared;

import java.io.Serializable;

public abstract class BaseFilterVM implements Serializable
{  
   private String Prefix ;

	public BaseFilterVM() {
	}

	public String getPrefix() {
		if(Prefix == null) return"Filter"; 	return Prefix;
	}
	
	public void setPrefix(String prefix) {
		Prefix = prefix;
	}


	abstract public Boolean getAll();

	abstract public void setAll(Boolean all);

}