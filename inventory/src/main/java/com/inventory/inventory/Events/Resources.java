package com.inventory.inventory.Events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.inventory.inventory.Model.Event;
//import com.inventory.inventory.Model.EventProduct;
//import com.inventory.inventory.Model.EventType;
import com.inventory.inventory.Repository.EventProductRepositoryImpl;
import com.querydsl.core.types.Predicate;

@Service
class Resources {
	
    private static final Logger logger = LoggerFactory.getLogger("Resources");
    
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

	//public synchronized void setWebEmitters(Map<Long, SseEmitter> emitters) {
	//	Resources.WebEmitters = emitters;
	//	notify();
	//}
	
	protected synchronized void removeFromEmitters(Long userId) {
		 getWebEmitters().remove(userId);
		 notify();  
	}

	protected synchronized void putInWebEmitters(Long userId, SseEmitter emitter) {
		getWebEmitters().put(userId,emitter);
		notify();
	}
	
	
	private static ConcurrentHashMap<Long, ConcurrentMap<EventType, List<Long>>> events = new ConcurrentHashMap<>();
	
//	private ConcurrentHashMap<Long, ConcurrentMap<EventType, List<Long>>> getEvents() {		
//		return events;
//	}
	
	protected  ConcurrentMap<EventType, List<Long>> getUserEvents(Long userId) {		
		return events.get(userId);
	}
	
	//public synchronized void addInEvents(com.inventory.inventory.Events.EventType alldiscarded,
	//		Map<Long, List<Long>> allDiscardedDeliveries) {
		// TODO Auto-generated method stub
		
	//} 
	//@SuppressWarnings("unchecked")
	protected synchronized void addInEvents(EventType type, Map<Long, List<Long>> list) {
		
		for (Map.Entry<Long, List<Long>> e : list.entrySet()) {
			Long userId = e.getKey();
			List<Long> ids = e.getValue();
			
		if(events.get(userId) != null) {
			
			//Map<EventType, List<Long>> map = events.get(userId);			
			//map.put(type, (List<Long>) list);
			events.get(userId).put(type, ids);
				
		}else {
			ConcurrentMap<EventType, List<Long>> map = new ConcurrentHashMap<>();
			map.put(type, ids);
			events.put(userId, map);			
		}
		}
		notify();
	} 
	
	protected synchronized void setUserMap(Long userId, ConcurrentMap<EventType, List<Long>> map) {
		events.put(userId, map);
		
	}
	
	protected synchronized void removeFromEvents(Long userId) {
		
		events.remove(userId);		
		notify();
		 
	}
	
	
//	private static ConcurrentHashMap<Long, List<Long>> fullyAmortizedInventories = new ConcurrentHashMap<>();
//	
//	public ConcurrentHashMap<Long, List<Long>> getFullyAmortizedInventories() {
//		if(fullyAmortizedInventories == null)
//			fullyAmortizedInventories = new ConcurrentHashMap<>();
//		return fullyAmortizedInventories;
//	}
//	
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
//	
//	public synchronized void removeFromFullyAmortizedInventories(Long userId) {
//		
//		getFullyAmortizedInventories().remove(userId);		
//		notify();
//		 
//	}
//	
//    private static ConcurrentHashMap<Long, List<Long>> allDiscardedDeliveries = new ConcurrentHashMap<>();
//	
//	public ConcurrentHashMap<Long, List<Long>> getAllDiscardedDeliveries() {
//		if(allDiscardedDeliveries == null)
//			allDiscardedDeliveries = new ConcurrentHashMap<>();
//		return allDiscardedDeliveries;
//	}
//	
//	public synchronized void setAllDiscardedDeliveries(ConcurrentHashMap<Long, List<Long>> allDiscardedDeliveries) {
//		Resources.allDiscardedDeliveries = allDiscardedDeliveries;
//		//addInEvents(Long userId, EventType type, List<Long> list)
//		notify();
//	}
//	
////	public synchronized void setAllDiscardedDeliveries(Long userId, Long inventoryId) {
////		
////		if(allDiscardedDeliveries.get(userId) != null) {
////			
////			allDiscardedDeliveries.get(userId).add(inventoryId);
////			//eventProductRepo.save( new EventProduct(getDiscardedEvent(), productId, userId));		
////		}else {
////			List<Long> ids = new ArrayList<>();
////			ids.add(inventoryId);
////			allDiscardedDeliveries.put(userId, ids);
////			//putInDiscardedForUsers( userId, productId);
////		}
////		notify();
////	} 
//	
//	
//	
//	public synchronized void removeFromAllDiscardedDeliveries(Long userId) {
//		
//		getAllDiscardedDeliveries().remove(userId);		
//		notify();
//		 
//	}
//
//
//	
//	


