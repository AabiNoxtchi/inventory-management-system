package com.inventory.inventory.Repository;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.inventory.inventory.Model.City;
import com.inventory.inventory.Model.QCity;
import com.inventory.inventory.Model.QCountry;
import com.inventory.inventory.ViewModels.City.CityDAO;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class CityRepositoryImpl {
	
	@PersistenceContext
	EntityManager entityManager;
	
	public List<CityDAO> getDAOs(Predicate predicate, Long offset, Long limit){
		
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);	    
	    QCity city = QCity.city;
	    
		List<CityDAO> countryDAOs = 
				 queryFactory.select( city)	
				.from(city)	
				.innerJoin(city.country)
				.distinct()
				.where(predicate)
				.orderBy(city.country.name.asc())
				.offset(offset).limit(limit)		
				.fetch()
				.stream().map(c -> new CityDAO(c, c.getCountry())).collect(Collectors.toList());
		
		return countryDAOs;
	}
	
	public Long DAOCount(Predicate predicate) {
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);	    
		QCity city = QCity.city;
		JPAQuery<City> query = 				
				 queryFactory.select( city)	
				 .from(city)				 
				 .distinct()
				 .where(predicate);
				
		return query.fetchCount();
	}
	
	public List<SelectItem> getZones(){
		
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);	    
	    QCity city = QCity.city;
	    
		List<SelectItem> zones = 
				 queryFactory.select( city.timeZone)	
				.from(city)					
				.distinct()				
				.orderBy(city.timeZone.asc())						
				.fetch()
				.stream().map(c -> new SelectItem(c, c)).collect(Collectors.toList());
		
		return zones;
	}
	
	public List<SelectItem> getCurrencies(){
		
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);	    
	    QCountry c = QCountry.country;
	    
		List<SelectItem> currencies = 
				 queryFactory.select(c.currency)	
				.from(c)					
				.distinct()				
				.orderBy(c.currency.asc())						
				.fetch()
				.stream().map(i -> new SelectItem(i, i)).collect(Collectors.toList());
		
		
		
		return currencies;
	}


	

}
