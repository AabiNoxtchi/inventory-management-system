package com.inventory.inventory.ViewModels.Shared;

import java.util.List;

import com.inventory.inventory.Model.BaseEntity;

public class BaseIndexVM<E extends BaseEntity, F extends BaseFilterVM ,O extends BaseOrderBy>
{
	private PagerVM Pager ;
	private F Filter ;
	private O OrderBy ;
	
	private List<E> Items ;

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
