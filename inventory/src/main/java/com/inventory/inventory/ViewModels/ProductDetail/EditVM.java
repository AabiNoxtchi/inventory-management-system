package com.inventory.inventory.ViewModels.ProductDetail;

import java.util.List;

import com.inventory.inventory.Annotations.EnumAnnotation;
import com.inventory.inventory.Model.ECondition;
import com.inventory.inventory.Model.ProductDetail;
import com.inventory.inventory.ViewModels.Shared.BaseEditVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;

public class EditVM extends BaseEditVM<ProductDetail>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String inventoryNumber;

	private boolean isDiscarded;

	//private boolean isAvailable;
	@EnumAnnotation(target="econdition",title="select condition")
	private List<SelectItem> econditions;
	private ECondition econdition;
	
	private Long deliveryDetailId;
	
	private SelectItem selectItem;// to recieve updated numbers

	@Override
	public void populateModel(ProductDetail item) {
		setId(item.getId());
//		employee_id = item.getEmployee_id();
//		name = item.getName();
		inventoryNumber = item.getInventoryNumber();
		econdition= item.getEcondition();
//		description = item.getDescription();
//		productType=item.getProductType();
//		yearsToDiscard=item.getYearsToDiscard();
		isDiscarded=item.isDiscarded();
		//isAvailable=item.isAvailable();
		//condition = item.getCondition();
//		dateCreated=item.getDateCreated();
//		amortizationPercent=item.getAmortizationPercent();
//		yearsToMAConvertion=item.getYearsToMAConvertion();
//		
	}

	@Override
	public void populateEntity(ProductDetail item) {
		
		item.setId(getId());
		item.setEcondition(econdition);
//		item.setUser(new User(userId));
//		if (employee_id != null && employee_id > 0)
//			item.setEmployee(new User(employee_id));
//		else if (employee_id == null || employee_id == 0)
//			item.setEmployee(null);
		
		item.setInventoryNumber(inventoryNumber);		
		
		item.setDiscarded(isDiscarded);
		//item.setAvailable(isAvailable);
		//item.setCondition(condition);
		item.setDeliveryDetail(deliveryDetailId);
		
		
	}
	

		public String getInventoryNumber() {
	        return inventoryNumber;
	    }

	    public void setInventoryNumber(String inventoryNumber) {
	        this.inventoryNumber = inventoryNumber;
	    }	   

	    public boolean isDiscarded() {
	        return isDiscarded;
	    }

	    public void setDiscarded(boolean isDiscarded) {
	        this.isDiscarded = isDiscarded;
	    }

//	    public boolean isAvailable() {
//	        return isAvailable;
//	    }
//
//	    public void setAvailable(boolean isAvailable) {
//	        this.isAvailable = isAvailable;
//	    }
	    
	    

		public Long getDeliveryDetailId() {
			return deliveryDetailId;
		}

		

		public void setDeliveryDetailId(Long deliveryDetailId) {
			this.deliveryDetailId = deliveryDetailId;
		}

		public SelectItem getSelectItem() {
			return selectItem;
		}

		public void setSelectItem(SelectItem selectItem) {
			this.selectItem = selectItem;
		}

		public List<SelectItem> getEconditions() {
			return econditions;
		}

		public void setEconditions(List<SelectItem> econditions) {
			this.econditions = econditions;
		}

		public ECondition getEcondition() {
			return econdition;
		}

		public void setEcondition(ECondition econdition) {
			this.econdition = econdition;
		}

		
		
		

	    
}
