package com.inventory.inventory.ViewModels.Shared;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PagerVM
{
    private String Prefix ;
    private int Page ; 
    private int PagesCount ; 
    private int ItemsPerPage ;
    private int ItemsCount ;
    
    public Pageable getPageRequest(Sort sort) {    	
    	if( sort == null )
    		return PageRequest.of( Page, ItemsPerPage ) ;    	
    	return PageRequest.of( Page, ItemsPerPage, sort );
    }
    
	public String getPrefix() {
		return Prefix;
	}
	public void setPrefix(String prefix) {
		Prefix = prefix;
	}
	public int getPage() {
		return Page;
	}
	public void setPage(int page) {
		Page = page;
	}
	public int getPagesCount() {
		return PagesCount;
	}
	public void setPagesCount(int pagesCount) {
		PagesCount = pagesCount;
	}
	public int getItemsPerPage() {
		return ItemsPerPage;
	}
	public void setItemsPerPage(int itemsPerPage) {
		ItemsPerPage = itemsPerPage;
	}
	 public int getItemsCount() {
			return ItemsCount;
		}

		public void setItemsCount(long l) {
			ItemsCount = (int) l;
		}
	
}
