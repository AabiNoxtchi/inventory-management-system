package com.inventory.inventory.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.inventory.inventory.Service.ProductsManagerService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/inventory/manager")
public class ProductsManager {
	
	@Autowired
	private ProductsManagerService service;
	
	 @GetMapping("/products")
	 public SseEmitter registerListner()
	 {    	
	    return service.registerListner();
	 }
	
}
	
	 