package com.inventory.inventory.Service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.Supplier;
import com.inventory.inventory.Repository.Interfaces.BaseRepository;
import com.inventory.inventory.Repository.Interfaces.DeliveryRepository;
import com.inventory.inventory.Repository.Interfaces.SuppliersRepository;
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
		return new EditVM();
	}

	@Override
	protected OrderBy orderBy() {
		return new OrderBy();
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
	}

	@Override
	protected boolean handleDeletingChilds(Supplier e) {
		return false;
	}

	@Override
	protected void populateEditGetModel(EditVM model) {	
	}

	@Override
	protected Long setDAOItems(IndexVM model, Predicate predicate, Long offset, Long limit,
			OrderSpecifier<?> orderSpecifier) {
		return null;
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

	

}
