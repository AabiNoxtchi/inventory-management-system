package com.inventory.inventory.Events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@Service
class Resources {	
    
    private static SseEmitter adminEmitter;
    protected SseEmitter getAdminEmitter() {    	
    	return adminEmitter;
    }
    protected synchronized void setAdminEmitter(SseEmitter emitter) {
    	System.out.println("setting admin emitter");
    	adminEmitter = emitter;
    }
    
    private static ConcurrentMap<EventType, List<Long>> adminEvents = new ConcurrentHashMap<>();
    protected ConcurrentMap<EventType, List<Long>> getAdminEvents() {		
		return adminEvents;
	}
    protected synchronized void addInAdminEvents(EventType type, Long id) {
		
		if(adminEvents.get(type) == null) {
			List<Long> ids = new ArrayList<>();
			ids.add(id);
			adminEvents.put(type, ids);
			
		}else
			getAdminEvents().get(type).add(id);
		notify();
	} 

	
	private static Map<Long, SseEmitter> WebEmitters = new HashMap<>();
	
	protected Map<Long, SseEmitter> getWebEmitters() {
		if(WebEmitters == null)
			WebEmitters = new HashMap<>();
		return WebEmitters;
	}
	
	protected synchronized void putInWebEmitters(Long userId, SseEmitter emitter) {
		getWebEmitters().put(userId,emitter);
		notify();
	}
	
	protected synchronized void removeFromEmitters(Long userId) {
		 getWebEmitters().remove(userId);
		 notify();  
	}
	
	private static ConcurrentHashMap<Long, ConcurrentMap<EventType, List<Long>>> events = new ConcurrentHashMap<>();
	
	protected  ConcurrentMap<EventType, List<Long>> getUserEvents(Long userId) {		
		return events.get(userId);
	}
	
	protected synchronized void setUserMap(Long userId, ConcurrentMap<EventType, List<Long>> map) {
		events.put(userId, map);		
	}
	
	protected synchronized void addInEvents(EventType type, Map<Long, List<Long>> list) {
		
		for (Map.Entry<Long, List<Long>> e : list.entrySet()) {
			Long userId = e.getKey();
			List<Long> ids = e.getValue();
			
		if(events.get(userId) != null) {
			
			events.get(userId).put(type, ids);
				
		}else {
			ConcurrentMap<EventType, List<Long>> map = new ConcurrentHashMap<>();
			map.put(type, ids);
			events.put(userId, map);			
		}
		}
		notify();
	} 
	
	protected synchronized void removeFromEvents(Long userId) {		
		events.remove(userId);		
		notify();		 
	}

	protected synchronized void addFullyAmortizedInventories(Long userId, Long inventoryId) {
		EventType type = EventType.Amortized;
		 if(events.get(userId) != null) {
			 Map<EventType, List<Long>> map = events.get(userId);
			 List<Long> ids = map.get(type);					 
			 if(ids != null)
			 {
				 if(ids.indexOf(inventoryId) < 0)
					 ids.add(inventoryId);
			 }else {
				 
				 ids = new ArrayList<>();
				 ids.add(inventoryId);
				 map.put(type, ids);
			 }			 
		 }else{
			 
			 List<Long> ids = new ArrayList<>();
				ids.add(inventoryId);
				ConcurrentMap<EventType, List<Long>> map = new ConcurrentHashMap<>();
				map.put(type, ids);
				events.put(userId, map);
		 }		
		
		notify();
	} 

	protected void populateResources() {		
		//populateEvents();
	}

	protected void putInMobileEmitters(Long userId, SseEmitter emitter) {
		// TODO Auto-generated method stub		
	}
	
	
}


