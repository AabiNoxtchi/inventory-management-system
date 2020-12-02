package com.inventory.inventory.Repository;

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

import com.inventory.inventory.Model.QEmployee;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.QUser;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class RepositoryImpl {
	
	
	  @PersistenceContext
	  private EntityManager entityManager;
	 
	  private static final Logger logger = LoggerFactory.getLogger("Repository Impl");
	
	@SuppressWarnings({ "serial", "rawtypes" })
	private final Map<String, EntityPathBase> entityPaths = new HashMap<String, EntityPathBase>() {{		
        put("product", QProduct.product);
        put("employee", QEmployee.employee);
        put("user", QUser.user);
    }};
    
    @SuppressWarnings("rawtypes")
	public List<SelectItem> selectItems(
    		Predicate dropDownFiltersPredicate, PathBuilder<?> entityValuePath, PathBuilder<String> entityNamePath, @Nullable String table){
		
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		EntityPathBase entityPath = entityPaths.get(table);
		logger.info(" entity path = "+entityPath);
		List<SelectItem> selectItems = 
				 queryFactory.select( entityValuePath, entityNamePath )		//QProduct.product.name , QProduct.product.name)//				
				 .from(entityPath)
				 .where(dropDownFiltersPredicate)
				 .distinct()
				 .fetch()
				 .stream()
				 .map( i -> //new SelectItem( path.as("employee.id"),  path.as("employee.userName")))
				 new SelectItem //( i.get(QProduct.product.employee.id).toString(),i.get(QProduct.product.employee.userName)))
				 ((i.get(entityValuePath)).toString(), i.get(entityNamePath ) ))
				 //i.get(QProduct.product.name),i.get( QProduct.product.name)) )//
				 .collect(Collectors.toList());
		
		return selectItems;
		
	}
}
