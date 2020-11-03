package com.inventory.inventory.Controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TestController {
	

    @GetMapping("/test")
    public SseEmitter fetchData2() 
    {
          SseEmitter emitter = new SseEmitter((long) 0);

          ExecutorService executor = Executors.newSingleThreadExecutor();

          executor.execute(() -> 
          {
                try {
                      for (int i=0;i<50;i++) {
                    	  
                    	  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
                    	   LocalDateTime now = LocalDateTime.now();  
                    	  // System.out.println(dtf.format(now));  
                    	  
                    	  String time=dtf.format(now).toString();
                    	  String msg=i+" : "+time;
                            randomDelay();
                            emitter.send(msg);
                            System.out.println("sending emitter:"+msg);
                      }

                      emitter.complete();

                } catch (IOException e) {
                	System.out.println("error sending emitter.send ");
                      emitter.completeWithError(e);
                }
          });
          executor.shutdown();
          return emitter;
    }

    private void randomDelay() {
          try {
                Thread.sleep(5000);
          } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
          }
    }

	
}
	
	 