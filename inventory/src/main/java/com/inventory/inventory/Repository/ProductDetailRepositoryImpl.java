package com.inventory.inventory.Repository;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.inventory.inventory.Model.ProductDetail;
import com.inventory.inventory.Model.QProductDetail;
import com.inventory.inventory.ViewModels.ProductDetail.ProductDetailDAO;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class ProductDetailRepositoryImpl {
	
	@PersistenceContext
    EntityManager entityManager;
	
	public List<SelectItem> getInventoryNumbers(Predicate predicate){		
		QProductDetail pd = QProductDetail.productDetail;
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		
		List<SelectItem> pds =  queryFactory
				.select(pd.id, pd.inventoryNumber)
				.from(pd)
				.where(predicate)
				.fetch()
				.stream()
				.map(i -> new SelectItem(i.get(pd.id),i.get(pd.inventoryNumber)))
				.collect(Collectors.toList());
		
		return pds;
	}	
	
	public List<ProductDetailDAO> getDAOs(Predicate predicate, Long offset, Long limit){
		
		
		QProductDetail pd = QProductDetail.productDetail;
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		
		List<ProductDetailDAO> DAOs = 				
				 queryFactory.select(pd, pd.deliveryDetail.product.name, pd.deliveryDetail.product.userCategory,						
						 pd.deliveryDetail.delivery.number,
						 pd.deliveryDetail.delivery.date, pd.deliveryDetail.pricePerOne)
						.from(pd)	
				.innerJoin(pd.deliveryDetail)
				.innerJoin(pd.deliveryDetail.delivery)
				.innerJoin(pd.deliveryDetail.product)
				.innerJoin(pd.deliveryDetail.product.userCategory)
				.distinct()
				.where(predicate)
				.orderBy(pd.id.asc())
				.offset(offset).limit(limit)		
				.fetch()
				.stream()
				.map(x -> new ProductDetailDAO( 
						x.get(pd),
						x.get(pd.deliveryDetail.product.name),
						x.get(pd.deliveryDetail.product.userCategory),
						x.get(pd.deliveryDetail.delivery.date), 
						x.get(pd.deliveryDetail.delivery.number),
						x.get(pd.deliveryDetail.pricePerOne )
						
						))
				.collect(Collectors.toList());
		
		return DAOs;
	}	
	
	public Long DAOCount(Predicate predicate) {
		QProductDetail pd = QProductDetail.productDetail;
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		JPAQuery<ProductDetail> query = 				
				queryFactory.select(pd)
						.from(pd)	
				.innerJoin(pd.deliveryDetail)
				.innerJoin(pd.deliveryDetail.delivery)
				.innerJoin(pd.deliveryDetail.product)
				.distinct()
				.where(predicate);
				
				return query.fetchCount();
	}
	 
}
