package com.inventory.inventory.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.ProductType;

@Repository
public interface ProductsRepository extends JpaRepository<Product,Long>,
                                            QuerydslPredicateExecutor<Product>{
	
	List<Product> findByUserId(Long id);
	List<Product> findByUserIdAndProductType(Long id,ProductType productType);
	List<Product> findByUserIdAndIsDiscarded(Long id,boolean discarded);
	List<Product> findByUserIdAndIsAvailable(long id, boolean available);
	List<Product> findByEmployeeId(Long id);
	List<Product> findByIdIn(List<Long> ids);

}
