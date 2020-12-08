package com.inventory.inventory.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.inventory.inventory.EventSender;
import com.inventory.inventory.auth.Models.UserDetailsImpl;

@Service
public class ProductsManagerService {
	
	@Autowired
	EventSender sender;
	
	
	public SseEmitter registerListner() {

	SseEmitter emitter = new SseEmitter((long) 0); 
	
	final Long userId = ((UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
	
	 sender.register(userId, emitter);	 

     emitter.onCompletion(() -> sender.unregister(userId));
     emitter.onTimeout(() -> {
             emitter.complete(); 
             sender.unregister(userId);
     });

     return emitter;
	
  }
	

}
