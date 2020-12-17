package com.inventory.inventory.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.inventory.inventory.Model.QEventProduct;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.QSubCategory;
import com.inventory.inventory.Model.QUser;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class EventProductRepositoryImpl {
	
	 @PersistenceContext
	 private EntityManager entityManager;
	 
	 // private static final Logger logger = LoggerFactory.getLogger("EventUserRepository Impl");
		
	
	 public ConcurrentHashMap<Long, List<Long>> getAllEvents(Predicate predicate){
			
//			JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);		
//			
//			List<Tuple> items =
//					 queryFactory
//					 .select(QUser.user.id, QProduct.product.id)
//					 .from(QEventProduct.eventProduct)
//				     .innerJoin(QEventProduct.eventProduct.product, QProduct.product)
//				     .on(QEventProduct.eventProduct.product.id.eq(QProduct.product.id))
//				   .innerJoin(QProduct.product.subCategory, QSubCategory.subCategory) // .innerJoin(QProduct.product.user , QUser.user)
//				     .on(QEventProduct.eventProduct.user.id.eq(QUser.user.id))
//				     .where(predicate).fetch()	;			     
//			
//			ConcurrentHashMap<Long, List<Long>> eventProducts = new ConcurrentHashMap<>();
//			for(Tuple t : items) {
//				
//				Long userId = t.get(0, Long.class);
//				Long productId = t.get(1, Long.class);
//				
//				List<Long> productIds = eventProducts.containsKey(userId)? eventProducts.get(userId) : new ArrayList<>();
//				productIds.add(productId);
//				eventProducts.put(userId, productIds);
//				
//			}
//			
//			return eventProducts;
		 return null;
			
		}
	 
	// /***************** event handler .  List<Tuple> productsToDiscard() *********************************/
	    public List<Tuple> getProducsToDiscard(
	    		Predicate predicate){
//			
//			JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);		
//			
//			List<Tuple> items =
//					 queryFactory
//					 .select(QProduct.product, QUser.user.id)
//					 .from(QProduct.product)
//				     .innerJoin(QProduct.product.user , QUser.user)
//				     .on(QProduct.product.user.id.eq(QUser.user.id))
//				     .where(predicate).fetch();	 		
//			
//			return items;
	    		
	    		return null;
			
		}
	    
	   

}
