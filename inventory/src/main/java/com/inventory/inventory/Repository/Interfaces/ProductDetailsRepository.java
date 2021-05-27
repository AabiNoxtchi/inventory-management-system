package com.inventory.inventory.Repository.Interfaces;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.inventory.inventory.Model.ProductDetail;

@Repository
public interface ProductDetailsRepository extends BaseRepository<ProductDetail>{
	
	void deleteByIdIn(List<Long> ids);	

}
