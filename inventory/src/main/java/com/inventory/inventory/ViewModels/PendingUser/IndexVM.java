package com.inventory.inventory.ViewModels.PendingUser;

import java.util.List;


import com.inventory.inventory.ViewModels.Shared.BaseIndexVM;


import com.inventory.inventory.auth.Models.RegisterRequest;

public class IndexVM extends BaseIndexVM<RegisterRequest, FilterVM, OrderBy>{
	
	private boolean LongView = true;	
	private List<PendingUserDAO> DAOItems ;
	public boolean isLongView() {
		return LongView;
	}
	public void setLongView(boolean longView) {
		LongView = longView;
	}
	public List<PendingUserDAO> getDAOItems() {
		return DAOItems;
	}
	public void setDAOItems(List<PendingUserDAO> dAOItems) {
		DAOItems = dAOItems;
	}
	
}