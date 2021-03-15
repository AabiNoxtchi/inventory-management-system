package com.inventory.inventory.Repository;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.inventory.inventory.Model.Delivery;
import com.inventory.inventory.Model.QDelivery;
import com.inventory.inventory.ViewModels.Delivery.DeliveryDAO;
import com.inventory.inventory.ViewModels.ProductDetail.ProductDetailDAO;
import com.inventory.inventory.ViewModels.Shared.PagerVM;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class DeliveryRepositoryImpl{
	
	
	@PersistenceContext
	EntityManager entityManager;
	
	public List<DeliveryDAO> getDeliveryDAOs(Predicate predicate, Long offset, Long limit){//, PagerVM pager){
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);	    
	    QDelivery delivery = QDelivery.delivery;
		/*JPAQuery<Delivery> query = 				
		 queryFactory.select( delivery)	
		 .from(delivery)
		 .innerJoin(delivery.deliveryDetails )
		 .distinct()
		 .where(predicate);
		
		Long totalCount = query.fetchCount();
		pager.setItemsCount(totalCount);
		pager.setPagesCount((int) (totalCount % limit > 0 ? (totalCount/limit) + 1 : totalCount / limit));*/
		
		List<DeliveryDAO> deliveryDAOs = 
				 queryFactory.select( delivery)	
				.from(delivery)	
				.innerJoin(delivery.deliveryDetails )
				.distinct()
				.where(predicate)
				.orderBy(delivery.number.asc())
				.offset(offset).limit(limit)		
				.fetch()
				.stream().map(d -> new DeliveryDAO(d, d.getDeliveryDetails())).collect(Collectors.toList());
		
		return deliveryDAOs;
	}
	
	public Long DAOCount(Predicate predicate) {
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);	    
	    QDelivery delivery = QDelivery.delivery;
		JPAQuery<Delivery> query = 				
				 queryFactory.select( delivery)	
				 .from(delivery)
				 .innerJoin(delivery.deliveryDetails )
				 .distinct()
				 .where(predicate);
				
				return query.fetchCount();
	}

	

}
