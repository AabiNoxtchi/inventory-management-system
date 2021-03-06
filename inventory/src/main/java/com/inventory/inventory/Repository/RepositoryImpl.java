package com.inventory.inventory.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.QUser;
import com.inventory.inventory.ViewModels.Product.SelectProduct;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class RepositoryImpl {	
	
    @PersistenceContext
	private EntityManager entityManager;
	 
   // private static final Logger logger = LoggerFactory.getLogger("Repository Impl");
	
	@SuppressWarnings({ "serial", "rawtypes" })
	private final Map<String, EntityPathBase> entityPaths = new HashMap<String, EntityPathBase>() {{		
			
			  put("product", QProduct.product); 
			  put("user", QUser.user);			  
			 
    }};
    
    @SuppressWarnings("rawtypes")
	public List<SelectItem> selectItems(
    		Predicate dropDownFiltersPredicate, PathBuilder<?> entityValuePath, PathBuilder<String> entityNamePath, @Nullable String table){
		
    	
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		EntityPathBase entityPath = entityPaths.get(table);
		//logger.info(" entity path = "+entityPath);
		
//		List<Tuple> items = 
//				 queryFactory.select( entityValuePath, entityNamePath )				
//				 .from(entityPath)
//				 .where(dropDownFiltersPredicate)
//				 .distinct()
//				 .fetch();
//		System.out.println("selectitems = "+items.size());
//		
//		List<SelectItem> selectItems =
//				 items.stream()
//				 .map( i -> 
//				 new SelectItem 
//				// ((i.get(entityValuePath)).toString(), i.get(entityNamePath ) ))
//				 ( i.get(0, Object.class), i.get(1, Object.class) ))
//				 //(i.get)
//				 .collect(Collectors.toList());
//		System.out.println("items = "+selectItems.size());
		
		List<SelectItem> selectItems = 
		 queryFactory.select( entityValuePath, entityNamePath )				
		 .from(entityPath)
		 .where(dropDownFiltersPredicate)
		 .distinct()
		 .fetch()
		 .stream()
		 .map( i -> 
		 new SelectItem 
		 ((i.get(entityValuePath)).toString(), i.get(entityNamePath ) ))
		 .collect(Collectors.toList());
		
		
		return selectItems;
		
	}
    
    public List<SelectProduct> selectProducts(
    		Predicate predicate ){    	
		
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);		
		
		List<SelectProduct> items = 
				queryFactory.select(QProduct.product.name.as("name"), QProduct.product.id.count().as("total"))
				.from(QProduct.product)
				.where(predicate)
				.groupBy(QProduct.product.name)
				.fetch()
				.stream()
				.map(i -> new SelectProduct( i.get(0,String.class), i.get(1,Long.class ))).collect(Collectors.toList());
				
		return items;
		
	}
    
}
