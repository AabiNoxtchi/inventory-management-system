package com.inventory.inventory.Repository.Interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.inventory.inventory.Model.Category;
import com.inventory.inventory.Model.ProductDetail;

@Repository
public interface ProductDetailsRepository extends BaseRepository<ProductDetail>{
	
	void deleteByIdIn(List<Long> ids);
	
	

}