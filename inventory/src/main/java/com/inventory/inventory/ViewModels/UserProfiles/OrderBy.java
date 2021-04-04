package com.inventory.inventory.ViewModels.UserProfiles;

import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.QUserProfile;
import com.inventory.inventory.ViewModels.Shared.BaseOrderBy;
import com.querydsl.core.types.OrderSpecifier;

public class OrderBy extends BaseOrderBy{
	
	 private String userName;
	 private String givenAt;

	@Override
	public Sort getSort() {
		
		
		Sort sort =Sort.by(
			    Sort.Order.asc("givenAt"),
			   Sort.Order.asc("returnedAt").nullsLast());
		if(userName != null)
			sort = Sort.by(Sort.Order.asc("givenAt"));
		
		
		return sort;
	}
	
	@Override
	@JsonIgnore
	public OrderSpecifier<?> getSpecifier(){
		OrderSpecifier<?> name = QUserProfile.userProfile.user.userName.asc();
		OrderSpecifier<?> given = QUserProfile.userProfile.givenAt.asc();
		OrderSpecifier<?> orderBy = userName != null ? name : givenAt != null ? given : QUserProfile.userProfile.id.asc(); 
		return orderBy;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGivenAt() {
		return givenAt;
	}

	public void setGivenAt(String givenAt) {
		this.givenAt = givenAt;
	}
	
	
	
	

}
