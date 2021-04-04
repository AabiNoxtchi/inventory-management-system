package com.inventory.inventory.Repository;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.inventory.inventory.Model.ProductDetail;
import com.inventory.inventory.Model.QProductDetail;
import com.inventory.inventory.Model.QUserProfile;
import com.inventory.inventory.Model.UserProfile;
import com.inventory.inventory.ViewModels.ProductDetail.ProductDetailDAO;
import com.inventory.inventory.ViewModels.UserProfiles.UserProfileDAO;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class UserProfileRepositoryImpl {

	@PersistenceContext
    EntityManager entityManager;
	
	
	
	public List<UserProfileDAO> getDAOs(Predicate predicate, Long offset, Long limit, OrderSpecifier<?> sort){//, @Nullable PagerVM pager){
		
		QUserProfile up = QUserProfile.userProfile;
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		
		
		
		List<UserProfileDAO> DAOs = 				
				 queryFactory.select(up, up.profileDetail,  up.user.userName, up.productDetail.inventoryNumber,
						 up.productDetail.deliveryDetail.productId, up.productDetail.deliveryDetail.productName)
						.from(up)
						.leftJoin(up.profileDetail)
						
						 //??????????????????????????????????
				.innerJoin(up.user)
				.innerJoin(up.productDetail)
				.innerJoin(up.productDetail.deliveryDetail)				
				.distinct()
				.where(predicate)
				.orderBy(sort)				
				.offset(offset).limit(limit)		
				.fetch()
				.stream()
				.map(x -> new UserProfileDAO( 
						x.get(up),
						//x.get(up.profileDetail),
						x.get(up.user.userName),
						x.get(up.productDetail.inventoryNumber),
						x.get(up.productDetail.deliveryDetail.productId),
						x.get(up.productDetail.deliveryDetail.productName)						
						))
				.collect(Collectors.toList());
		
		return DAOs;
	}
	
	public Long DAOCount(Predicate predicate) {
		QUserProfile up = QUserProfile.userProfile;
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		JPAQuery<UserProfile> query = 				
				queryFactory.select(up)
						.from(up)
						.leftJoin(up.profileDetail)
						.innerJoin(up.user)
						.innerJoin(up.productDetail)
						.innerJoin(up.productDetail.deliveryDetail)
				.distinct()
				.where(predicate);
				
				return query.fetchCount();
	}
	
	
}
