package com.inventory.inventory.Repository;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.QUserCategory;
import com.inventory.inventory.ViewModels.Product.ProductDAO;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class ProductRepositoryImpl {
	
	@PersistenceContext
    EntityManager entityManager;
	
	public List<ProductDAO> getDAOs(Predicate predicate, Long offset, Long limit){//, @Nullable PagerVM pager){
		
		QProduct p = QProduct.product;
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		
		List<ProductDAO> DAOs = 				
				 queryFactory.select(p, p.userCategory)
						.from(p)	
				.innerJoin(p.userCategory)				
				.distinct()
				.where(predicate)
				.orderBy(p.id.asc())
				.offset(offset).limit(limit)		
				.fetch()
				.stream()
				.map(x -> new ProductDAO( 
						x.get(p),
						x.get(p.userCategory)						
						))
				.collect(Collectors.toList());
		
		return DAOs;
	}

	public Long DAOCount(Predicate predicate) {
	JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);	    
    QProduct p = QProduct.product;
	JPAQuery<Product> query = 				
			 queryFactory.select( p)	
			 .from(p)
			 .innerJoin(p.userCategory )
			 .distinct()
			 .where(predicate);
			
			return query.fetchCount();
	}

	public List<SelectItem> selectCategoryItems(
   		Predicate dropDownFiltersPredicate){
   	
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		
		List<SelectItem> selectItems = 
		queryFactory.select( QUserCategory.userCategory.id, 
				QUserCategory.userCategory.category.name ,QUserCategory.userCategory.category.productType )				
		 .from(QUserCategory.userCategory)
		 .innerJoin(QUserCategory.userCategory.category)
		 .where(dropDownFiltersPredicate)
		 .distinct()
		 .fetch()
		 .stream()
		 .map( i -> 
		 new SelectItem 
		 (i.get( QUserCategory.userCategory.id), i.get( QUserCategory.userCategory.category.name ), 
				 i.get( QUserCategory.userCategory.category.productType) ))
		 .collect(Collectors.toList());		
		
		return selectItems;
		
	}

}
