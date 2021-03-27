package com.inventory.inventory.ViewModels.City;

import java.util.List;

import com.inventory.inventory.Model.City;
import com.inventory.inventory.ViewModels.Shared.BaseIndexVM;


public class IndexVM extends BaseIndexVM<City, FilterVM, OrderBy>{
	
	private boolean LongView = true;	
	private List<CityDAO> DAOItems ;
	
	public boolean isLongView() {
		return LongView;
	}
	public void setLongView(boolean longView) {
		LongView = longView;
	}
	public List<CityDAO> getDAOItems() {
		return DAOItems;
	}
	public void setDAOItems(List<CityDAO> dAOItems) {
		DAOItems = dAOItems;
	}
	
	
}
