package com.inventory.inventory.Events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@Component
class Resources {	
    
    private static List<SseEmitter> adminEmitters = new ArrayList<>();
    protected List<SseEmitter> getAdminEmitters() {    	
    	return adminEmitters;
    }
    protected synchronized void addToAdminEmitters(SseEmitter emitter) {    	
    	
    	if(!adminEmitters.contains(emitter))
    		adminEmitters.add(emitter);
    }
    protected synchronized void removeFromAdminEmitters(SseEmitter emitter) {
    
    	if(adminEmitters != null)
    		adminEmitters.remove(emitter);
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
    protected synchronized void removeFromAdminEvents(EventType key) {
		adminEvents.remove(key);
		notify();
		
	}

	
	private static Map<Long, List<SseEmitter>> WebEmitters = new HashMap<>();	
	protected Map<Long, List<SseEmitter>> getWebEmitters() {		
		return WebEmitters;
	}
	protected List<SseEmitter> getWebEmitters(Long userId) {		
		return WebEmitters.get(userId);
	}
	private synchronized void setWebEmitters(Long userId, List<SseEmitter> emitters, SseEmitter emitterToAdd, SseEmitter emitterToRemove) {
		
		if((emitters == null || emitters.size() == 0) && emitterToAdd == null)
			WebEmitters.remove(userId);
		else if(emitters != null) {		
			if(emitterToAdd != null) {
				emitters.add(emitterToAdd);
				WebEmitters.put(userId, emitters);
			}
			else if(emitterToRemove != null) {
				emitters.remove(emitterToRemove);
				WebEmitters.put(userId, emitters);
			}
			else
				WebEmitters.put(userId, emitters);
		}	
		System.out.println("set web emitters user.emitters.size = "+ getWebEmitters().get(userId).size());
		
		notify();
	}	
	protected synchronized void putInWebEmitters(Long userId, SseEmitter emitter) {
		
		List<SseEmitter> emitters = getWebEmitters().get(userId);
		
		if(emitters == null) {
			emitters = new ArrayList<>();
			emitters.add(emitter);
			setWebEmitters(userId, emitters, null, null);
		}else {
			if(!emitters.contains(emitter))
				setWebEmitters(userId, emitters, emitter, null);			
		}
		
		
		notify();
	}	
	protected synchronized void removeFromEmitters(Long userId, SseEmitter emitter) {
		List<SseEmitter> emitters = getWebEmitters().get(userId);
		if(emitters == null) return;
		
		if(!emitters.contains(emitter)) return;
		setWebEmitters(userId, emitters, null, emitter);
		
		//System.out.println("remove from web emitters user.emitters.size = "+ getWebEmitters().get(userId).size());
		 
		 notify();  
	}
	
	private static ConcurrentHashMap<Long, ConcurrentMap<EventType, List<Long>>> events = new ConcurrentHashMap<>();	
	protected  ConcurrentMap<EventType,List<Long>> getUserEvents(Long userId) {		
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
	protected synchronized void addInEvents(Long userId, Long inventoryId, EventType type) {
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
	protected synchronized void removeFromEvents(Long userId) {		
		events.remove(userId);		
		notify();		 
	}

	protected void populateResources() {		
		//populateEvents();
	}
	
}


