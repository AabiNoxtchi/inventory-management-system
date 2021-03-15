package com.inventory.inventory.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory.Model.UserProfile;
import com.inventory.inventory.Service.BaseService;
import com.inventory.inventory.Service.UserProfilesService;
import com.inventory.inventory.ViewModels.UserProfiles.EditVM;
import com.inventory.inventory.ViewModels.UserProfiles.FilterVM;
import com.inventory.inventory.ViewModels.UserProfiles.IndexVM;
import com.inventory.inventory.ViewModels.UserProfiles.OrderBy;



@RestController
@RequestMapping("${app.BASE_URL}/userprofiles")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
public class UserProfilesController extends BaseController<UserProfile, FilterVM, OrderBy, IndexVM, EditVM>{

	@Autowired
	UserProfilesService service;
	
	@Override
	protected BaseService<UserProfile, FilterVM, OrderBy, IndexVM, EditVM> service() {
		// TODO Auto-generated method stub
		return service;
	}

}
