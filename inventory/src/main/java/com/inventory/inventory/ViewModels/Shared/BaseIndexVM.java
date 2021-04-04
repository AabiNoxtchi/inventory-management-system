package com.inventory.inventory.ViewModels.Shared;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.inventory.inventory.Model.BaseEntity;
import com.inventory.inventory.ViewModels.User.UserDAO;
import com.inventory.inventory.ViewModels.UserProfiles.UserProfileDAO;

@JsonInclude(Include.NON_NULL)
public class BaseIndexVM<E extends BaseEntity, F extends BaseFilterVM ,O extends BaseOrderBy>
{
	private PagerVM Pager ;
	private F Filter ;
	private O OrderBy ;
	
	private List<E> Items ;
	//private List<DAOIt> DAOItems ;
	
	private boolean LongView = false;	
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
