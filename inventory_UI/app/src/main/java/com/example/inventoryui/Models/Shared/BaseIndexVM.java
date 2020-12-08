package com.example.inventoryui.Models.Shared;

import android.util.Log;

import com.example.inventoryui.Utils.Utils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

public class BaseIndexVM<E extends BaseModel, F extends BaseFilterVM ,O extends BaseOrderBy> implements Serializable
{
	final String TAG="MyActivity_BaseIndexVM";
	private PagerVM Pager ;
	private F Filter ;
	private O OrderBy ;
	private List<E> Items ;

	@JsonIgnore
	public String getUrl() {

		StringBuilder sb = new StringBuilder();
		if(this.Pager!=null)
			Utils.getUrl( sb, Pager , Pager.getPrefix());
		if( this.OrderBy != null){}
		if(this.Filter != null)
		{ sb.append("&");  this.Filter.getFilterUrl(sb);}
		if(sb.length()>0)
			sb.insert(0,"?");
		Log.i(TAG,"this class = " + this.getClass().getName());
		Log.i(TAG,"url = "+sb.toString());
		return sb.toString();

	}


	public PagerVM getPager() {
		return Pager;
	}

	public void setPager(PagerVM pager) {
		Pager = pager;
	}

	public F getFilter() {
		return Filter;
	}

	public void setFilter(F filter) {
		Filter = filter;
	}

	public O getOrderBy() {
		return OrderBy;
	}

	public void setOrderBy(O orderBy) {
		OrderBy = orderBy;
	}

	public List<E> getItems() {
		return Items;
	}

	public void setItems(List<E> items) {
		Items = items;
	}

}
