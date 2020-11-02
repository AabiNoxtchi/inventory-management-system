package com.inventory.inventory.Controller;

import java.io.IOException;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

@RestController
public class TestController {
	
	/*
	 * private final DataSetService dataSetService;
	 * 
	 * public DataSetController(DataSetService dataSetService) { this.dataSetService
	 * = dataSetService; }
	 */

    @GetMapping("/test")
    public SseEmitter fetchData2() 
    {
          SseEmitter emitter = new SseEmitter();

          ExecutorService executor = Executors.newSingleThreadExecutor();

          executor.execute(() -> 
          {
               // List<DataSet> dataSets = dataSetService.findAll();
                try {
                      for (int i=0;i<7;i++) {

                            randomDelay();
                            emitter.send(i);
                      }

                      emitter.complete();

                } catch (IOException e) {
                      emitter.completeWithError(e);
                }
          });
          executor.shutdown();
          return emitter;
    }

    private void randomDelay() {
          try {
                Thread.sleep(1000);
          } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
          }
    }

	/*
	 * private ExecutorService executor = Executors.newCachedThreadPool();
	 * 
	 * @GetMapping("/emitter/{id}")
	 * 
	 * public SseEmitter eventEmitter(@PathVariable Long id) {
	 * 
	 * 
	 * }
	 */
}
	
	  /* SseEmitter emitter = new SseEmitter(); //12000 here is the timeout and it is optional   
	   
	   
	
	   //create a single thread for sending messages asynchronously
	
	   //ExecutorService executor = Executors.newSingleThreadExecutor();
	
	   executor.execute(() -> {
	
	       try {
	
	           for (int i = 0; i < 20; i++) {
	        	   
	        	   SseEventBuilder event = SseEmitter.event()
	                       .data("SSE MVC - " + LocalTime.now().toString()+id)
	                       .id(String.valueOf(i))
	                       .name("sse event - mvc");
	                     emitter.send(event);
	                     Thread.sleep(2000);
	
	              //emitter.send("message" + i);   
	              
	              //------named events------//
					
					 * seEmitter emitter = new SseEmitter(); 2 SseEmitter.SseEventBuilder
					 * sseEventBuilder = SseEmitter.event() 3 .id("0") // You can give nay string as
					 * id 4 .name("customEventName") 5 .data("message1") 6 .reconnectTime(10000);
					 * //reconnect time in millis 7 emitter.send(sseEventBuilder);
					 
	           }    
	           
	           emitter.complete();
	
	       } catch(Exception e) {
	
	            emitter.completeWithError(e);       
	
	       } 
	
	   });
	
	   executor.shutdown();
	
	   return emitter;
	
	}*/


