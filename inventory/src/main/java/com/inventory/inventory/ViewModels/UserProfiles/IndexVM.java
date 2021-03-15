package com.inventory.inventory.ViewModels.UserProfiles;

import java.util.List;

import com.inventory.inventory.Model.UserProfile;
import com.inventory.inventory.ViewModels.Shared.BaseIndexVM;

public class IndexVM extends BaseIndexVM<UserProfile, FilterVM, OrderBy>{

	private boolean LongView = true;	
	private List<UserProfileDAO> DAOItems ;
	public boolean isLongView() {
		return LongView;
	}
	public void setLongView(boolean longView) {
		LongView = longView;
	}
	public List<UserProfileDAO> getDAOItems() {
		return DAOItems;
	}
	public void setDAOItems(List<UserProfileDAO> dAOItems) {
		DAOItems = dAOItems;
	}
	
	
}
