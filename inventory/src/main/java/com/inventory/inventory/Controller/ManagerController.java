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

import com.inventory.inventory.Events.EClient;
import com.inventory.inventory.Service.ManagerService;

//@CrossOrigin(origins = "*", maxAge = 3600)
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
@RestController
@RequestMapping(value = "/api/inventory/manager")
public class ManagerController {
	
	
	
	@Autowired
	private ManagerService service;
	
	 @GetMapping("/subscribeWebClient")
	 private SseEmitter registerWebListner()
	 {    	
		
	    return service.registerListner(EClient.Web);
	 }
	 
	 @GetMapping("/subscribeMobileClient")
	 private SseEmitter registerMobileListner()
	 {    	
		
	    return service.registerListner(EClient.Mobile);
	 }
	 
	 
	 


	
}
	
	 