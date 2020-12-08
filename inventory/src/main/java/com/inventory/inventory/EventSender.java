package com.inventory.inventory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class EventSender {
	
	private static final Logger logger = LoggerFactory.getLogger("EventSender");
	
	@Autowired
	Resources resources ;	
	
	public void register(Long userId, SseEmitter emitter) {
		System.out.println(" Resources.put in emitters");
		resources.putInEmitters(userId, emitter);
		
		System.out.println("(userid) = "+userId);
		for(Long id : resources.getDiscardedForUsers().keySet()) {
			System.out.println("id = "+id);
		}
			
			
		System.out.println("(resources.getDiscardedForUsers().containsKey(userId)) = "+(resources.getDiscardedForUsers().containsKey(userId)));
		if(resources.getDiscardedForUsers().containsKey(userId)) {
			logger.info("sending emitter *******");
			if(! sendEmitter(emitter, resources.getDiscardedForUsers().get(userId))) resources.removeFromEmitters(userId);
			else resources.removeFromDiscardedForUsers(userId);
		}
	}
	
	public void unregister(Long userId) {
		resources.removeFromEmitters(userId);
	}
	
  public void notifyUsers() {
    	
    	Map<Long,List<Long>> discardedForUsers = resources.getDiscardedForUsers();
    	
    	Map<Long,SseEmitter> emitters = resources.getEmitters();
    	
    	for(Map.Entry<Long, List<Long>> entry : discardedForUsers.entrySet()) {
    		
    		Long userId = entry.getKey();
    		List<Long> productsIds = entry.getValue();
    		
    		if(emitters.containsKey(userId)) {
    			
    			if(! sendEmitter(emitters.get(userId), productsIds)) resources.removeFromEmitters(userId);
    			else resources.removeFromDiscardedForUsers(userId);
    			
    		}
    	}
	}
  
	private boolean sendEmitter(SseEmitter emitter, List<Long> discardedProductsIds) {

		boolean sent = true;
		  if( discardedProductsIds.size() > 0) {
			  
		  SseEmitter.SseEventBuilder event = 
				  SseEmitter.event()
				  .name("discarded")
				  .data(discardedProductsIds); 
		  try {
			  
			emitter.send(event);
			
		} catch (IOException e) {
			
			e.printStackTrace();
			sent = false;
			
		} 
	 }
		  
		  return sent;
	}


}
