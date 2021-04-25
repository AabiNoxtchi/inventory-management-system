package com.inventory.inventory.ViewModels.DeliveryDetail;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.QDeliveryDetail;
import com.inventory.inventory.ViewModels.Shared.BaseFilterVM;
import com.querydsl.core.types.Predicate;

public class FilterVM extends BaseFilterVM{

	private Boolean all; 
	
	@JsonIgnore
	private Long userId;
	
	@Override
	public Predicate getPredicate() {
		System.out.println("user id in filter = "+userId);
		Predicate p = QDeliveryDetail.deliveryDetail.product.userCategory.user.id.eq(userId);
				return p;		
	}

	@Override
	public void setDropDownFilters() {		
	}

	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}	
	@Override
	public Boolean getAll() {		
		return all;
	}
	@Override
	public void setAll(Boolean all) {
		this.all = all;		
	}

	@Override
	public Predicate getFurtherAuthorizePredicate(Long id, Long userId) {
		// TODO Auto-generated method stub
		return QDeliveryDetail.deliveryDetail.product.userCategory.userId.eq(userId).and(QDeliveryDetail.deliveryDetail.id.eq(id));
	}

	@Override
	public Predicate getListAuthorizationPredicate(List<Long> ids, ERole eRole, Long userId) {
		// TODO Auto-generated method stub
		return QDeliveryDetail.deliveryDetail.product.userCategory.userId.eq(userId).and(QDeliveryDetail.deliveryDetail.id.in(ids));
	}

}
