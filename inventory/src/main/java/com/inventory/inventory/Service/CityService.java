package com.inventory.inventory.Service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.inventory.inventory.Exception.DuplicateNumbersException;
import com.inventory.inventory.Exception.NoParentFoundException;
import com.inventory.inventory.Model.City;
import com.inventory.inventory.Model.Country;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Repository.CityRepositoryImpl;
import com.inventory.inventory.Repository.Interfaces.BaseRepository;
import com.inventory.inventory.Repository.Interfaces.CityRepository;
import com.inventory.inventory.ViewModels.City.CityDAO;
import com.inventory.inventory.ViewModels.City.EditVM;
import com.inventory.inventory.ViewModels.City.FilterVM;
import com.inventory.inventory.ViewModels.City.IndexVM;
import com.inventory.inventory.ViewModels.City.OrderBy;
import com.inventory.inventory.ViewModels.City.References;
import com.inventory.inventory.ViewModels.Product.ProductDAO;
import com.inventory.inventory.ViewModels.Shared.PagerVM;
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
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	protected City newItem() {
		// TODO Auto-generated method stub
		return new City();
	}

	@Override
	protected FilterVM filter() {
		// TODO Auto-generated method stub
		return new FilterVM();
	}

	@Override
	protected EditVM editVM() {
		// TODO Auto-generated method stub
		return new EditVM();
	}

	@Override
	protected OrderBy orderBy() {
		// TODO Auto-generated method stub
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
		zones.add(0, empty);
		//countries.add(0,empty);
		
		model.setZones(zones);
		model.setCountries(countries);
		
//		List<SelectItem> allCountries = //r.getCountrySelects();//all countries - existing  from references	 //===
//				r.getCountrySelectsMinus(countries);
//			
//		List<SelectItem> currencies = r.getCurrencySelects();//===
		
	}

	@Override
	protected void populateEditPostModel(@Valid EditVM model)
			throws DuplicateNumbersException, NoParentFoundException, Exception {
		// TODO Auto-generated method stub
		
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
	
	/*protected boolean setModel(IndexVM model, Predicate predicate, Sort sort) {
		
		if(model.isLongView()) {			
			PagerVM pager =  model.getPager();
			Long limit = (long) pager.getItemsPerPage();
			Long offset = pager.getPage() * limit;
			List<CityDAO> DAOs = repoImpl.getDAOs(predicate, offset, limit);
			model.setDAOItems(DAOs);
			
			Long totalCount = repoImpl.DAOCount(predicate);
			pager.setItemsCount(totalCount);
			pager.setPagesCount((int) (totalCount % limit > 0 ? (totalCount/limit) + 1 : totalCount / limit));
			return true;
		}
		else return false;		
	}*/
	
//	void processChildDto(Parent parent, Set<ChildDto> childDtos) {
//        Set<Child> children = new HashSet<>();
//
//        for (ChildDto dto : childDtos) {
//            Child child;
//            if (dto.getId() == null) {
//                //CREATE MODE: create new child
//                child = new Child();
//                child.setParent(parent); //associate parent 
//            } else {
//                //UPDATE MODE : fetch by id
//                child = childRepository.getOne(dto.getId());
//            }
//
//            BeanUtils.copyProperties(dto, child);//copy properties from dto
//
//            children.add(child);
//        }
//        parent.getChildren().clear();
//        parent.getChildren().addAll(children);
//
//        //finally save the parent ( and children)
//        parentRepository.save(parent);
//    }
	
	protected void printmsg(IndexVM model) {
		//System.out.println("country service filter city id = "+model.getFilter().getCityId());
		
	}

	@Override
	protected Long setDAOItems(IndexVM model, Predicate predicate, Long offset, Long limit,
			OrderSpecifier<?> orderSpecifier) {
		List<CityDAO> DAOs = repoImpl.getDAOs(predicate, offset, limit);
		model.setDAOItems(DAOs);
		
		return repoImpl.DAOCount(predicate);		
	}


}