	protected void populateResources() {
		
		//populateEmitters();
		//populateDiscardedForUsers();
	}

	protected void putInMobileEmitters(Long userId, SseEmitter emitter) {
		// TODO Auto-generated method stub
		
	}
	
	
	
//	private static ConcurrentHashMap<Long,List<Long>> discardedForUsers = new ConcurrentHashMap<>();
//	private Event discardedEvent;
//	private Event getDiscardedEvent() {
////		if (discardedEvent == null) {
////			discardedEvent = eventsRepository.findByName(EventType.Discarded).get();
////		}
//		
//		return discardedEvent;
//	}
	
//	public ConcurrentHashMap<Long, List<Long>> getDiscardedForUsers() {
//		if(discardedForUsers == null)
//			discardedForUsers = new ConcurrentHashMap<>();
//		return discardedForUsers;
//	}
//
//	private synchronized void setDiscardedForUsers(ConcurrentHashMap<Long, List<Long>> discardedForUsers) {
//		Resources.discardedForUsers = discardedForUsers;
//	}

//	public synchronized void removeFromDiscardedForUsers(Long userId) {
//		
////	    getDiscardedForUsers().remove(userId);
////	    
////		Iterable<EventProduct> events =  eventProductRepo.findAll(QEventProduct.eventProduct.user.id.eq(userId));
////		eventProductRepo.deleteAll(events);
////		//long count = eventProductRepo.count(QEventProduct.eventProduct.product.user.id.eq(userId));
////		//logger.info(" count = " + count);		
////		notify();
//		 
//	}

//	public synchronized void putInDiscardedForUsers(Long userId, Long productId) {
//		
////		 List<Long> discardedProductsIds = new ArrayList<Long>();			 		  
////		 discardedProductsIds.add(productId);
////	     
////		 getDiscardedForUsers().put(userId, discardedProductsIds);
////		 
////		 //**************??**********/
////		 List<EventProduct> events =  (List<EventProduct>) eventProductRepo.findAll(QEventProduct.eventProduct.user.id.eq(userId));
////		 eventProductRepo.deleteInBatch(events);
////			
////		 for(Long pId : discardedProductsIds) {
////			 
////			 eventProductRepo.save( new EventProduct(getDiscardedEvent(), pId, userId));
////		 }
////		 
////		 notify();
//		 
//	}
	
//	public synchronized void addInDiscardedForUsersList(Long userId, Long productId) {
//		
////		if(discardedForUsers.contains(userId)) {
////			
////			discardedForUsers.get(userId).add(productId);
////			eventProductRepo.save( new EventProduct(getDiscardedEvent(), productId, userId));		
////		}else {
////			
////			putInDiscardedForUsers( userId, productId);
////		}
////		notify();
//	} 
//
//	private void populateEmitters() {}
//
//	private void populateDiscardedForUsers() {
//		
//		//Predicate p = QEventProduct.eventProduct.event.name.eq(EventType.Discarded);
//		//setDiscardedForUsers( eventProductRepoImpl.getAllEvents(p));
//	}
//
//	

	
	
	
}
