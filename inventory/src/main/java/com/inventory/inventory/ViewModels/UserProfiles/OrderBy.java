package com.inventory.inventory.ViewModels.UserProfiles;

import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.QUserProfile;
import com.inventory.inventory.ViewModels.Shared.BaseOrderBy;
import com.inventory.inventory.ViewModels.Shared.EDirection;
import com.querydsl.core.types.OrderSpecifier;



public class OrderBy extends BaseOrderBy{
	
	
	
	 private EDirection userName;
	 private EDirection inventoryNumber;
	 private EDirection productName;
	 private EDirection givenAt;
	 private EDirection returnedAt;

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
		QUserProfile profile = QUserProfile.userProfile;
		
		OrderSpecifier<?> name = userName != null ? userName.equals(EDirection.asc) ? profile.user.userName.asc() : 
			profile.user.userName.desc() : null;
		OrderSpecifier<?> product = productName != null ? productName.equals(EDirection.asc) ?  profile.productDetail.deliveryDetail.productName.asc() :
			profile.givenAt.desc() : null;		
			OrderSpecifier<?> inventory = inventoryNumber != null ? inventoryNumber.equals(EDirection.asc) ?  profile.productDetail.inventoryNumber.asc() :
				profile.productDetail.inventoryNumber.desc() : null;
		OrderSpecifier<?> given = givenAt != null ? givenAt.equals(EDirection.asc) ?  profile.givenAt.asc() :
			profile.givenAt.desc() : null;
		OrderSpecifier<?> returned = returnedAt != null ? returnedAt.equals(EDirection.asc) ? profile.returnedAt.asc().nullsLast() :
			profile.productDetail.deliveryDetail.productName.desc().nullsFirst() : null;
			
		OrderSpecifier<?> orderBy = userName != null ? name : inventoryNumber != null ? inventory : 
			productName != null ? product : givenAt != null ? given : returnedAt != null ? returned : QUserProfile.userProfile.id.asc(); 
		return orderBy;
		//return name.
	}

	public EDirection getUserName() {
		return userName;
	}

	public void setUserName(EDirection userName) {
		this.userName = userName;
	}

	public EDirection getInventoryNumber() {
		return inventoryNumber;
	}

	public void setInventoryNumber(EDirection inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
	}

	public EDirection getProductName() {
		return productName;
	}

	public void setProductName(EDirection productName) {
		this.productName = productName;
	}

	public EDirection getGivenAt() {
		return givenAt;
	}

	public void setGivenAt(EDirection givenAt) {
		this.givenAt = givenAt;
	}

	public EDirection getReturnedAt() {
		return returnedAt;
	}

	public void setReturnedAt(EDirection returnedAt) {
		this.returnedAt = returnedAt;
	}

	
	
	

}
