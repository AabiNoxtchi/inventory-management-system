package com.inventory.inventory.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.inventory.Annotations.DropDownAnnotation;
import com.inventory.inventory.Exception.DuplicateNumbersException;
import com.inventory.inventory.Exception.NoChildrensFoundException;
import com.inventory.inventory.Exception.NoParentFoundException;
import com.inventory.inventory.Model.BaseEntity;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Repository.RepositoryImpl;
import com.inventory.inventory.Repository.Interfaces.BaseRepository;
import com.inventory.inventory.ViewModels.Shared.BaseEditVM;
import com.inventory.inventory.ViewModels.Shared.BaseFilterVM;
import com.inventory.inventory.ViewModels.Shared.BaseIndexVM;
import com.inventory.inventory.ViewModels.Shared.BaseOrderBy;
import com.inventory.inventory.ViewModels.Shared.PagerVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.inventory.inventory.auth.Models.UserDetailsImpl;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;

import com.inventory.inventory.ViewModels.Product.FilterVM;

public abstract class BaseService<E extends BaseEntity, F extends BaseFilterVM, 
						O extends BaseOrderBy, IndexVM extends BaseIndexVM<E, F, O>, EditVM extends BaseEditVM<E>> {

	private static final Logger logger = LoggerFactory.getLogger("Base Service");

	@Autowired
	private RepositoryImpl repositoryImpl;

	private UserDetailsImpl LoggedUser;

	private void setValue(Field f, F filter, List<SelectItem> items) {
		try {							
			f.set(filter, items);
		} catch (IllegalArgumentException e) {
			logger.info(" error catched in here");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			logger.info(" error catched in here 2 ");
			e.printStackTrace();
		}
		
	}
	
	private void getPageAndSetModel(IndexVM model, Predicate predicate, Sort sort) {
		Page<E> page =  repo().findAll(predicate, model.getPager().getPageRequest(sort));//;
				model.setItems(page.getContent());
				model.getPager().setPagesCount(page.getTotalPages());
				model.getPager().setItemsCount(page.getTotalElements());		
	}

	private String getProperty(String propertyName) {
		
		int index = propertyName.indexOf(".");
		String property = propertyName.substring(index + 1, propertyName.length());
		return property;
	}

	private String getTableName(String propertyName) {
		
		int index = propertyName.indexOf(".");
		String table = propertyName.substring(0, index);
		return table;
	}

	protected abstract BaseRepository<E> repo();
	protected abstract E newItem();
	protected abstract F filter();
	protected abstract EditVM editVM();
	protected abstract O orderBy();
	
	protected void dealWithEnumDropDowns(IndexVM model) {}
	//protected void handleChildren(EditVM model, E item) throws DuplicateNumbersException {}
	protected void handleAfterSave(EditVM model, E item) throws DuplicateNumbersException, NoChildrensFoundException, NoParentFoundException {}
	protected ResponseEntity<?> saveResponse(EditVM model, E item) {
		return ResponseEntity.ok(item.getId());
	}
	protected ResponseEntity<?> errorsResponse(EditVM model) {
		return ResponseEntity		
			.badRequest()
			.body("error");}
	//protected ResponseEntity<?> saveResponse(EditVM model, E item) { return ResponseEntity.ok(item); }	
	protected boolean setModel(IndexVM model, Predicate predicate, Sort sort) { return false; }
	
	protected abstract void populateModel(IndexVM model) ;
	protected abstract void populateEditGetModel(EditVM model);
	protected abstract void populateEditPostModel(@Valid EditVM model) throws DuplicateNumbersException, NoParentFoundException, Exception;
	
	protected void handleDeletingChilds(List<E> items) {};
	protected void handleDeletingChilds(E e) throws Exception{};	

	protected UserDetailsImpl getLoggedUser() {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		LoggedUser = (UserDetailsImpl) auth.getPrincipal();
		return LoggedUser;
	}

	protected ERole checkRole() {

		String currentUserRole = getLoggedUser().getAuthorities().stream().map(u -> u.getAuthority())
				.collect(Collectors.toList()).get(0);
		ERole eRole = ERole.valueOf(currentUserRole);
		return eRole;
	}
	
	protected void fillSpinners(F filter) {

		filter.setDropDownFilters();
		
		for (Field f : filter.getClass().getDeclaredFields()) {
			
			Predicate predicate = filter.getDropDownPredicate(f.getName());
			
			if (predicate != null) {
				
				Annotation[] annotations = f.getDeclaredAnnotations();
				for (Annotation annotation : annotations) {
					if (annotation instanceof DropDownAnnotation) {
						
						Class<?> entityClass = ((Class<?>) ((ParameterizedType) getClass().getGenericSuperclass())
								.getActualTypeArguments()[0]);
						String propertyName = ((DropDownAnnotation) annotation).name();
						String tableName = null;
						if (propertyName.contains(".")) {
							tableName = getTableName(propertyName);
							propertyName = getProperty(propertyName);
						} else {
							tableName = entityClass.getSimpleName().toLowerCase();
						}
						
						String propertyValue = ((DropDownAnnotation) annotation).value();
						if (propertyValue.contains(".")) {
							propertyValue = getProperty(propertyValue);
						}
						
						List<SelectItem> items = getListItems(predicate , entityClass, propertyName, propertyValue, tableName );
						f.setAccessible(true);
						setValue(f,filter,items);	
						
					}
				}
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected List<SelectItem> getListItems(Predicate predicate, 
			Class<?> entityClass, String propertyName, 
			String propertyValue,String tableName) {
		PathBuilder<String> entityNamePath = new PathBuilder(entityClass, propertyName);
		PathBuilder<?> entityValuePath = new PathBuilder(entityClass, propertyValue);	
		System.out.println("entityClass = "+entityClass);
		System.out.println("entityValuePath = "+entityValuePath+" entityNamePath = "+entityNamePath+" tableName = "+tableName);
		List<SelectItem> items = repositoryImpl.selectItems(predicate, entityValuePath,
				entityNamePath, tableName);
		items.add(0, new SelectItem("", ""));
		return items;
	}
	
	public abstract Boolean checkGetAuthorization();
	public abstract Boolean checkSaveAuthorization();
	public abstract Boolean checkDeleteAuthorization();
	
	public ResponseEntity<IndexVM> getAll(IndexVM model) {

		if (model.getPager() == null) model.setPager(new PagerVM());		
		model.getPager().setPrefix("Pager");		
		if (model.getPager().getPage() < 0)	model.getPager().setPage(0);
		if (model.getPager().getItemsPerPage() <= 0) model.getPager().setItemsPerPage(10);
		
		if (model.getFilter() == null) { model.setFilter(filter()); }
		model.getFilter().setPrefix("Filter");

		populateModel(model);
		Boolean filtersSet = model.getFilter().getFiltersSet();
		if(filtersSet == null || !filtersSet) {
			fillSpinners(model.getFilter());
			dealWithEnumDropDowns(model);
		}
		
		Predicate predicate = model.getFilter().getPredicate();
		if (model.getOrderBy() == null)	model.setOrderBy(orderBy());
		Sort sort = model.getOrderBy().getSort();

		if(!setModel(model, predicate, sort)) 
			getPageAndSetModel(model, predicate, sort);
		
		return ResponseEntity.ok(model);
	}

	public ResponseEntity<EditVM> get(Long id) {

		EditVM model = editVM();
		E item = null;
		if(id > 0) {
			Optional<E> opt = repo().findById(id);
			if(opt.isPresent())item = opt.get();
			model.populateModel(item);
		}
		populateEditGetModel(model);		
		return ResponseEntity.ok(model);

	}	
	
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = DuplicateNumbersException.class)
	public ResponseEntity<?> save(EditVM model) throws Exception{

        E item = newItem();
        
        populateEditPostModel(model);        
        model.populateEntity(item);
        item = repo().save(item);
        handleAfterSave(model, item); //       
        return saveResponse(model, item);//
	}

	@Transactional
	public ResponseEntity<?> delete(List<Long> ids) {
		
		List<E> items = repo().findAllById(ids);
		handleDeletingChilds(items);		
		repo().deleteAll(items);
		/************ in need of event to check parents children count ??????????????   **************////////////////
		return ResponseEntity.ok(ids);

	}

	@Transactional
	public ResponseEntity<?> delete(Long id) throws Exception {
		
		Optional<E> existingItem = repo().findById(id);
		if (!existingItem.isPresent())
			return ResponseEntity.badRequest().body("No record with that ID");
		handleDeletingChilds(existingItem.get());
		repo().deleteById(id);
		/************ in need of event to check parents children count ??????????????   **************////////////////
		return ResponseEntity.ok(id);
		

	}

	
}
