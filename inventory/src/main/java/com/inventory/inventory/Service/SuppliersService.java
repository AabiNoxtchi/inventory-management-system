package com.inventory.inventory.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.inventory.inventory.Model.City;
import com.inventory.inventory.Model.Delivery;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.QDelivery;
import com.inventory.inventory.Model.Supplier;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.Repository.Interfaces.BaseRepository;
import com.inventory.inventory.Repository.Interfaces.DeliveryRepository;
import com.inventory.inventory.Repository.Interfaces.SuppliersRepository;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.inventory.inventory.ViewModels.Supplier.EditVM;
import com.inventory.inventory.ViewModels.Supplier.FilterVM;
import com.inventory.inventory.ViewModels.Supplier.IndexVM;
import com.inventory.inventory.ViewModels.Supplier.OrderBy;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;

@Service
public class SuppliersService extends BaseService<Supplier, FilterVM, OrderBy, IndexVM, EditVM>{

	@Autowired
	SuppliersRepository repo;
	
	@Autowired
	DeliveryRepository dRepo;
	
	@Override
	protected BaseRepository<Supplier> repo() {		
		return repo;
	}

	@Override
	protected Supplier newItem() {
		return new Supplier();
	}

	@Override
	protected FilterVM filter() {
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
	public Boolean checkGetAuthorization() {
		return checkRole().equals(ERole.ROLE_Mol);
	}

	@Override
	public Boolean checkSaveAuthorization() {
		return checkRole().equals(ERole.ROLE_Mol);
	}

	@Override
	public Boolean checkDeleteAuthorization() {
		return checkRole().equals(ERole.ROLE_Mol);
	}
	
	@Override
	protected void populateModel(IndexVM model) {
		model.getFilter().setWhosAskingId(getLoggedUser().getId());
	}

	@Override
	protected void populateEditPostModel(@Valid EditVM model) {
		model.setMol(getLoggedUser().getId());
		
	}

	@Override
	protected void handleDeletingChilds(List<Supplier> items) {
		for(Supplier s : items) {
			handleDeletingChilds(s);
		}
		
	}

	@Override
	protected boolean handleDeletingChilds(Supplier e) {
		List<Delivery> deliveries = (List<Delivery>) dRepo.findAll(QDelivery.delivery.supplier.id.eq(e.getId()));
		for(Delivery d : deliveries) {
			d.setSupplier(null);
		}
		
		dRepo.saveAll(deliveries);	
		return false;
	}

	@Override
	protected void populateEditGetModel(EditVM model) {
		
//		String phone = model.getPhoneNumber();
//		String currentCode = model.getId()!=null && model.getId() > 0 && phone !=null ?
//				phone.substring(phone.indexOf("+")+1,phone.indexOf("/")):null;
//				System.out.println("current code = "+currentCode);
//				phone = phone==null?null:phone.substring(phone.indexOf("/")+1, phone.length());
//				model.setPhoneNumber(phone);
//		 //model.setSelectedCode();
//		
//		City city = currentCode==null?getCurrentUserCity():null;
//		String molCountryName = city==null?null:city.getCountry().getName();
//		
//		List<SelectItem> phoneCodes = new ArrayList<>();
//		for (String cc : PhoneNumberUtil.getInstance().getSupportedRegions()) {
//			
//            int phoneCode = PhoneNumberUtil.getInstance().getCountryCodeForRegion(cc);
//            String displayCountry = (new Locale("", cc)).getDisplayCountry();
//            SelectItem si = new SelectItem(cc, displayCountry+"/+"+phoneCode);
//            phoneCodes.add(si);
//            
//            if((phoneCode+"").equals(currentCode) || displayCountry.equals(molCountryName)) 
//            		{model.setDefaultCodeValue(si);}//model.setSelectedCode(currentCode+"");}
//          //  else if() {model.setDefaultCodeValue(si);}//model.setSelectedCode(phoneCode+"");}
//           
//           // if(molCountryName!=null ) model.setDefaultCodeValue(displayCountry);
//            
//          // System.out.println("cc = "+cc+" code = "+phoneCode+" country = "+displayCountry );
//        }
//		model.setPhoneCodes(phoneCodes);
	}

	@Override
	protected Long setDAOItems(IndexVM model, Predicate predicate, Long offset, Long limit,
			OrderSpecifier<?> orderSpecifier) {
		// TODO Auto-generated method stub
		return null;
	}

}
