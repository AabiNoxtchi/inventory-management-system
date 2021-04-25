package com.inventory.inventory.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory.Model.Delivery;
import com.inventory.inventory.Service.BaseService;
import com.inventory.inventory.Service.DeliveryService;
import com.inventory.inventory.ViewModels.Delivery.EditVM;
import com.inventory.inventory.ViewModels.Delivery.FilterVM;
import com.inventory.inventory.ViewModels.Delivery.IndexVM;
import com.inventory.inventory.ViewModels.Delivery.OrderBy;

@RestController
@RequestMapping(value = "${app.BASE_URL}/deliveries")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
public class DeliveryController extends BaseController<Delivery, FilterVM, OrderBy, IndexVM, EditVM> {

	@Autowired
	DeliveryService service;
	
	@Override
	protected BaseService<Delivery, FilterVM, OrderBy, IndexVM, EditVM> service() {
		return service;
	}
	
	protected ResponseEntity<?> errorsResponse(EditVM model) {
 		return service.errorsResponse(model);
 	}
	
	@DeleteMapping("/{id}/child/{childid}")
	@PreAuthorize("this.checkDeleteAuthorization()")
	public ResponseEntity<?> deletechild( @PathVariable Long id, @PathVariable Long childid) {	
			try {
				return service.deleteChild(id, childid);
			} catch (Exception e) {
				return exceptionResponse(e.getMessage());
			}
	}

}
