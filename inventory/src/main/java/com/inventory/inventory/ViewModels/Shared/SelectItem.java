package com.inventory.inventory.ViewModels.Shared;

public class SelectItem {

	    private String value;
	    private String name;

	    public SelectItem() {}
	    public SelectItem(String value, String name) {
			super();
			this.value = value;
			this.name = name;
		}
	    
	    public SelectItem(Object value, Object name) {
			super();
			this.value = value.toString();
			this.name = name.toString();
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
	    public String toString(){

	        return this.name;
	    }
}
