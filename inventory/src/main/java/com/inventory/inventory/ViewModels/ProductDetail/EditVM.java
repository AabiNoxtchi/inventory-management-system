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

	@EnumAnnotation(target="econdition",title="select condition")
	private List<SelectItem> econditions;
	private ECondition econdition;
	
	private Long deliveryDetailId;
	
	private SelectItem selectItem;// to recieve updated numbers

	@Override
	public void populateModel(ProductDetail item) {
		
		setId(item.getId());
		inventoryNumber = item.getInventoryNumber();
		econdition= item.getEcondition();
		isDiscarded=item.isDiscarded();		
	}

	@Override
	public void populateEntity(ProductDetail item) {
		
		item.setId(getId());
		item.setEcondition(econdition);
		item.setInventoryNumber(inventoryNumber);
		item.setDiscarded(isDiscarded);		
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
