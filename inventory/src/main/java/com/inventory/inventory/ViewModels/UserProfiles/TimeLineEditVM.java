package com.inventory.inventory.ViewModels.UserProfiles;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.inventory.inventory.Model.ProductDetail;
import com.inventory.inventory.Model.UserProfile;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.ViewModels.Shared.SelectItem;

@JsonInclude(Include.NON_NULL)
public class TimeLineEditVM {
	
	private List<UserProfile> items;
	private Long firstId;
	private Long lastId;
	private int count;
	private Long totalCount;
	private SelectItem select;
	private String message;
	
	private LocalDate submitGivenAfter;
	private LocalDate submitReturnedBefore;
	private Long submitProductDetailId;
	
	private String[] givenAtErrors;
	private String[] returnAtErrors;
	private String[] timeErrors;
	
	private List<Long> deletedIds;
	
	public void populateEntities(List<UserProfile> items) {
		items.parallelStream().forEach ( p -> {			
			p.setUser(new User(p.getUserId()));
			p.setProductDetail(new ProductDetail(p.getProductDetailId()));
		});		
	}
	
	public TimeLineEditVM(List<UserProfile> items, Long firstId, Long lastId, int count, Long totalCount) {
		super();
		this.items = items;
		this.firstId = firstId;
		this.lastId = lastId;
		this.count = count;
		this.totalCount = totalCount;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public LocalDate getSubmitGivenAfter() {
		return submitGivenAfter;
	}

	public void setSubmitGivenAfter(LocalDate submitGivenAfter) {
		this.submitGivenAfter = submitGivenAfter;
	}

	public LocalDate getSubmitReturnedBefore() {
		return submitReturnedBefore;
	}

	public void setSubmitReturnedBefore(LocalDate submitReturnedBefore) {
		this.submitReturnedBefore = submitReturnedBefore;
	}

	public Long getSubmitProductDetailId() {
		return submitProductDetailId;
	}

	public void setSubmitProductDetailId(Long submitProductDetailId) {
		this.submitProductDetailId = submitProductDetailId;
	}

	

}
