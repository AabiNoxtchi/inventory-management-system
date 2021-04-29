package com.example.inventoryui.Models.Shared;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PagerVM implements Serializable
{
    private String Prefix ;
    private Integer Page ;
    private Integer PagesCount ;
    private Integer ItemsPerPage ;
    private Integer ItemsCount ;
    
	public String getPrefix() {
		if( Prefix == null ) return "Pager" ; return Prefix;
	}
	public void setPrefix(String prefix) {
		Prefix = prefix;
	}
	public Integer getPage() {
		return Page;
	}
	public void setPage(int page) {
		Page = page;
	}
	public Integer getPagesCount() {
		return PagesCount;
	}
	public void setPagesCount(int pagesCount) {
		PagesCount = pagesCount;
	}
	public Integer getItemsPerPage() {
		return ItemsPerPage;
	}
	public void setItemsPerPage(int itemsPerPage) {
		ItemsPerPage = itemsPerPage;
	}
	 public Integer getItemsCount() {
			return ItemsCount;
		}

		public void setItemsCount(long l) {
			ItemsCount = (int) l;
		}
	
}
