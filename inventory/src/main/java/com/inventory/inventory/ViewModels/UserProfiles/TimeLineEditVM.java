package com.inventory.inventory.ViewModels.UserProfiles;

import java.util.List;

import com.inventory.inventory.Model.ProductDetail;
import com.inventory.inventory.Model.UserProfile;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.ViewModels.Shared.SelectItem;

public class TimeLineEditVM {
	
	private List<UserProfile> items;
	private Long firstId;
	private Long lastId;
	private int count;
	private SelectItem select;
	
	private String[] givenAtErrors;
	private String[] returnAtErrors;
	private String[] timeErrors;
	
	private List<Long> deletedIds;
	
	public void populateEntities(List<UserProfile> items) {
		items.parallelStream().forEach(p->{			
			p.setUser(new User(p.getUserId()));
			p.setProductDetail(new ProductDetail(p.getProductDetailId()));
		});		
	}
	
	public TimeLineEditVM(List<UserProfile> items, Long firstId, Long lastId, int count) {
		super();
		this.items = items;
		this.firstId = firstId;
		this.lastId = lastId;
		this.count = count;
	}
	public List<UserProfile> getItems() {
		return items;
	}
	public void setItems(List<UserProfile> items) {
		this.items = items;
	}
	
	public Long getFirstId() {
		return firstId;
	}
	public void setFirstId(Long firstId) {
		this.firstId = firstId;
	}
	public Long getLastId() {
		return lastId;
	}
	public void setLastId(Long lastId) {
		this.lastId = lastId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public String[] getGivenAtErrors() {
		return givenAtErrors;
	}
	public void setGivenAtErrors(String[] givenAtErrors) {
		this.givenAtErrors = givenAtErrors;
	}
	public String[] getReturnAtErrors() {
		return returnAtErrors;
	}
	public void setReturnAtErrors(String[] returnAtErrors) {
		this.returnAtErrors = returnAtErrors;
	}
	public SelectItem getSelect() {
		return select;
	}
	public void setSelect(SelectItem select) {
		this.select = select;
	}

	public String[] getTimeErrors() {
		return timeErrors;
	}

	public void setTimeErrors(String[] timeErrors) {
		this.timeErrors = timeErrors;
	}

	public List<Long> getDeletedIds() {
		return deletedIds;
	}

	public void setDeletedIds(List<Long> deletedIds) {
		this.deletedIds = deletedIds;
	}
	
	

}
