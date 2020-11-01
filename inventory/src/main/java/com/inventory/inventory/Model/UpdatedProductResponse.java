package com.inventory.inventory.Model;

public class UpdatedProductResponse {
	
	    private String response;
	    private Long id;
	    private String productName;
	    private boolean discarded;
	    private boolean convertedToMA;
	    private Long employeeId;

	    public Long getEmployeeId() {
			return employeeId;
		}

		public void setEmployeeId(Long employeeId) {
			this.employeeId = employeeId;
		}

		public String getProductName() {
	        return productName;
	    }

	    public void setProductName(String productName) {
	        this.productName = productName;
	    }

	    public boolean isDiscarded() {
	        return discarded;
	    }

	    public void setDiscarded(boolean discarded) {
	        this.discarded = discarded;
	    }

	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public boolean isConvertedToMA() {
	        return convertedToMA;
	    }

	    public void setConvertedToMA(boolean convertedToMA) {
	        this.convertedToMA = convertedToMA;
	    }

	    public String getResponse() {
	        return response;
	    }

	    public void setResponse(String response) {
	        this.response = response;
	    }

}
