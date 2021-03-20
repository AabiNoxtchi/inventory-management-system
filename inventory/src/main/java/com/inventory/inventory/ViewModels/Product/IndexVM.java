package com.inventory.inventory.ViewModels.Product;


import java.util.List;

import com.inventory.inventory.Model.Product;
import com.inventory.inventory.ViewModels.Shared.BaseIndexVM;

public class IndexVM extends BaseIndexVM<Product, FilterVM, OrderBy>{
	
	private boolean LongView = true;	
	private List<ProductDAO> DAOItems ;
	public boolean isLongView() {
		return LongView;
	}
	public void setLongView(boolean longView) {
		LongView = longView;
	}
	public List<ProductDAO> getDAOItems() {
		return DAOItems;
	}
	public void setDAOItems(List<ProductDAO> dAOItems) {
		DAOItems = dAOItems;
	}
	
	
	
	
}
