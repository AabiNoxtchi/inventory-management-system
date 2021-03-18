package com.inventory.inventory.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.inventory.Exception.DuplicateNumbersException;
import com.inventory.inventory.Exception.NoParentFoundException;
import com.inventory.inventory.Model.UserProfile;
import com.inventory.inventory.Service.BaseService;
import com.inventory.inventory.Service.UserProfilesService;
import com.inventory.inventory.ViewModels.UserProfiles.EditVM;
import com.inventory.inventory.ViewModels.UserProfiles.FilterVM;
import com.inventory.inventory.ViewModels.UserProfiles.IndexVM;
import com.inventory.inventory.ViewModels.UserProfiles.OrderBy;
import com.inventory.inventory.ViewModels.UserProfiles.TimeLineEditVM;



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
	
	@GetMapping("/timeline")
	@ResponseBody 
	@PreAuthorize("this.checkSaveAuthorization()")
	public ResponseEntity<?> gettimeline(FilterVM filter){
		
		return service.getTimeline(filter);
	}
	
	@PutMapping("/timeline") 
	    @PreAuthorize("this.checkSaveAuthorization()")
		public ResponseEntity<?> saveTimeline(@RequestBody TimeLineEditVM model) throws Exception{
	    	System.out.println("in saveTimeline model");
	    	  	
	    	return service.saveTimeline(model);	
	    	
	         
	    }

}
