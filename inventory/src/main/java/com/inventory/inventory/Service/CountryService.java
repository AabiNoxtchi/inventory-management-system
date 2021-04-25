package com.inventory.inventory.Service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventory.inventory.Exception.DuplicateNumbersException;
import com.inventory.inventory.Exception.NoParentFoundException;
import com.inventory.inventory.Model.Country;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Repository.CountryRepositoryImpl;
import com.inventory.inventory.Repository.Interfaces.BaseRepository;
import com.inventory.inventory.Repository.Interfaces.CountryRepository;
import com.inventory.inventory.ViewModels.City.References;
import com.inventory.inventory.ViewModels.Country.CountryDAO;
import com.inventory.inventory.ViewModels.Country.EditVM;
import com.inventory.inventory.ViewModels.Country.FilterVM;
import com.inventory.inventory.ViewModels.Country.IndexVM;
import com.inventory.inventory.ViewModels.Country.OrderBy;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.http.ResponseEntity;

@Service
public class CountryService extends BaseService<Country, FilterVM, OrderBy, IndexVM, EditVM>{

	@Autowired
	CountryRepository repo;
	
	@Autowired
	CountryRepositoryImpl repoImpl;	

	@Autowired
	CityService cityService;	
	
	@Override
	protected BaseRepository<Country> repo() {
		return repo;
	}

	@Override
	protected Country newItem() {
		return new Country();
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
	}

	@Override
	protected void populateEditGetModel(EditVM model) {
		References r = new References();
		
		List<SelectItem> allCountries = new ArrayList<>();
		
		if(model.getId() == null || model.getId() < 1) {
		List<SelectItem> countries =   //existing countries
		getListItems(Expressions.asBoolean(true).isTrue(), 
				Country.class, "name", "id", "country");		
		
		 allCountries = r.getCountrySelectsMinus(countries);
		}
		//all countries + current
		if(model.getName() != null && model.getId() > 0) allCountries.add(new SelectItem(model.getCode(), model.getName()));
		
		List<SelectItem> currencies =  new ArrayList<>();
		List<SelectItem> phoneCodes =  new ArrayList<>();
		if(((model.getId() == null || model.getId() < 1) && allCountries.size() > 0) || (model.getId() != null && model.getId() > 0)) {			
			currencies = r.getCurrencySelects(allCountries);
			phoneCodes =  r.getPhoneCodes(allCountries);
		}	
		model.setAllCountries(allCountries);   // country code , country code
		model.setCurrencies(currencies);   // currency , currency
		model.setAllPhoneCodes(phoneCodes); //  phone code , phone code  
		
	}

	@Override
	protected void populateEditPostModel(@Valid EditVM model)
			throws DuplicateNumbersException, NoParentFoundException, Exception {
	}
	
	@Override
	protected Long setDAOItems(IndexVM model, Predicate predicate, Long offset, Long limit,
			OrderSpecifier<?> orderSpecifier) {
		List<CountryDAO> DAOs = repoImpl.getDAOs(predicate, offset, limit);
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
	
	public ResponseEntity<?> deleteChild(Long childid) throws Exception {		
		return cityService.delete(childid);
	}
	
	public ResponseEntity<?> getChild(Long id) throws Exception {		
		return cityService.get(id);
	}

	public ResponseEntity<?> saveChild(com.inventory.inventory.ViewModels.City.EditVM model) throws Exception {
		return cityService.save(model);
	}

	

	public ResponseEntity<?> getChild(Long id, Long parentId) throws Exception {
		return cityService.get(id, parentId);
	}

	
}
