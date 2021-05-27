package com.inventory.inventory.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.inventory.inventory.Model.City;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.QCity;
import com.inventory.inventory.Model.User.QEmployee;
import com.inventory.inventory.Model.User.QMOL;
import com.inventory.inventory.Model.User.QUser;
import com.inventory.inventory.Repository.RepositoryImpl;
import com.inventory.inventory.Repository.Interfaces.BaseRepository;
import com.inventory.inventory.Repository.Interfaces.CityRepository;
import com.inventory.inventory.ViewModels.Shared.BaseEditVM;
import com.inventory.inventory.ViewModels.Shared.BaseFilterVM;
import com.inventory.inventory.ViewModels.Shared.BaseIndexVM;
import com.inventory.inventory.ViewModels.Shared.BaseOrderBy;
import com.inventory.inventory.ViewModels.Shared.PagerVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.inventory.inventory.ViewModels.UserProfiles.IndexVM;
import com.inventory.inventory.auth.Models.UserDetailsImpl;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

@SuppressWarnings("hiding")
public abstract class BaseService<E extends BaseEntity, F extends BaseFilterVM, 
						O extends BaseOrderBy, IndexVM extends BaseIndexVM<E, F, O>, EditVM extends BaseEditVM<E>> {

	private static final Logger logger = LoggerFactory.getLogger("Base Service");

	@Autowired
	private RepositoryImpl repositoryImpl;
	
	@Autowired
	CityRepository cityRepo;

	private UserDetailsImpl LoggedUser;	

	protected abstract BaseRepository<E> repo();
	protected abstract E newItem();
	protected abstract F filter();
	protected abstract EditVM editVM();
	protected abstract O orderBy();
	
	protected abstract void populateModel(IndexVM model) ;
	protected abstract void populateEditGetModel(EditVM model);
	protected abstract void populateEditPostModel(@Valid EditVM model) throws DuplicateNumbersException, NoParentFoundException, Exception;
	protected abstract Long setDAOItems(IndexVM model, Predicate predicate, Long offset, Long limit, OrderSpecifier<?> orderSpecifier);
	
	protected void handleDeletingChilds(List<E> items) throws Exception {};	
	protected void dealWithEnumDropDowns(IndexVM model) {}	
	protected void handleAfterSave(EditVM model, E item) throws DuplicateNumbersException, NoChildrensFoundException, NoParentFoundException, Exception {}
	
	protected boolean handleDeletingChilds(E e) throws Exception{return false;}; // return false to delete // true to save	
	protected boolean furtherAuthorize(Long id) { return true;} 
	
	protected ResponseEntity<?> saveResponse(EditVM model, E item) {
		return ResponseEntity.ok(item.getId());
	}
	
	protected ResponseEntity<?> errorsResponse(EditVM model) {
		return ResponseEntity		
			.badRequest()
			.body("error");
	}
	
	protected ERole checkRole() {
		if(getLoggedUser() == null) return null;		
		ERole eRole = getLoggedUser().getErole();
		return eRole;
	}
	
	protected LocalDate getUserCurrentDate() { /***************** from start of day   **********************/
		LocalDate now = LocalDate.now();
		if (checkRole().equals(ERole.ROLE_Admin))return (now.atStartOfDay(ZoneId.of("UTC"))).toLocalDate();		
		String timeZone = getCurrentUserCity().getTimeZone();				 
		ZonedDateTime zonedDateTime = now.atStartOfDay(ZoneId.of(timeZone));// ???
		return now = zonedDateTime.toLocalDate();
	}
	
	protected E getItem(Long id) throws Exception {		
		if(id == null ) throw new Exception("item not found !!!");		
		Optional<E> opt = repo().findOne(filter().getFurtherAuthorizePredicate(id, getLoggedUser().getId()));
		if(opt.isPresent()) { return opt.get();}
		else throw new Exception("item not found !!!");
	}
	
	protected List<E> getItems(List<Long> ids) {
		return (List<E>) repo().findAll(filter().getListAuthorizationPredicate(ids, checkRole(), getLoggedUser().getId()));
	}

	protected List<SelectItem> getProductTypes(){
		List<SelectItem> productTypes = new ArrayList<>();
		SelectItem item = new SelectItem(ProductType.LTA.name(), ProductType.LTA.name());
		SelectItem item2 = new SelectItem(ProductType.STA.name(), ProductType.STA.name());
		productTypes.add(item);		
		productTypes.add(item2);
		
		return productTypes;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected List<SelectItem> getListItems(Predicate predicate, 
			Class<?> entityClass, String propertyName, 
			String propertyValue, String filterBy, String tableName) {
		PathBuilder<String> entityNamePath = new PathBuilder(entityClass, propertyName);
		PathBuilder<?> entityValuePath = new PathBuilder(entityClass, propertyValue);
		PathBuilder<?> entityFilterByPath = new PathBuilder(entityClass, filterBy);		
		List<SelectItem> items = repositoryImpl.selectItems(predicate, entityValuePath, entityFilterByPath,entityNamePath, tableName);
		items.add(0, new SelectItem("", ""));
		return items;
	}
	
	protected List<SelectItem> getListItems(Predicate predicate, 
			Class<?> entityClass, String propertyName, 
			String propertyValue, String tableName) {		
		return getListItems(predicate, 
				entityClass, propertyName, 
				propertyValue, "", tableName);		
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
							if(propertyName.contains(".")) continue;
						} else {
							tableName = entityClass.getSimpleName().toLowerCase();
						}
						
						String propertyValue = ((DropDownAnnotation) annotation).value();
						if (propertyValue.contains(".")) {
							propertyValue = getProperty(propertyValue);
						}
						
						String filterBy = ((DropDownAnnotation) annotation).filterBy();	
						if (filterBy.contains(".")) {
							filterBy = getProperty(filterBy);
						}
						
						List<SelectItem> items = getListItems(predicate , entityClass, propertyName, propertyValue,filterBy, tableName );
						f.setAccessible(true);
						setValue(f,filter,items);						
					}
				}
			}
		}
	}
	
	public abstract Boolean checkGetAuthorization();
	public abstract Boolean checkSaveAuthorization();
	public abstract Boolean checkDeleteAuthorization();
	public Boolean checkGetItemAuthorization() { return checkGetAuthorization(); }
	
	public UserDetailsImpl getLoggedUser() {		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(!(auth.getPrincipal() instanceof  UserDetailsImpl)) return null;
		LoggedUser = (UserDetailsImpl) auth.getPrincipal();
		return LoggedUser;
	}
	
	public City getCurrentUserCity() {
		JPQLQuery<Long> parentId = checkRole() != ERole.ROLE_Mol ? 
				JPAExpressions
			    .selectFrom(QUser.user)
			    .where(QUser.user.id.eq(getLoggedUser().getId()))
			    .select(QUser.user.as(QEmployee.class).mol.id) : null;
		
		Predicate parent = checkRole() == ERole.ROLE_Mol ? 
				QMOL.mOL.id.eq(getLoggedUser().getId()): 
					QMOL.mOL.id.eq(parentId);
		
		JPQLQuery<Long> up = JPAExpressions.selectFrom(QMOL.mOL).where(parent).select(QMOL.mOL.city.id);		
		return cityRepo.findOne(QCity.city.id.eq(up)).get();
	}
	
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
		
		System.out.println(predicate);
		
		if (model.getOrderBy() == null)	model.setOrderBy(orderBy());

		if(!setModel(model, predicate, model.getOrderBy().getSpecifier())) {
			Sort sort = model.getOrderBy().getSort();
			getPageAndSetModel(model, predicate, sort);
		}		
		return ResponseEntity.ok(model);
	}

	public ResponseEntity<EditVM> get(Long id) throws Exception {
		authorizeIdAccess(id);		
		EditVM model = editVM();
		E item = null;
		if(id > 0) {
			item = getItem(id);			
			model.populateModel(item);
		}
		populateEditGetModel(model);	
		return ResponseEntity.ok(model);
	}	

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public ResponseEntity<?> save(EditVM model) throws Exception{

		authorizeIdAccess(model.getId());
		Long id = model.getId();
        E item = id != null && id > 0 ? getItem(id) : newItem();
        populateEditPostModel(model);        
        model.populateEntity(item);
        item = repo().save(item);
        handleAfterSave(model, item);        
        return saveResponse(model, item);
	}

	@Transactional
	public ResponseEntity<?> delete(List<Long> ids) throws Exception {
		List<E> items = getItems(ids);//	
		handleDeletingChilds(items);		
		if(items.size() > 0)
			repo().deleteAll(items);		
		return ResponseEntity.ok(items.size());
	}
	
	@Transactional
	public ResponseEntity<?> delete(Long id) throws Exception {
		authorizeIdAccess(id);		
		E item = getItem(id);		
		if(!handleDeletingChilds(item)) 
			repo().deleteById(id);		
		return ResponseEntity.ok(id);
	}
	
	private void authorizeIdAccess(Long id) throws Exception {
		if(!furtherAuthorize(id)) {throw new Exception("Error: not authorized !!!");}		
	}
	
	private void setValue(Field f, F filter, List<SelectItem> items) {
		try {							
			f.set(filter, items);
		} catch (IllegalArgumentException e) {
			logger.info(" error IllegalArgumentException");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			logger.info(" error IllegalAccessException");
			e.printStackTrace();
		}		
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
	
	private void getPageAndSetModel(IndexVM model, Predicate predicate, Sort sort) {
		Page<E> page =  repo().findAll(predicate, model.getPager().getPageRequest(sort));//;
				model.setItems(page.getContent());
				model.getPager().setPagesCount(page.getTotalPages());
				model.getPager().setItemsCount(page.getTotalElements());		
	}
	
	private boolean setModel(IndexVM model, Predicate predicate, OrderSpecifier<?> orderSpecifier) {		
		if(model.isLongView()) {			
			PagerVM pager =  model.getPager();
			Long limit = (long) pager.getItemsPerPage();
			Long offset = pager.getPage() * limit;
			
			Long totalCount = setDAOItems(model, predicate,offset, limit,  orderSpecifier);
			if(totalCount == null) return false;
			
			pager.setItemsCount(totalCount);
			pager.setPagesCount((int) (totalCount % limit > 0 ? (totalCount/limit) + 1 : totalCount / limit));
			return true;
		}
		else return false;		
	}
	

	
}
