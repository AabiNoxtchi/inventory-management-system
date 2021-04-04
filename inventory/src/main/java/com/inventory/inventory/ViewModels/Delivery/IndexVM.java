package com.inventory.inventory.ViewModels.Delivery;

import java.util.List;

import com.inventory.inventory.Model.Delivery;
import com.inventory.inventory.Model.DeliveryDetail;
import com.inventory.inventory.ViewModels.Shared.BaseIndexVM;
import com.inventory.inventory.ViewModels.UserProfiles.UserProfileDAO;


public class IndexVM extends BaseIndexVM<Delivery, FilterVM, OrderBy>{
	
	private EDeliveryView deliveryView = EDeliveryView.DeliveryView;
	
	private boolean LongView;
	
	public boolean isLongView() {
		return deliveryView.equals(EDeliveryView.DeliveryDetailView);
	}
	public void setLongView(boolean longView) {
		LongView = longView;
	}
	
	private List<DeliveryDAO> DAOItems ;
	

	public List<DeliveryDAO> getDAOItems() {
		return DAOItems;
	}

	public void setDAOItems(List<DeliveryDAO> dAOItems) {
		DAOItems = dAOItems;
	}

	public EDeliveryView getDeliveryView() {
		return deliveryView;
	}

	public void setDeliveryView(EDeliveryView deliveryView) {
		this.deliveryView = deliveryView;
	}
	
	

}
