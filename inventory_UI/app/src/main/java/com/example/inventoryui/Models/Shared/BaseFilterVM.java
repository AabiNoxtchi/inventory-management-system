package com.example.inventoryui.Models.Shared;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class BaseFilterVM implements Serializable
{
	final String TAG="MyActivity_BaseFilterVM";
    private String Prefix ;
	private SimpleDateFormat df = new SimpleDateFormat("M/dd/yy");
   //private Map<String,Object> urlParameters;



	public BaseFilterVM() {
	}

	@JsonIgnore
	public String getFilterUrl(StringBuilder sb){

		Map<String, Object> parameters = getUrlParameters();
		if(parameters==null)return null;

		Log.i(TAG,"urlLength = "+parameters.size());
		Log.i(TAG,"url = "+parameters);
		for(Map.Entry<String,Object> entry : parameters.entrySet()){
			sb.append(this.Prefix);
			sb.append(".");
			sb.append(entry.getKey());
			sb.append("=");
			if (entry.getValue() instanceof List) {
				String listToString = entry.getValue().toString();
				listToString = (listToString.substring(1, listToString.length() - 1))
						.replaceAll("\\s", "");//replace white spaces
				sb.append(listToString);
			}else if(entry.getValue() instanceof Date){
				sb.append(df.format(entry.getValue()));
			}
			else sb.append(entry.getValue());
			sb.append("&");

		}
		Log.i(TAG,"url = "+sb.toString());
		return sb.toString();

	}

	public String getPrefix() {
		if(Prefix == null) return"Filter"; 	return Prefix;
	}
	
	public void setPrefix(String prefix) {
		Prefix = prefix;
	}

	/*@JsonIgnore
	public Map<String, Object> getUrlParameters() {
		return urlParameters;
	}

	@JsonIgnore
	public void setUrlParameters(Map<String, Object> urlParameters) {
		this.urlParameters = urlParameters;
	}*/

	abstract public Map<String, Object> getUrlParameters();
	abstract public void setUrlParameters(Map<String, Object> urlParameters);

	abstract public Boolean getAll();

	abstract public void setAll(Boolean all);

}