package com.inventory.inventory.Service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventory.inventory.Exception.DuplicateNumbersException;
import com.inventory.inventory.Exception.NoParentFoundException;
import com.inventory.inventory.Model.City;
import com.inventory.inventory.Model.Country;
import com.inventory.inventory.Model.Delivery;
import com.inventory.inventory.Model.DeliveryDetail;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.QDeliveryDetail;
import com.inventory.inventory.Repository.CountryRepositoryImpl;
import com.inventory.inventory.Repository.Interfaces.BaseRepository;
import com.inventory.inventory.Repository.Interfaces.CityRepository;
import com.inventory.inventory.Repository.Interfaces.CountryRepository;
import com.inventory.inventory.ViewModels.City.References;
import com.inventory.inventory.ViewModels.Country.CityEditVM;
import com.inventory.inventory.ViewModels.Country.CountryDAO;
import com.inventory.inventory.ViewModels.Country.EditVM;
import com.inventory.inventory.ViewModels.Country.FilterVM;
import com.inventory.inventory.ViewModels.Country.IndexVM;
import com.inventory.inventory.ViewModels.Country.OrderBy;
import com.inventory.inventory.ViewModels.Shared.PagerVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;




@Service
public class CountryService extends BaseService<Country, FilterVM, OrderBy, IndexVM, EditVM>{

	@Autowired
	CountryRepository repo;
	
	@Autowired
	CountryRepositoryImpl repoImpl;
	
	@Autowired
	CityRepository cityRepo;
	
	@Autowired
	CityService cityService;
	
	
	@Override
	protected BaseRepository<Country> repo() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	protected Country newItem() {
		// TODO Auto-generated method stub
		return new Country();
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
		//List<SelectItem> zones = r.getZidSelects(); //all zones from references	
		
		List<SelectItem> countries =   //existing countries
		getListItems(Expressions.asBoolean(true).isTrue(), 
				Country.class, "name", "id", "country");
		
		//model.setZones(zones);
		//model.setCountries(countries);
		
		List<SelectItem> allCountries = //r.getCountrySelects();//all countries - existing  from references	 //===
				r.getCountrySelectsMinus(countries);
		//all countries + current
		if(model.getName()!=null)allCountries.add(new SelectItem(model.getName(),model.getName()));
		List<SelectItem> currencies = r.getCurrencySelects();//===
		
		SelectItem empty = new SelectItem("","");
		allCountries.add(0, empty);
		currencies.add(0, empty);
		model.setAllCountries(allCountries);
		model.setCurrencies(currencies);
		
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
	
	//protected boolean setModel(IndexVM model, Predicate predicate, Sort sort) {return true;}
	protected boolean setModel(IndexVM model, Predicate predicate, Sort sort) {
		
		System.out.println("country service setmodel islong view = "+(model.isLongView()));
		if(model.isLongView()) {			
			PagerVM pager =  model.getPager();
			Long limit = (long) pager.getItemsPerPage();
			Long offset = pager.getPage() * limit;
			List<CountryDAO> DAOs = repoImpl.getDAOs(predicate, offset, limit);
			model.setDAOItems(DAOs);
			
			Long totalCount = repoImpl.DAOCount(predicate);
			pager.setItemsCount(totalCount);
			pager.setPagesCount((int) (totalCount % limit > 0 ? (totalCount/limit) + 1 : totalCount / limit));
			System.out.println("setmodel country");
			return true;
		}
		else return false;		
	}
	
	public ResponseEntity<?> deleteChild(Long childid) throws Exception {		
		/*City item = cityRepo.findById(childid).get();
		cityRepo.deleteById(childid);
		return ResponseEntity.ok(childid);*/
		return cityService.delete(childid);
	}
	
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
		System.out.println("country service print msg ");
		
	}

	public ResponseEntity<?> getChild(Long id) {
		/*CityEditVM model = new CityEditVM();
		City item = null;
		if(id > 0) {
			Optional<City> opt = cityRepo.findById(id);
			if(opt.isPresent())item = opt.get();
			model.populateModel(item);
		}
		//populateEditGetModel(model);
		List<SelectItem> countries =   //existing countries
				getListItems(Expressions.asBoolean(true).isTrue(), 
						Country.class, "name", "id", "country");
		
		References r = new References();
		List<SelectItem> zones = r.getZidSelects(); //all zones from references	
		
		SelectItem empty = new SelectItem("","");
		zones.add(0, empty);
		countries.add(0,empty);
		
		model.setCountries(countries);
		model.setZones(zones);
		
		
		return ResponseEntity.ok(model);*/
		
		return cityService.get(id);
	}

	public ResponseEntity<?> saveChild(com.inventory.inventory.ViewModels.City.EditVM model) throws Exception {
		return cityService.save(model);
	}
}
