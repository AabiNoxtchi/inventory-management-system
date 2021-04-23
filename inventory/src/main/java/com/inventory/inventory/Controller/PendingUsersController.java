package com.inventory.inventory.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory.Service.BaseService;
import com.inventory.inventory.Service.PendingUsersService;
import com.inventory.inventory.ViewModels.PendingUser.EditVM;
import com.inventory.inventory.ViewModels.PendingUser.FilterVM;
import com.inventory.inventory.ViewModels.PendingUser.IndexVM;
import com.inventory.inventory.ViewModels.PendingUser.OrderBy;
import com.inventory.inventory.auth.Models.RegisterRequest;

@RestController
@RequestMapping("${app.BASE_URL}/pendingusers")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
public class PendingUsersController extends BaseController<RegisterRequest, FilterVM, OrderBy, IndexVM, EditVM>{

	@Autowired
	PendingUsersService service;
	
	@Override
	protected BaseService<RegisterRequest, FilterVM, OrderBy, IndexVM, EditVM> service() {
		// TODO Auto-generated method stub
		return service;
	}
	
	

}
