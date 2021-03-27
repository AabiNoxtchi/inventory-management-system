package com.inventory.inventory.ViewModels.Country;

import java.util.List;

import com.inventory.inventory.Model.Country;
import com.inventory.inventory.ViewModels.Shared.BaseIndexVM;




public class IndexVM extends BaseIndexVM<Country, FilterVM, OrderBy>{
	
	private boolean LongView = true;	
	private List<CountryDAO> DAOItems ;
	
	public boolean isLongView() {
		return LongView;
	}
	public void setLongView(boolean longView) {
		LongView = longView;
	}
	public List<CountryDAO> getDAOItems() {
		return DAOItems;
	}
	public void setDAOItems(List<CountryDAO> dAOItems) {
		DAOItems = dAOItems;
	}
	
	
	
}
