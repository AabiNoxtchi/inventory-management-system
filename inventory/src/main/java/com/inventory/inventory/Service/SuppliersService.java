package com.inventory.inventory.Service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventory.inventory.Model.Delivery;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.QDelivery;
import com.inventory.inventory.Model.Supplier;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.Repository.Interfaces.BaseRepository;
import com.inventory.inventory.Repository.Interfaces.DeliveryRepository;
import com.inventory.inventory.Repository.Interfaces.SuppliersRepository;
import com.inventory.inventory.ViewModels.Supplier.EditVM;
import com.inventory.inventory.ViewModels.Supplier.FilterVM;
import com.inventory.inventory.ViewModels.Supplier.IndexVM;
import com.inventory.inventory.ViewModels.Supplier.OrderBy;

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
	protected void handleDeletingChilds(Supplier e) {
		List<Delivery> deliveries = (List<Delivery>) dRepo.findAll(QDelivery.delivery.supplier.id.eq(e.getId()));
		for(Delivery d : deliveries) {
			d.setSupplier(null);
		}
		
		dRepo.saveAll(deliveries);		
	}

	@Override
	protected void populateEditGetModel(EditVM model) {
		// TODO Auto-generated method stub
		
	}

}
