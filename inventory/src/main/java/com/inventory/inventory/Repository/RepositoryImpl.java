package com.inventory.inventory.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.inventory.inventory.Model.QCategory;
import com.inventory.inventory.Model.QCity;
import com.inventory.inventory.Model.QCountry;
import com.inventory.inventory.Model.QDelivery;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.QProductDetail;
import com.inventory.inventory.Model.QSupplier;
import com.inventory.inventory.Model.QUserCategory;
import com.inventory.inventory.Model.User.QUser;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class RepositoryImpl {	
	
    @PersistenceContext
	EntityManager entityManager;
   
	@SuppressWarnings({ "serial", "rawtypes" })
	private final Map<String, EntityPathBase> entityPaths = new HashMap<String, EntityPathBase>() {{		
			
			  put("product", QProduct.product);
			  put("productdetail", QProductDetail.productDetail); 
			  put("user", QUser.user);	
			  put("supplier", QSupplier.supplier);			 
			  put("delivery", QDelivery.delivery);
			  put("category", QCategory.category);
			  put("UserCategory", QUserCategory.userCategory);
			  put("country", QCountry.country);
			  put("city", QCity.city);
    }};
    
	public List<SelectItem> selectItems(
    		Predicate dropDownFiltersPredicate, PathBuilder<?> entityValuePath,
    		PathBuilder<String> entityNamePath, @Nullable String table){
    	
    	@SuppressWarnings("rawtypes")
		EntityPathBase entityPath = entityPaths.get(table);
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		
		if(entityNamePath.toString().contains(".")) {
			return null;
		}
		
		List<SelectItem> selectItems = 
		 queryFactory.select( entityValuePath, entityNamePath )				
		 .from(entityPath)
		 .where(dropDownFiltersPredicate)
		 .distinct()
		 .fetch()
		 .stream()
		 .map( i -> 
		 new SelectItem 
		 (i.get(entityValuePath), i.get(entityNamePath ) ))
		 .collect(Collectors.toList());		
		
		return selectItems;
	}

	public List<SelectItem> selectItems(Predicate dropDownFiltersPredicate, PathBuilder<?> entityValuePath,
			PathBuilder<?> entityFilterByPath, PathBuilder<String> entityNamePath, String table) {
		if(entityFilterByPath.toString().length() < 1)
			return selectItems(dropDownFiltersPredicate, entityValuePath, entityNamePath, table);
		
		@SuppressWarnings("rawtypes")
		EntityPathBase entityPath = entityPaths.get(table);
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		
		if(entityNamePath.toString().contains(".")) {
			return null;
		}
		
		List<SelectItem> selectItems = 
		 queryFactory.select( entityValuePath, entityNamePath, entityFilterByPath )				
		 .from(entityPath)
		 .where(dropDownFiltersPredicate)
		 .distinct()
		 .fetch()
		 .stream()
		 .map( i -> 
		 new SelectItem 
		 (i.get(entityValuePath), i.get(entityNamePath ),i.get(entityFilterByPath ) ))
		 .collect(Collectors.toList());		
		
		return selectItems;
	}
    
   
    
    
  
    
}
