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
		this.filterBy = filterBy != null ? filterBy.toString() : null;
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
			return "SelectItem [value=" + value + ", name=" + name + "filterby = "+filterBy+"]";
		}

		public String getFilterBy() {
			return filterBy;
		}

		public void setFilterBy(String filterBy) {
			this.filterBy = filterBy;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((filterBy == null) ? 0 : filterBy.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SelectItem other = (SelectItem) obj;
			if (filterBy == null) {
				if (other.filterBy != null)
					return false;
			} else if (!filterBy.equals(other.filterBy))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}

	   
}
