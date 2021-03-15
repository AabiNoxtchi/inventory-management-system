package com.inventory.inventory.Repository;

import static java.time.temporal.ChronoUnit.MONTHS;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.inventory.inventory.Model.Delivery;
import com.inventory.inventory.Model.ProductDetail;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.QProductDetail;
import com.inventory.inventory.ViewModels.ProductDetail.ProductDetailDAO;
import com.inventory.inventory.ViewModels.ProductDetail.Selectable;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class ProductDetailRepositoryImpl {//extends RepositoryImpl{
	
	@PersistenceContext
    EntityManager entityManager;
    
    //JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
	
	//private QProductDetail pd = QProductDetail.productDetail;
	
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
	
	public List<ProductDetailDAO> getDAOs(Predicate predicate, Long offset, Long limit){//, @Nullable PagerVM pager){
		
		QProductDetail pd = QProductDetail.productDetail;
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		
		List<ProductDetailDAO> DAOs = 				
				 queryFactory.select(pd, pd.deliveryDetail.product.name, pd.deliveryDetail.product.productType,
						 pd.deliveryDetail.product.amortizationPercent,
						 pd.deliveryDetail.delivery.number,
						 pd.deliveryDetail.delivery.date, pd.deliveryDetail.pricePerOne)
						.from(pd)	
				.innerJoin(pd.deliveryDetail)
				.innerJoin(pd.deliveryDetail.delivery)
				.innerJoin(pd.deliveryDetail.product)
				.distinct()
				.where(predicate)
				.orderBy(pd.id.asc())
				.offset(offset).limit(limit)		
				.fetch()
				.stream()
				.map(x -> new ProductDetailDAO( 
						x.get(pd),
						x.get(pd.deliveryDetail.product.name),
						x.get(pd.deliveryDetail.product.productType),
						x.get( pd.deliveryDetail.product.amortizationPercent),
						x.get(pd.deliveryDetail.delivery.date), 
						x.get(pd.deliveryDetail.delivery.number),
						x.get(pd.deliveryDetail.pricePerOne )
						))
				.collect(Collectors.toList());
		
		return DAOs;
	}
	
//	public List<Selectable> getSelectables(){
//		
//		QProductDetail pd = QProductDetail.productDetail;
//		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
//		
//		Predicate p = pd.userProfiles.any().user.id.eq((long) 4).and(pd.userProfiles.any().returnedAt.isNull());
//		
//		List<Tuple> selectables = 				
//				 queryFactory.select(pd, pd.deliveryDetail.product.name)
//						.from(pd)	
//				.innerJoin(pd.deliveryDetail)				
//				.innerJoin(pd.deliveryDetail.product)				
//				.where(p)
//				.orderBy(pd.deliveryDetail.product.id.asc())						
//				.fetch();
//		for(Tuple t : selectables)
//			System.out.println("t1.0 = "+selectables.get(0).toString());
//		System.out.println("t1.1 = "+selectables.get(1).toString());
//				/*.stream()
//				.map(x -> new Selectable( 
//						x.get(pd.deliveryDetail.product.name),
//						x.get(pd.deliveryDetail.product.name),
//						x.get(pd.deliveryDetail.product.productType),
//						x.get( pd.deliveryDetail.product.amortizationPercent),
//						x.get(pd.deliveryDetail.delivery.date), 
//						x.get(pd.deliveryDetail.delivery.number),
//						x.get(pd.deliveryDetail.pricePerOne )				 
//						))
//				.collect(Collectors.toList());*/
//		return null;
//	}
	
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
