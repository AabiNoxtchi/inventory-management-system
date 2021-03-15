package com.inventory.inventory.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory.Model.DeliveryDetail;
import com.inventory.inventory.Service.BaseService;
import com.inventory.inventory.Service.DeliveryDetailsService;
import com.inventory.inventory.ViewModels.DeliveryDetail.EditVM;
import com.inventory.inventory.ViewModels.DeliveryDetail.FilterVM;
import com.inventory.inventory.ViewModels.DeliveryDetail.IndexVM;
import com.inventory.inventory.ViewModels.DeliveryDetail.OrderBy;

@RestController
@RequestMapping(value = "${app.BASE_URL}/deliverydetails")
public class DeliveryDetailsController extends BaseController<DeliveryDetail, FilterVM, OrderBy, IndexVM, EditVM> {

	@Autowired
	DeliveryDetailsService service;
	
	@Override
	protected BaseService<DeliveryDetail, FilterVM, OrderBy, IndexVM, EditVM> service() {
		return service;
	}

}
