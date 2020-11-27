package com.inventory.inventory.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.inventory.inventory.Annotations.DropDownAnnotation;
import com.inventory.inventory.Model.BaseEntity;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Repository.BaseRepository;
import com.inventory.inventory.Repository.RepositoryImpl;
import com.inventory.inventory.ViewModels.Product.FilterVM;
import com.inventory.inventory.ViewModels.Shared.BaseFilterVM;
import com.inventory.inventory.ViewModels.Shared.BaseIndexVM;
import com.inventory.inventory.ViewModels.Shared.BaseOrderBy;
import com.inventory.inventory.ViewModels.Shared.PagerVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.inventory.inventory.auth.Models.UserDetailsImpl;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;

public abstract class BaseService< E extends BaseEntity , F extends BaseFilterVM,O extends BaseOrderBy,
																IndexVM extends BaseIndexVM<E, F, O>> {
	
	private static final Logger logger = LoggerFactory.getLogger("Base Service");
	
	@Autowired
	private RepositoryImpl repositoryImpl;
	
	private UserDetailsImpl LoggedUser ;
	
	protected abstract BaseRepository<E> repo();
	protected abstract F filter();
	protected abstract O orderBy();	
	//protected abstract <Q extends EntityPathBase<E>> Q getQEntity();
	protected abstract Boolean checkGetAuthorization();
	protected void populateModel(IndexVM model) { }	
	protected void dealWithEnumDropDowns(IndexVM model) {}
	protected UserDetailsImpl getLoggedUser() {
		if (LoggedUser == null)
		{
		  Authentication auth=SecurityContextHolder.getContext().getAuthentication();
		  LoggedUser = (UserDetailsImpl) auth.getPrincipal();
		}
		  return LoggedUser;
	}
	
	public ResponseEntity<IndexVM> getAll(IndexVM model) {
		if(model!=null&&model.getFilter()!=null)
			System.out.println("model.productType = "+((FilterVM)model.getFilter()).getProductType());
			
		 if(model.getPager() == null) model.setPager( new PagerVM() );
		 model.getPager().setPrefix("Pager");
		 if(model.getPager().getPage() < 0 ) model.getPager().setPage(0);
		 
		 if(model.getPager().getItemsPerPage() <= 0 ) model.getPager().setItemsPerPage(10);
		
		/*
		 * if(model.getFilter()!=null) {
		 * System.out.println(((FilterVM)model.getFilter()).getDateCreatedAfter() ==null
		 * ? "null ": ((FilterVM)model.getFilter()).getDateCreatedAfter());
		 * System.out.println(repo().count(QProduct.product.dateCreated.after(((FilterVM
		 * )model.getFilter()).getDateCreatedAfter()))); }
		 */
		// boolean wasNull = false;
		 if(model.getFilter() == null) {
			// wasNull = true;
			 model.setFilter( filter() );
		 }
		 model.getFilter().setPrefix("Filter");
		 
		 populateModel(model);
		// if(wasNull) {
			 fillSpinners(model.getFilter());
			 dealWithEnumDropDowns(model);
		// }
		 Predicate predicate = model.getFilter().getPredicate();
		 if(model.getOrderBy() == null) model.setOrderBy( orderBy());		 
		 Sort sort = model.getOrderBy().getSort();
		 
		 Page<E> page = repo().findAll(predicate, model.getPager().getPageRequest(sort));
		 
		 model.setItems(page.getContent());
		 model.getPager().setPagesCount(page.getTotalPages());
		 model.getPager().setItemsCount(page.getTotalElements());
	     return ResponseEntity.ok(model);
	    }
	
	 
	private String getProperty(String propertyName) {
			int index = propertyName.indexOf(".");
			String property = propertyName.substring(index+1, propertyName.length());
			return property;
		}
		private String getTableName(String propertyName) {
			int index = propertyName.indexOf(".");
			String table = propertyName.substring(0, index);
			return table;
		}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void fillSpinners(F filter) {
		
		filter.setDropDownFilters();
		 for ( Field f : filter.getClass().getDeclaredFields() ) {
			 Predicate predicate=filter.getDropDownPredicate(f.getName());
			 if(predicate!=null) {
	            Annotation[] annotations = f.getDeclaredAnnotations();
	            for (Annotation annotation : annotations) {
	                if (annotation instanceof DropDownAnnotation)
	                {
	                	Class<?> entityClass = ((Class<?>) ((ParameterizedType) getClass()
						        .getGenericSuperclass()).getActualTypeArguments()[0]);
	                	String propertyName = ((DropDownAnnotation) annotation).name();//tostring
	                	String tableName = null;
	                	if(propertyName.contains(".")) {
	                		tableName = getTableName(propertyName);
	                		propertyName = getProperty(propertyName);
	                	}else {	tableName = entityClass.getSimpleName().toLowerCase();}
						
					   PathBuilder<String> entityNamePath = new PathBuilder(entityClass, propertyName);
					   
					   String propertyValue = ((DropDownAnnotation) annotation).value();//.toString();
	                	if(propertyValue.contains(".")) {
	                		propertyValue = getProperty(propertyValue);
	                	}	                	
						
					   PathBuilder<?> entityValuePath = new PathBuilder(entityClass, propertyValue);
	                   f.setAccessible(true);
						  try { 
							  	  List<SelectItem> items = repositoryImpl
							  			  .selectItems(predicate, entityValuePath, entityNamePath  , tableName);
							  	items.add(0, new SelectItem("",""));
							  	  //items.add(0,new SelectItem("",((DropDownAnnotation) annotation).title()));
								  f.set(filter, items);
								
								 // logger.info(" items.size = "+);
							  } catch (IllegalArgumentException e) {
								  logger.info(" error catched in here");
								  e.printStackTrace(); 
							  } catch (IllegalAccessException e) {
								  logger.info(" error catched in here 2 ");
								  e.printStackTrace();
							  }
						 
	                }
	        }
		 }
	   }
	}
	

}
