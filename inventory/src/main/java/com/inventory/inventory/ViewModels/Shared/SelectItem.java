package com.inventory.inventory.ViewModels.Shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SelectItem {

	    private String value;
	    private String name;
	    private String filterBy;
	    

	    public SelectItem() {}
//	    public SelectItem(String value, String name) {
//			super();
//			this.value = value;
//			this.name = name;
//		}
	    
	    public SelectItem(Object value, Object name) {
			super();
			this.value = value.toString();
			this.name = name.toString();
		}
	    
	    

		public SelectItem(Object value, Object name, Object filterBy) {
		super();
		this.value = value.toString();
		this.name = name.toString();
		this.filterBy = filterBy.toString();
		}
		public String getValue() {
	        return value;
	    }

	    public void setValue(String value) {
	        this.value = value;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

		@Override
		public String toString() {
			return "SelectItem [value=" + value + ", name=" + name + "]";
		}

		public String getFilterBy() {
			return filterBy;
		}

		public void setFilterBy(String filterBy) {
			this.filterBy = filterBy;
		}

	   
}
