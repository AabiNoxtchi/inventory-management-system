package com.inventory.inventory.Repository;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.inventory.inventory.Model.User.MOL;
import com.inventory.inventory.Model.User.QMOL;
import com.inventory.inventory.Model.User.QUser;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.ViewModels.User.UserDAO;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class UserRepositoryImpl {
	
	@PersistenceContext
    EntityManager entityManager;
	
	public List<UserDAO> getDAOs(Predicate predicate, Long offset, Long limit){//, @Nullable PagerVM pager){
		
		QUser q = QUser.user;
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		List<UserDAO> DAOs = 				
				 queryFactory.select(q.id,q.firstName, q.lastName, q.userName, q.email, q.deleted)
				.from(q)						
				.distinct()
				.where(predicate)
				.orderBy(q.id.asc())
				.offset(offset).limit(limit)		
				.fetch()
				.stream()
				.map(x -> new UserDAO( x.get(q.id), x.get(q.firstName), x.get(q.lastName), x.get(q.userName), x.get(q.email), x.get(q.deleted)))
				.collect(Collectors.toList());
		
		return DAOs;
	}
	
	public Long DAOCount(Predicate predicate) {
		QUser q = QUser.user;
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		JPAQuery<User> query = 				
				queryFactory.select(q)
				.from(q)						
				.distinct()
				.where(predicate);				
				return query.fetchCount();
	}
	
	public List<UserDAO> getDAOsLong(Predicate predicate, Long offset, Long limit){
		
		QUser u = QUser.user;
		QMOL q = u.as(QMOL.class);
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		List<UserDAO> DAOs = 				
				 queryFactory.select(q.id,q.firstName, q.lastName, q.userName, q.email,
						 q.city.country.name,  q.city.name, q.lastActive)
				.from(q)	
				.innerJoin(q.city)
				.innerJoin(q.city.country)
				.distinct()
				.where(predicate)
				.orderBy(q.id.asc())
				.offset(offset).limit(limit)		
				.fetch()
				.stream()
				.map(x -> new UserDAO( x.get(q.id), x.get(q.firstName), x.get(q.lastName), x.get(q.userName), 
						x.get(q.email), x.get( q.city.country.name), x.get(q.city.name), x.get(q.lastActive)))
				.collect(Collectors.toList());
		
		return DAOs;
	}
	
	public Long DAOCountLong(Predicate predicate) {
		QUser u = QUser.user;
		QMOL q = u.as(QMOL.class);
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		JPAQuery<MOL> query = 				
				queryFactory.select(q)
						.from(q)
						//.innerJoin(q.molUser)
						.innerJoin(q.city)
						.innerJoin(q.city.country)
				.distinct()
				.where(predicate);
		
		return query.fetchCount();
	}
	

}


