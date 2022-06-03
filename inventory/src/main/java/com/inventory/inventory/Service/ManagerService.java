package com.inventory.inventory.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.inventory.inventory.Events.EClient;
import com.inventory.inventory.Events.EventSender;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.auth.Models.UserDetailsImpl;

@Service
public class ManagerService {
	
	@Autowired
	EventSender sender;
	
	public SseEmitter registerListner(EClient eClient) {		
		
	SseEmitter emitter = new SseEmitter((long) 0); // 0 = untill its closed by error or lost conn
	
	UserDetailsImpl udImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	final Long userId = udImpl.getId();
	String userName = udImpl.getUsername();
	
	if(getRole().equals(ERole.ROLE_Admin))
		 sender.registerAdmin(emitter, userName, eClient);
	else {		
		 sender.registerClient(userId, emitter, userName, eClient);	 
	}

     emitter.onCompletion(() -> {      	
    	 sender.unregister(userId, emitter, getRole());
     });
     
     emitter.onTimeout(() -> {
    	 emitter.complete();             
     });

     return emitter;	
 }
	
	private ERole getRole() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			return
				((UserDetailsImpl)auth.getPrincipal()).getErole();
	}

}
