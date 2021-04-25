package com.inventory.inventory.Service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inventory.inventory.Exception.DuplicateNumbersException;
import com.inventory.inventory.Exception.NoParentFoundException;
import com.inventory.inventory.Model.City;
import com.inventory.inventory.Model.Country;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.QCountry;
import com.inventory.inventory.Repository.CityRepositoryImpl;
import com.inventory.inventory.Repository.Interfaces.BaseRepository;
import com.inventory.inventory.Repository.Interfaces.CityRepository;
import com.inventory.inventory.ViewModels.City.CityDAO;
import com.inventory.inventory.ViewModels.City.EditVM;
import com.inventory.inventory.ViewModels.City.FilterVM;
import com.inventory.inventory.ViewModels.City.IndexVM;
import com.inventory.inventory.ViewModels.City.OrderBy;
import com.inventory.inventory.ViewModels.City.References;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;


@Service
public class CityService extends BaseService<City, FilterVM, OrderBy, IndexVM, EditVM>{

	@Autowired
	CityRepository repo;
	
	@Autowired
	CityRepositoryImpl repoImpl;
	
	
	@Override
	protected BaseRepository<City> repo() {
		return repo;
	}

	@Override
	protected City newItem() {
		return new City();
	}

	@Override
	protected FilterVM filter() {
		return new FilterVM();
	}

	@Override
	protected EditVM editVM() {
		return new EditVM();
	}

	@Override
	protected OrderBy orderBy() {
		return new OrderBy();
	}

	@Override
	protected void populateModel(IndexVM model) {		
		List<SelectItem> zones = repoImpl.getZones();// existing in database
		List<SelectItem> currencies = repoImpl.getCurrencies();//===
		SelectItem empty = new SelectItem("","");
		zones.add(0, empty);
		currencies.add(0, empty);		
		model.getFilter().setZones(zones);
		model.getFilter().setCurrencies(currencies);		
	}

	@Override
	protected void populateEditGetModel(EditVM model) {
		References r = new References();
		List<SelectItem> zones = r.getZidSelects(); //all zones from references	
		
		List<SelectItem> countries =   //existing countries
		getListItems(Expressions.asBoolean(true).isTrue(), 
				Country.class, "name", "id", "country");
		
		SelectItem empty = new SelectItem("","");
		if(!zones.contains(empty)) zones.add(0, empty);
		
		model.setZones(zones);
		model.setCountries(countries);
	}

	@Override
	protected void populateEditPostModel(@Valid EditVM model)
			throws DuplicateNumbersException, NoParentFoundException, Exception {
	}

	@Override
	protected Long setDAOItems(IndexVM model, Predicate predicate, Long offset, Long limit,
			OrderSpecifier<?> orderSpecifier) {
		List<CityDAO> DAOs = repoImpl.getDAOs(predicate, offset, limit);
		model.setDAOItems(DAOs);		
		return repoImpl.DAOCount(predicate);		
	}
	
	@Override
	public Boolean checkGetAuthorization() {
		ERole role = checkRole();
		return role.equals(ERole.ROLE_Admin) ;
	}

	@Override
	public Boolean checkSaveAuthorization() {
		ERole role = checkRole();
		return role.equals(ERole.ROLE_Admin) ;
	}

	@Override
	public Boolean checkDeleteAuthorization() {
		ERole role = checkRole();
		return role.equals(ERole.ROLE_Admin) ;
	}

	public ResponseEntity<?> get(Long id, Long parentId) throws Exception {
		EditVM model = editVM();		
		if(parentId == null || parentId < 1) throw new Exception("you need country for this end point !!!");
		
		List<SelectItem> countries =   //existing countries
		getListItems(QCountry.country.id.eq(parentId), 
				Country.class, "name", "id", "code", "country");
		
		countries.remove(0);		
		String code = countries.stream().filter(x -> x.getValue().equals(parentId+"")).findFirst().get().getFilterBy();		
		References r = new References();
		List<SelectItem> zones = r.getZidSelectsForRegion(code); //all zones from references		
		
		model.setZones(zones);
		model.setCountries(countries);	
		return ResponseEntity.ok(model);
	}

	

}
