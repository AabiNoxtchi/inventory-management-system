package com.inventory.inventory.ViewModels.Shared;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.inventory.inventory.Model.BaseEntity;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class BaseIndexVM<E extends BaseEntity, F extends BaseFilterVM ,O extends BaseOrderBy>
{
	private PagerVM Pager ;
	private F Filter ;
	private O OrderBy ;
	
	private List<E> Items ;
	
	private boolean LongView = false;
	
	/***** getters and setter *****/
	
	public boolean isLongView() {
		return LongView;
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

	public void setItems(List<E> content) {
		Items=content;
		
	}	

}
