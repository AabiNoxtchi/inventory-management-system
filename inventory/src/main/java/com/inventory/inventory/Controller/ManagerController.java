package com.inventory.inventory.Controller;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.inventory.inventory.Service.ManagerService;

//@CrossOrigin(origins = "*", maxAge = 3600)
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
@RestController
@RequestMapping(value = "/api/inventory/manager")
public class ManagerController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger("ManagerController");
	 private final ExecutorService executor = Executors.newSingleThreadExecutor();
	
	@Autowired
	private ManagerService service;
	
	 @GetMapping("/subscribe")
	 private SseEmitter registerListner()
	 {    	
		 System.out.println("recieved emitter subsciption ");
		 SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

	        sseEmitter.onCompletion(() -> LOGGER.info("SseEmitter is completed"));

	        sseEmitter.onTimeout(() -> LOGGER.info("SseEmitter is timed out"));

	        sseEmitter.onError((ex) -> LOGGER.info("SseEmitter got error:", ex));

	        executor.execute(() -> {
	            for (int i = 0; i < 15; i++) {
	                try {
	                    sseEmitter.send(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")));
	                    sleep(1, sseEmitter);
	                } catch (IOException e) {
	                    e.printStackTrace();
	                    sseEmitter.completeWithError(e);
	                }
	            }
	            sseEmitter.complete();
	        });

	        LOGGER.info("Controller exits");
	        return sseEmitter;
	    }
	   // return service.registerListner();
	 

private void sleep(int seconds, SseEmitter sseEmitter) {
    try {
        Thread.sleep(seconds * 1000);
    } catch (InterruptedException e) {
        e.printStackTrace();
        sseEmitter.completeWithError(e);
    }
}
	
}
	
	 