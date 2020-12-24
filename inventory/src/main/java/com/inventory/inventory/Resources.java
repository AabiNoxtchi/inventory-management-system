package com.inventory.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.inventory.inventory.Model.Event;
import com.inventory.inventory.Model.EventProduct;
import com.inventory.inventory.Model.EventType;
import com.inventory.inventory.Model.QEventProduct;
import com.inventory.inventory.Repository.EventProductRepositoryImpl;
import com.inventory.inventory.Repository.Interfaces.EventProductRepository;
import com.inventory.inventory.Repository.Interfaces.EventsRepository;
import com.querydsl.core.types.Predicate;

@Service
class Resources {
	
    private static final Logger logger = LoggerFactory.getLogger("Resources");
	
	@Autowired
	EventProductRepository eventProductRepo;
	
	@Autowired
	EventProductRepositoryImpl eventProductRepoImpl;
	
	@Autowired
	EventsRepository eventsRepository;
	
	private static Map<Long,SseEmitter> emitters = new HashMap<>();
	
	private static ConcurrentHashMap<Long,List<Long>> discardedForUsers = new ConcurrentHashMap<>();

	private Event discardedEvent;
	private Event getDiscardedEvent() {
		if (discardedEvent == null) {
			discardedEvent = eventsRepository.findByName(EventType.Discarded).get();
		}
		
		return discardedEvent;
	}

	public Map<Long, SseEmitter> getEmitters() {
		if(emitters == null)
			emitters = new HashMap<>();
		return emitters;
	}

	public synchronized void setEmitters(Map<Long, SseEmitter> emitters) {
		Resources.emitters = emitters;
		notify();
	}
	
	public synchronized void removeFromEmitters(Long userId) {
		 getEmitters().remove(userId);
		 notify();  
	}

	public synchronized void putInEmitters(Long userId, SseEmitter emitter) {
		getEmitters().put(userId,emitter);
		notify();
	}
	
	public ConcurrentHashMap<Long, List<Long>> getDiscardedForUsers() {
		if(discardedForUsers == null)
			discardedForUsers = new ConcurrentHashMap<>();
		return discardedForUsers;
	}

	private synchronized void setDiscardedForUsers(ConcurrentHashMap<Long, List<Long>> discardedForUsers) {
		Resources.discardedForUsers = discardedForUsers;
	}

	public synchronized void removeFromDiscardedForUsers(Long userId) {
		
//	    getDiscardedForUsers().remove(userId);
//	    
//		Iterable<EventProduct> events =  eventProductRepo.findAll(QEventProduct.eventProduct.user.id.eq(userId));
//		eventProductRepo.deleteAll(events);
//		//long count = eventProductRepo.count(QEventProduct.eventProduct.product.user.id.eq(userId));
//		//logger.info(" count = " + count);		
//		notify();
		 
	}

	public synchronized void putInDiscardedForUsers(Long userId, Long productId) {
		
//		 List<Long> discardedProductsIds = new ArrayList<Long>();			 		  
//		 discardedProductsIds.add(productId);
//	     
//		 getDiscardedForUsers().put(userId, discardedProductsIds);
//		 
//		 //**************??**********/
//		 List<EventProduct> events =  (List<EventProduct>) eventProductRepo.findAll(QEventProduct.eventProduct.user.id.eq(userId));
//		 eventProductRepo.deleteInBatch(events);
//			
//		 for(Long pId : discardedProductsIds) {
//			 
//			 eventProductRepo.save( new EventProduct(getDiscardedEvent(), pId, userId));
//		 }
//		 
//		 notify();
		 
	}
	
	public synchronized void addInDiscardedForUsersList(Long userId, Long productId) {
		
		if(discardedForUsers.contains(userId)) {
			
			discardedForUsers.get(userId).add(productId);
			eventProductRepo.save( new EventProduct(getDiscardedEvent(), productId, userId));		
		}else {
			
			putInDiscardedForUsers( userId, productId);
		}
		notify();
	} 

	private void populateEmitters() {}

	private void populateDiscardedForUsers() {
		
		Predicate p = QEventProduct.eventProduct.event.name.eq(EventType.Discarded);
		setDiscardedForUsers( eventProductRepoImpl.getAllEvents(p));
	} 
	
	public void populateResources() {
		
		populateEmitters();
		populateDiscardedForUsers();
	}
	
}
