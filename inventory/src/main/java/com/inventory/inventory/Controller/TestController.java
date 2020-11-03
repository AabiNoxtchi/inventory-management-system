package com.inventory.inventory.Controller;

import java.io.IOException;
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
          SseEmitter emitter = new SseEmitter();

          ExecutorService executor = Executors.newSingleThreadExecutor();

          executor.execute(() -> 
          {
                try {
                      for (int i=0;i<7;i++) {

                            randomDelay();
                            emitter.send(i);
                            System.out.println("sending emitter.send ");
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
                Thread.sleep(2000);
          } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
          }
    }

	
}
	
	 