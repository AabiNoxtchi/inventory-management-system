package com.inventory.inventory.ViewModels.ProductDetail;

import java.util.List;

import com.inventory.inventory.Model.ProductDetail;

import com.inventory.inventory.ViewModels.Shared.BaseIndexVM;

public class IndexVM extends BaseIndexVM<ProductDetail, FilterVM, OrderBy>{
	
	private boolean LongView = true;	
	private List<ProductDetailDAO> DAOItems ;
	
	public boolean isLongView() {
		return LongView;
	}
	public void setLongView(boolean longView) {
		LongView = longView;
	}
	public List<ProductDetailDAO> getDAOItems() {
		return DAOItems;
	}
	public void setDAOItems(List<ProductDetailDAO> dAOItems) {
		DAOItems = dAOItems;
	}
	
	
}
