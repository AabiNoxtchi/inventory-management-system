package com.example.inventoryui.Models.Shared;

import com.example.inventoryui.Utils.Utils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown=true)
public abstract class BaseFilterVM implements Serializable
{
	private String Prefix = "Filter";

	public BaseFilterVM() {}

	@JsonIgnore
	public String getFilterUrl(StringBuilder sb){

		Map<String, Object> parameters = getUrlParameters();
		if(parameters == null) return null;
		return Utils.getUrlFromMap(sb, parameters, Prefix);

	}

	@JsonIgnore
	abstract public Map<String, Object> getUrlParameters();
	@JsonIgnore
	abstract public void setUrlParameters(Map<String, Object> urlParameters);


	public abstract Boolean getAll();
	public abstract void setAll(Boolean all);
	public String getPrefix() {
		return Prefix;
	}
	public void setPrefix(String prefix) {
		Prefix = prefix;
	}
}