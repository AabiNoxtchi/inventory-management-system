package com.inventory.inventory.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

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
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Repository.BaseRepository;
import com.inventory.inventory.Repository.RepositoryImpl;
import com.inventory.inventory.ViewModels.Shared.BaseEditVM;
import com.inventory.inventory.ViewModels.Shared.BaseFilterVM;
import com.inventory.inventory.ViewModels.Shared.BaseIndexVM;
import com.inventory.inventory.ViewModels.Shared.BaseOrderBy;
import com.inventory.inventory.ViewModels.Shared.PagerVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.inventory.inventory.auth.Models.UserDetailsImpl;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;

public abstract class BaseService<E extends BaseEntity, F extends BaseFilterVM, 
						O extends BaseOrderBy, IndexVM extends BaseIndexVM<E, F, O>, EditVM extends BaseEditVM<E>> {

	private static final Logger logger = LoggerFactory.getLogger("Base Service");

	@Autowired
	private RepositoryImpl repositoryImpl;

	private UserDetailsImpl LoggedUser;

	protected abstract BaseRepository<E> repo();
	protected abstract E newItem();
	protected abstract F filter();
	protected abstract EditVM editVM();
	protected abstract O orderBy();
	
	public abstract Boolean checkGetAuthorization();
	public abstract Boolean checkSaveAuthorization();
	public abstract Boolean checkDeleteAuthorization();

	protected void populateModel(IndexVM model) {}	
	protected void dealWithEnumDropDowns(IndexVM model) {}
	protected abstract void PopulateEditPostModel(@Valid EditVM model);	
	private ResponseEntity<?> saveResponse(E item) { return ResponseEntity.ok(item); }
	
	protected abstract void handleDeletingChilds(List<E> items);
	protected abstract void handleDeletingChilds(E e);	

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
	
	public ResponseEntity<IndexVM> getAll(IndexVM model) {

		if (model.getPager() == null)model.setPager(new PagerVM());		
		model.getPager().setPrefix("Pager");		
		if (model.getPager().getPage() < 0)	model.getPager().setPage(0);
		if (model.getPager().getItemsPerPage() <= 0)model.getPager().setItemsPerPage(10);
		
		if (model.getFilter() == null) {model.setFilter(filter());}
		model.getFilter().setPrefix("Filter");

		populateModel(model);
		fillSpinners(model.getFilter());
		dealWithEnumDropDowns(model);
		
		Predicate predicate = model.getFilter().getPredicate();
		
		if (model.getOrderBy() == null)	model.setOrderBy(orderBy());
		
		Sort sort = model.getOrderBy().getSort();

		Page<E> page = repo().findAll(predicate, model.getPager().getPageRequest(sort));
		model.setItems(page.getContent());
		model.getPager().setPagesCount(page.getTotalPages());
		model.getPager().setItemsCount(page.getTotalElements());
		
		return ResponseEntity.ok(model);
	}

	public ResponseEntity<EditVM> get(Long id) {

		EditVM model = editVM();
		E item = null;
		if(id > 0) {
			Optional<E> opt = repo().findById(id);
			if(opt.isPresent())item = opt.get();
			model.PopulateModel(item);
		}
		return ResponseEntity.ok(model);

	}
	
	public ResponseEntity<?> save(EditVM model){

        E item = newItem();
        
        PopulateEditPostModel(model);        
        model.PopulateEntity(item);
        
        item = repo().save(item);
        return saveResponse(item);
	}
	
	@Transactional
	public ResponseEntity<?> delete(List<Long> ids) {
		
		List<E> items = repo().findAllById(ids);
		handleDeletingChilds(items);
		repo().deleteAll(items);

		return ResponseEntity.ok(ids);

	}

	public ResponseEntity<?> delete(Long id) {
		
		Optional<E> existingItem = repo().findById(id);
		if (!existingItem.isPresent())
			return ResponseEntity.badRequest().body("No record with that ID");
		handleDeletingChilds(existingItem.get());
		repo().deleteById(id);
		
		return ResponseEntity.ok(id);

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

	@SuppressWarnings({ "unchecked", "rawtypes" })
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
						String propertyName = ((DropDownAnnotation) annotation).name();// tostring
						String tableName = null;
						if (propertyName.contains(".")) {
							tableName = getTableName(propertyName);
							propertyName = getProperty(propertyName);
						} else {
							tableName = entityClass.getSimpleName().toLowerCase();
						}

						PathBuilder<String> entityNamePath = new PathBuilder(entityClass, propertyName);

						String propertyValue = ((DropDownAnnotation) annotation).value();// .toString();
						if (propertyValue.contains(".")) {
							propertyValue = getProperty(propertyValue);
						}

						PathBuilder<?> entityValuePath = new PathBuilder(entityClass, propertyValue);
						f.setAccessible(true);
						try {
							List<SelectItem> items = repositoryImpl.selectItems(predicate, entityValuePath,
									entityNamePath, tableName);
							items.add(0, new SelectItem("", ""));
							f.set(filter, items);

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
