package com.inventory.inventory.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.inventory.inventory.Model.BaseEntity;
import com.inventory.inventory.Service.BaseService;
import com.inventory.inventory.ViewModels.Shared.BaseFilterVM;
import com.inventory.inventory.ViewModels.Shared.BaseIndexVM;
import com.inventory.inventory.ViewModels.Shared.BaseOrderBy;

public abstract class BaseController <E extends BaseEntity , F extends BaseFilterVM,O extends BaseOrderBy,
							IndexVM extends BaseIndexVM<E, F, O>> {
	
	
	  protected abstract BaseService< E , F , O, IndexVM > service();
	  public abstract Boolean checkGetAuthorization();
	  
	  @RequestMapping(method = RequestMethod.GET)	  
	  @ResponseBody 
	  @PreAuthorize("this.checkGetAuthorization()")
	  public  ResponseEntity<IndexVM> getAll(IndexVM model) { 
		  return  service().getAll(model); }

}
