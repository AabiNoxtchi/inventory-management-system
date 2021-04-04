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
import com.inventory.inventory.Model.City;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.QCity;
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
import com.inventory.inventory.ViewModels.UserProfiles.UserProfileDAO;
import com.inventory.inventory.auth.Models.UserDetailsImpl;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.inventory.inventory.ViewModels.Country.CityEditVM;
import com.inventory.inventory.ViewModels.Product.FilterVM;

public abstract class BaseService<E extends BaseEntity, F extends BaseFilterVM, 
						O extends BaseOrderBy, IndexVM extends BaseIndexVM<E, F, O>, EditVM extends BaseEditVM<E>> {

	private static final Logger logger = LoggerFactory.getLogger("Base Service");

	@Autowired
	private RepositoryImpl repositoryImpl;
	
	@Autowired
	CityRepository cityRepo;
	
	

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
	
	//protected boolean setModel(IndexVM model, Predicate predicate, OrderSpecifier<?> orderSpecifier) { return false; }
	
	private boolean setModel(IndexVM model, Predicate predicate, OrderSpecifier<?> orderSpecifier) {
		
		if(model.isLongView()) {			
			PagerVM pager =  model.getPager();
			Long limit = (long) pager.getItemsPerPage();
			Long offset = pager.getPage() * limit;
			
			Long totalCount = setDAOItems(model, predicate,offset, limit,  orderSpecifier);
			if(totalCount == null) return false;
			//List<UserProfileDAO> DAOs = 
			//repoImpl.getDAOs(predicate, offset, limit, sort);//, pager);
			//model.setDAOItems(DAOs);
			//System.out.println("DAOs size = "+DAOs.size());
			//System.out.println("sort = "+sort);
			
			//Long totalCount = repoImpl.DAOCount(predicate);//.fetchCount();
			pager.setItemsCount(totalCount);
			pager.setPagesCount((int) (totalCount % limit > 0 ? (totalCount/limit) + 1 : totalCount / limit));
			return true;
		}
		else return false;		
	}

	protected abstract Long setDAOItems(IndexVM model, Predicate predicate, Long offset, Long limit, OrderSpecifier<?> orderSpecifier);

	protected List<SelectItem> getProductTypes(){
		List<SelectItem> productTypes = new ArrayList<>();
		SelectItem item = new SelectItem(ProductType.LTA.name(), ProductType.LTA.name());
		SelectItem item2 = new SelectItem(ProductType.STA.name(), ProductType.STA.name());
		productTypes.add(item);		
		productTypes.add(item2);
		
		return productTypes;
	}
	//protected void handleChildren(EditVM model, E item) throws DuplicateNumbersException {}
	protected void handleAfterSave(EditVM model, E item) throws DuplicateNumbersException, NoChildrensFoundException, NoParentFoundException, Exception {}
	protected ResponseEntity<?> saveResponse(EditVM model, E item) {
		return ResponseEntity.ok(item.getId());
	}
	protected ResponseEntity<?> errorsResponse(EditVM model) {
		return ResponseEntity		
			.badRequest()
			.body("error");}
	//protected ResponseEntity<?> saveResponse(EditVM model, E item) { return ResponseEntity.ok(item); }	
	
	
	protected abstract void populateModel(IndexVM model) ;
	protected abstract void populateEditGetModel(EditVM model);
	protected abstract void populateEditPostModel(@Valid EditVM model) throws DuplicateNumbersException, NoParentFoundException, Exception;
	
	protected void handleDeletingChilds(List<E> items) {};
	protected void handleDeletingChilds(E e) throws Exception{};	

	public UserDetailsImpl getLoggedUser() {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		LoggedUser = (UserDetailsImpl) auth.getPrincipal();
		return LoggedUser;
	}

	protected ERole checkRole() {

		/*String currentUserRole = getLoggedUser().getAuthorities().stream().map(u -> u.getAuthority())
				.collect(Collectors.toList()).get(0);
		ERole eRole = ERole.valueOf(currentUserRole);*/
		ERole eRole = getLoggedUser().getErole();
		return eRole;
	}
	
	public City getCurrentUserCity() {
		JPQLQuery<Long> parentId = checkRole() != ERole.ROLE_Mol ? //  updated not checked from ==
				JPAExpressions
			    .selectFrom(QUser.user)
			    .where(QUser.user.id.eq(getLoggedUser().getId()))
			    .select(QUser.user.mol.id) : null;
		
		Predicate parent = checkRole() == ERole.ROLE_Mol ? 
				QMOL.mOL.id.eq(getLoggedUser().getId()): 
					QMOL.mOL.id.eq(parentId);
		
		JPQLQuery<Long> up = JPAExpressions.selectFrom(QMOL.mOL).where(parent).select(QMOL.mOL.city.id);
		
		return cityRepo.findOne(QCity.city.id.eq(up)).get();
	}
	
	protected LocalDate getUserCurrentDate() { /***************** from start of day   **********************/
		LocalDate now = LocalDate.now();
		if (checkRole().equals(ERole.ROLE_Admin))return (now.atStartOfDay(ZoneId.of("UTC"))).toLocalDate();
		
		
		
		String timeZone = getCurrentUserCity().getTimeZone();
				 
		ZonedDateTime zonedDateTime = now.atStartOfDay(ZoneId.of(timeZone));// ???
		System.out.println("zonedDateTime = "+zonedDateTime);
		return now = zonedDateTime.toLocalDate();//now.atStartOfDay(ZoneId.of(timeZone)).toLocalDate();	
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected List<SelectItem> getListItems(Predicate predicate, 
			Class<?> entityClass, String propertyName, 
			String propertyValue, String filterBy, String tableName) {
		PathBuilder<String> entityNamePath = new PathBuilder(entityClass, propertyName);
		PathBuilder<?> entityValuePath = new PathBuilder(entityClass, propertyValue);
		PathBuilder<?> entityFilterByPath = new PathBuilder(entityClass, filterBy);
		//System.out.println("entityClass = "+entityClass);
		//System.out.println("entityValuePath = "+entityValuePath+" entityNamePath = "+entityNamePath+" tableName = "+tableName);
		System.out.println("entityFilterByPath = "+entityFilterByPath);
		List<SelectItem> items = repositoryImpl.selectItems(predicate, entityValuePath, entityFilterByPath,entityNamePath, tableName);
		items.add(0, new SelectItem("", ""));
		return items;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected List<SelectItem> getListItems(Predicate predicate, 
			Class<?> entityClass, String propertyName, 
			String propertyValue, String tableName) {
		
		return getListItems(predicate, 
				entityClass, propertyName, 
				propertyValue, "", tableName);
		/*PathBuilder<String> entityNamePath = new PathBuilder(entityClass, propertyName);
		PathBuilder<?> entityValuePath = new PathBuilder(entityClass, propertyValue);
		PathBuilder<?> entityFilterByPath = new PathBuilder(entityClass, filterBy);
		//System.out.println("entityClass = "+entityClass);
		//System.out.println("entityValuePath = "+entityValuePath+" entityNamePath = "+entityNamePath+" tableName = "+tableName);
		System.out.println("entityFilterByPath = "+entityFilterByPath);
		List<SelectItem> items = repositoryImpl.selectItems(predicate, entityValuePath,
				entityNamePath, tableName);
		items.add(0, new SelectItem("", ""));
		return items;*/
	}
	
	public abstract Boolean checkGetAuthorization();
	public abstract Boolean checkSaveAuthorization();
	public abstract Boolean checkDeleteAuthorization();
	
	public ResponseEntity<IndexVM> getAll(IndexVM model) {
		
		System.out.println("get all base service");
		printmsg(model);

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
		//System.out.println("get all predicate = "+predicate);
		if (model.getOrderBy() == null)	model.setOrderBy(orderBy());
		
		

		if(!setModel(model, predicate, model.getOrderBy().getSpecifier())) {
			Sort sort = model.getOrderBy().getSort();
			System.out.println("sort = "+sort);
			getPageAndSetModel(model, predicate, sort);
		}
		
		return ResponseEntity.ok(model);
	}

	protected void printmsg(IndexVM model) {
		// TODO Auto-generated method stub
		
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
		System.out.println("editvm model.tostring = "+model.toString());
		return ResponseEntity.ok(model);

	}	
	
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public ResponseEntity<?> save(EditVM model) throws Exception{

		System.out.println("in save base");
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
