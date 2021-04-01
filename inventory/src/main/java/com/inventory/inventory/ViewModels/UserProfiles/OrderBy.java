package com.inventory.inventory.ViewModels.UserProfiles;

import org.springframework.data.domain.Sort;

import com.inventory.inventory.ViewModels.Shared.BaseOrderBy;

public class OrderBy extends BaseOrderBy{

	@Override
	public Sort getSort() {
		Sort sort = Sort.by(
			    Sort.Order.asc("givenAt"),
			   Sort.Order.asc("returnedAt").nullsLast());
		return sort;
	}

}
