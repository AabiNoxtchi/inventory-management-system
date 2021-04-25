package com.inventory.inventory.Events;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component 
public class EventSender {
	
	Long sleep = (long) (1000 * 30) ;
	
	@Autowired	
	Resources resources ;	
	
	protected void runHandler() {
		
		ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> 
        {
        	while(true) {
        		delay();
        		 Map<Long,SseEmitter> emitters = resources.getWebEmitters();
        		 
        		 for(Map.Entry<Long, SseEmitter> e : emitters.entrySet())
        			 if(!sendEmitter(e.getValue(), EventType.KeepAlive.name(), EventType.KeepAlive))
        				 resources.removeFromEmitters(e.getKey());  
        		 
        		 SseEmitter adminEmitter = resources.getAdminEmitter();
        		 if(adminEmitter != null && !sendEmitter(adminEmitter, EventType.KeepAlive.name(), EventType.KeepAlive))
    				resources.setAdminEmitter(null);
          		
             }
        });
        
        executor.shutdown();          
    }
	
	protected void notifyUsers() {	 

		  Map<Long,SseEmitter> emitters = resources.getWebEmitters();
		  if(emitters.size() == 0) return;
		  
		  for(Map.Entry<Long, SseEmitter> entry : emitters.entrySet()) {
			  Long userId = entry.getKey();
			  SseEmitter emitter = entry.getValue(); 		  
			  checkmsgs(userId, emitter);		  
		  }
	}
	
	public void registerAdmin(SseEmitter emitter, String userName, EClient eClient) {		
		ConcurrentMap<EventType, List<Long>> events = resources.getAdminEvents();
		
		for(Map.Entry<EventType, List<Long>> e : events.entrySet()) {			
			sendEmitter(emitter, e.getValue(), e.getKey());
			}	
		
		resources.setAdminEmitter(emitter);		
	}
	
	public void registerClient(Long userId, SseEmitter emitter, String userName, EClient eClient) {
		
		if(sendEmitter(emitter, "Welcome "+userName, EventType.Message))		
			checkmsgs(userId, emitter);
		
		if(eClient.equals(EClient.Web))			
			resources.putInWebEmitters(userId, emitter);
		else
			resources.putInMobileEmitters(userId, emitter);			
		
	}	
	
	public void notifyAdmin(EventType type, Long id) {
		SseEmitter emitter = resources.getAdminEmitter();
		
		if( emitter != null)
			sendEmitter(emitter, id, type);
		else
			resources.addInAdminEvents(type, id);		
	}
	

	public void unregister(Long userId) {
		resources.removeFromEmitters(userId);
	}
  
	private void checkmsgs(Long userId, SseEmitter emitter) {
		ConcurrentMap<EventType, List<Long>> map = resources.getUserEvents(userId);
		  
		  if(map == null) return;
		  for(Map.Entry<EventType, List<Long>> e : map.entrySet()) {
			  
			  if(! sendEmitter(emitter, e.getValue(), e.getKey())) {resources.removeFromEmitters(userId); break;}
			  else map.remove(e.getKey());	
		  }
		  
		  resources.setUserMap(userId, map);		
	}

	@SuppressWarnings("unchecked")
	private boolean sendEmitter(SseEmitter emitter, Object data, EventType type) {
		
		boolean sent = true;
		  if( data != null && (!(data instanceof List && (( List<Object>) data).size() < 0))) {			  
			  SseEmitter.SseEventBuilder event = 
				  SseEmitter.event()
				  .name(type.name().toLowerCase())
				  .data(data); 
		  try{			  
			  emitter.send(event);			
			}catch (IOException e) {			
				e.printStackTrace();
				sent = false;			
			} 
	 }		  
		  
		  return sent;
	}
	
	private void delay() {
        try {      	  
              Thread.sleep(sleep); 
        } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
        }
  }

}
