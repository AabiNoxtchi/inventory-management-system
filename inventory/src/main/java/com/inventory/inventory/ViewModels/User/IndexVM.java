package com.inventory.inventory.ViewModels.User;

import java.util.List;

import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.ViewModels.Shared.BaseIndexVM;
import com.inventory.inventory.ViewModels.User.FilterVM;
import com.inventory.inventory.ViewModels.User.OrderBy;

public class IndexVM extends BaseIndexVM<User, FilterVM, OrderBy>{
	
	private boolean LongView = true;	
	private List<UserDAO> DAOItems ;
	
	public boolean isLongView() {
		return LongView;
	}
	public void setLongView(boolean longView) {
		LongView = longView;
	}
	public List<UserDAO> getDAOItems() {
		return DAOItems;
	}
	public void setDAOItems(List<UserDAO> dAOItems) {
		DAOItems = dAOItems;
	}
	
}