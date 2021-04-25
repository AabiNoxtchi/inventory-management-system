package com.example.inventoryui.Models.Shared;

import com.example.inventoryui.Utils.Utils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Map;

public abstract class BaseFilterVM implements Serializable
{
	final String TAG="MyActivity_BaseFilterVM";
    private String Prefix = "Filter";


	public BaseFilterVM() {
	}

	@JsonIgnore
	public String getFilterUrl(StringBuilder sb){

		Map<String, Object> parameters = getUrlParameters();
		if(parameters == null)return null;
		return Utils.getUrlFromMap(sb, parameters, Prefix);

	}

	public String getPrefix() {
		if(Prefix == null) return"Filter"; 	return Prefix;
	}
	
	public void setPrefix(String prefix) {
		Prefix = prefix;
	}

	abstract public Map<String, Object> getUrlParameters();

	abstract public void setUrlParameters(Map<String, Object> urlParameters);

	abstract public Boolean getAll();

	abstract public void setAll(Boolean all);

}