package com.inventory.inventory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Repository.EventProductRepositoryImpl;
import com.inventory.inventory.Repository.RepositoryImpl;
import com.inventory.inventory.Repository.Interfaces.ProductsRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;

@Service
 class EventHandler {
	
	private static final Logger logger = LoggerFactory.getLogger("EventHandler");
	
	@Autowired
	EventProductRepositoryImpl eventProductRepoImpl;
	
	@Autowired
	ProductsRepository productsRepository;
	
	@Autowired
	EventSender sender;

	@Autowired
	Resources resources ;
	
	Long sleep = (long) (60 * 1000 * 60*24); //every m * 60*24 for test
	//Long sleep = (long) (60 * 1000 *1); //every ? m for test
	
	public void runHandler() {
		
		resources.populateResources();
		
		ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> 
        {
        	while(true) {
          		
          		delay(); 
          		
          		List<Tuple> productsToDiscard = productsToDiscard();              		
          		if( productsToDiscard.size() < 1 ) continue;
          	    discardForUsers(productsToDiscard);
          		 
          		sender.notifyUsers();
             }
        });
        
        executor.shutdown();
          
    }
	
    private List<Tuple> productsToDiscard() {
    	
//    	LocalDate date = LocalDate.now();
//    	int days = 365;
//    	
//		Predicate predicate1 = QProduct.product.yearsToDiscard.multiply(days).subtract(
//				Expressions.numberTemplate
//				    (Integer.class , "FLOOR((TO_DAYS({0})-TO_DAYS({1})))",date, QProduct.product.dateCreated )).lt(1);
//		
//		Predicate predicate = QProduct.product.isDiscarded.isFalse().and(predicate1);
//    	
//    	List<Tuple> productsToDiscard = eventProductRepoImpl.getProducsToDiscard(predicate);
//    	//logger.info(" productsToDiscard.size() = " + productsToDiscard.size());
//    	return productsToDiscard;
    	return null;
    }
    
    private void discardForUsers(List<Tuple> productsToDiscard) {
		
//		Map<Long,List<Long>> discardedForUsers = resources.getDiscardedForUsers();
//		
//		for(Tuple tuple : productsToDiscard) {
//			
//			Product product = tuple.get(0, Product.class);
//			Long userId = tuple.get(1, Long.class);
//			
//			 product.setDiscarded(true);
//			 productsRepository.save(product);      //******** testing ***********/
//			 
//			 if(discardedForUsers.containsKey(userId)) {
//				 
//				 //logger.info(" addInDiscardedForUsersList ");
//				 resources.addInDiscardedForUsersList(userId, product.getId());
//				 logger.info(" product.getEmployee_id()!=null "+(product.getEmployee_id()!=null));
//				
//				 if(product.getEmployee_id()!=null) {
//					 logger.info(" product.getEmployee_id() = "+(product.getEmployee_id()));
//			    		resources.addInDiscardedForUsersList(product.getEmployee_id(), product.getId());
//				 }
//				 
//				 
//			 }else{
//				 
//				
//			     logger.info(" putInDiscardedForUsers ");
//			     resources.putInDiscardedForUsers(userId, product.getId());
//			     if(product.getEmployee_id()!=null) 
//			    		resources.putInDiscardedForUsers(product.getEmployee_id(), product.getId());
//			  
//			 }
//		}
	}
    


//    private void notifyUsers() {
//    	
//    	Map<Long,List<Long>> discardedForUsers = Resources.getDiscardedForUsers();
//    	
//    	Map<Long,SseEmitter> emitters = resources.getEmitters();
//    	
//    	for(Map.Entry<Long, List<Long>> entry : discardedForUsers.entrySet()) {
//    		
//    		Long userId = entry.getKey();
//    		List<Long> productsIds = entry.getValue();
//    		
//    		if(emitters.containsKey(userId)) {
//    			
//    			if(! sendEmitter(emitters.get(userId), productsIds)) resources.removeFromEmitters(userId);
//    			else resources.removeFromDiscardedForUsers(userId);
//    			
//    		}
//    	}
//	}
//
//	private boolean sendEmitter(SseEmitter emitter, List<Long> discardedProductsIds) {
//
//		boolean sent = true;
//		  if( discardedProductsIds.size() > 0) {
//			  
//		  SseEmitter.SseEventBuilder event = 
//				  SseEmitter.event()
//				  .name("discarded")
//				  .data(discardedProductsIds); 
//		  try {
//			  
//			emitter.send(event);
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//			sent = false;
//			
//		} 
//	 }
//		  
//		  return sent;
//	}

	

	private void delay() {
          try {
        	  
                Thread.sleep(sleep); 
          } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
          }
    }

}
