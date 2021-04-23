package com.inventory.inventory.Repository;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.inventory.inventory.Model.User.QUser;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.ViewModels.PendingUser.PendingUserDAO;
import com.inventory.inventory.ViewModels.User.UserDAO;
import com.inventory.inventory.auth.Models.QRegisterRequest;
import com.inventory.inventory.auth.Models.RegisterRequest;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class PendingUserRepositoryImpl {
	
	@PersistenceContext
    EntityManager entityManager;
	
	public List<PendingUserDAO> getDAOs(Predicate predicate, Long offset, Long limit){//, @Nullable PagerVM pager){
		
		QRegisterRequest q = QRegisterRequest.registerRequest;
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		//Long id, String firstName, String lastName, String userName, String email,
		List<PendingUserDAO> DAOs = 				
				 queryFactory.select(q.id, q.username, q.email, q.countryId, q.newCity)
						.from(q)	
						
				.distinct()
				.where(predicate)
				.orderBy(q.id.asc())
				.offset(offset).limit(limit)		
				.fetch()
				.stream()
				.map(x -> new PendingUserDAO( x.get(q.id), x.get(q.username), x.get(q.email), x.get(q.newCity), x.get(q.countryId)))
				.collect(Collectors.toList());
		
		return DAOs;
	}
	
	public Long DAOCount(Predicate predicate) {
		QRegisterRequest q = QRegisterRequest.registerRequest;
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		JPAQuery<RegisterRequest> query = 				
				queryFactory.select(q)
						.from(q)						
				.distinct()
				.where(predicate);				
				return query.fetchCount();
	}

}
