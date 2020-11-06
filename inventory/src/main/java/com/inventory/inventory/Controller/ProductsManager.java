package com.inventory.inventory.Controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import com.inventory.inventory.Service.ProductsManagerService;
import com.inventory.inventory.Service.ProductsService;
import com.inventory.inventory.auth.Models.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/manager")
public class ProductsManager {
	
	@Autowired
	private ProductsManagerService service;
	
	Long userId=(long) 0;

    @GetMapping("/products")
    public SseEmitter fetchData() 
    {
    	
    	UserDetailsImpl principal=
				(UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if(principal!=null) 
    		  userId=principal.getId();
    	
          SseEmitter emitter = new SseEmitter((long) 0);          
          ExecutorService executor = Executors.newSingleThreadExecutor();

          executor.execute(() -> 
          {
                try {
                	while(true) {
                		
              		    List<Long> discardedProductsIds = service.discardProducts(userId);
                		if(discardedProductsIds !=null && discardedProductsIds.size() > 0)
	                	{      
                			SseEmitter.SseEventBuilder event  = SseEmitter.event()	                        
		                        .name("discarded")
		                        .data(discardedProductsIds);       			
                			emitter.send(event);
                		}
                		
                            randomDelay();                      
                     }

                } catch (IOException e) {
                      emitter.completeWithError(e);
                }
          });
          
          executor.shutdown();
          return emitter;
    }

    private void randomDelay() {
          try {
                Thread.sleep(60000);//every m for test
          } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
          }
    }
}
	
	 