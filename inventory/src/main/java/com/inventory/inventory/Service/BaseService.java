package com.inventory.inventory.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.inventory.inventory.Model.BaseEntity;
import com.inventory.inventory.Repository.BaseRepository;
import com.inventory.inventory.ViewModels.Shared.BaseFilterVM;
import com.inventory.inventory.ViewModels.Shared.BaseIndexVM;
import com.inventory.inventory.ViewModels.Shared.BaseOrderBy;
import com.inventory.inventory.ViewModels.Shared.PagerVM;
import com.inventory.inventory.auth.Models.UserDetailsImpl;
import com.querydsl.core.types.Predicate;

public abstract class BaseService< E extends BaseEntity , F extends BaseFilterVM,O extends BaseOrderBy,
																IndexVM extends BaseIndexVM<E, F, O>> {
	private UserDetailsImpl LoggedUser ;
	protected abstract BaseRepository<E> repo();
	protected abstract F filter();
	protected abstract O orderBy();	
	protected void populateModel(IndexVM model) { }
	protected abstract Boolean checkGetAuthorization();
	protected UserDetailsImpl getLoggedUser() {
		if (LoggedUser == null)
		{
		  Authentication auth=SecurityContextHolder.getContext().getAuthentication();
		  LoggedUser = (UserDetailsImpl) auth.getPrincipal();
		}
		  return LoggedUser;
	}
	
	public ResponseEntity<IndexVM> getAll(IndexVM model) {
		
		 if(model.getPager() == null) model.setPager( new PagerVM() );
		 model.getPager().setPrefix("Pager");
		 if(model.getPager().getPage() < 0 ) model.getPager().setPage(0);
		 if(model.getPager().getItemsPerPage() <= 0 ) model.getPager().setItemsPerPage(10);
		
		 if(model.getFilter() == null) model.setFilter( filter() );
		 model.getFilter().setPrefix("Filter");
		 
		 populateModel(model);
		 Predicate predicate = model.getFilter().getPredicate();
		 if(model.getOrderBy() == null) model.setOrderBy( orderBy());		 
		 Sort sort = model.getOrderBy().getSort();
		 
		 Page<E> page = repo().findAll(predicate, model.getPager().getPageRequest(sort));
		 
		 model.setItems(page.getContent());
		 model.getPager().setPagesCount(page.getTotalPages());
		 model.getPager().setItemsCount(page.getTotalElements());
		 
	     return ResponseEntity.ok(model);
	    }
	

}
