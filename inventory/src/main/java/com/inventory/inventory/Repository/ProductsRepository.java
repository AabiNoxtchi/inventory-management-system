package com.inventory.inventory.Repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.inventory.inventory.Model.Product;

@Repository
public interface ProductsRepository extends BaseRepository<Product>{
	
	
	  List<Product> findByUserId(Long id); List<Product> findByIdIn(List<Long> ids);	  
	  List<Product> findByEmployeeId(Long id); List<Product>
	  findByUserIdAndIsDiscarded(Long id,boolean discarded);
	 

}
