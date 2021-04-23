package com.inventory.inventory.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.inventory.inventory.Events.EClient;
import com.inventory.inventory.Events.EventSender;
import com.inventory.inventory.Events.EventType;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.ViewModels.ProductDetail.ProductDetailDAO;
import com.inventory.inventory.auth.Models.UserDetailsImpl;



@Service
public class ManagerService {
	
	@Autowired
	EventSender sender;
	
	public SseEmitter registerListner(EClient eClient) {
		
		
	System.out.println("recieved emitter subsciption ");


	SseEmitter emitter = new SseEmitter((long) 0); // 0 = untill its closed by error or lost conn
	
	UserDetailsImpl udImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	final Long userId = udImpl.getId();
	String userName = udImpl.getUsername();
	
	if(getRole().equals(ERole.ROLE_Admin))
		sender.registerAdmin(emitter, userName, eClient);
	else
	 sender.registerClient(userId, emitter, userName, eClient);	 

     emitter.onCompletion(() -> sender.unregister(userId));
     emitter.onTimeout(() -> {
             emitter.complete(); 
             sender.unregister(userId);
     });

     return emitter;
	
 }
	
	private ERole getRole() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		//if(!(auth.getPrincipal() instanceof  UserDetailsImpl)) return null;
		return
				((UserDetailsImpl)auth.getPrincipal()).getErole();	
//		getAuthentication().getAuthorities().stream()
//		.map(item -> item.getAuthority())
//		.collect(Collectors.toList()).get(0);
	}



}
