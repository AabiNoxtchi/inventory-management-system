package com.inventory.inventory.Events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.inventory.inventory.Model.ERole;

@Component
public class EventSender {

	Long sleep = (long) (1000 * 30);

	@Autowired
	Resources resources;	

	protected void runHandler() {

		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.execute(() -> {
			while (true) {
				delay();
				Map<Long, List<SseEmitter>> emitters = resources.getWebEmitters();

				for (Map.Entry<Long, List<SseEmitter>> e : emitters.entrySet())

					for (SseEmitter emitter : e.getValue())
						
						sendEmitter(emitter, EventType.KeepAlive.name(), EventType.KeepAlive);

				List<SseEmitter> adminEmitters = resources.getAdminEmitters();
				
				if (adminEmitters != null)
					
					for (SseEmitter emitter : adminEmitters)
						
						sendEmitter(emitter, EventType.KeepAlive.name(), EventType.KeepAlive);
			}
		});

		executor.shutdown();
	}

	protected void notifyUsers() {

		Map<Long, List<SseEmitter>> emitters = resources.getWebEmitters();
		if (emitters.size() == 0)
			return;

		for (Map.Entry<Long, List<SseEmitter>> entry : emitters.entrySet()) {
			Long userId = entry.getKey();
			List<SseEmitter> userEmitters = entry.getValue();			
			checkmsgs(userId, userEmitters);			
		}
	}
	
	protected void notifyUser(Long userId) {

		List<SseEmitter> emitters = resources.getWebEmitters(userId);
		if (emitters.size() == 0) return;
		checkmsgs(userId, emitters);			
	}

	private void checkmsgs(Long userId, List<SseEmitter> emitters) {

		ConcurrentMap<EventType, List<Long>> map = resources.getUserEvents(userId);
		
		boolean emitterDissconnected = false;		
		List<EventType> sentTypes = new ArrayList<>();
		
		if (map == null)
			return;
		
		for (Map.Entry<EventType, List<Long>> e : map.entrySet()) {
			if(emitterDissconnected) break;
			boolean sent = false;
			
			for(SseEmitter emitter : emitters) {

				if (sendEmitter(emitter, e.getValue(), e.getKey())) {
					sent = true;					
				} else {
					emitterDissconnected = true;
					break;
				}
			}
			
				if(sent) sentTypes.add(e.getKey());	
		}
		
		if(sentTypes.size() > 0)
			sentTypes.forEach(t -> map.remove(t));
		resources.setUserMap(userId, map);		
	}

	@SuppressWarnings("unchecked")
	private boolean sendEmitter(SseEmitter emitter, Object data, EventType type) {

		boolean sent = true;
		if (data != null && (!(data instanceof List && ((List<Object>) data).size() < 0))) {
			SseEmitter.SseEventBuilder event = SseEmitter.event().name(type.name().toLowerCase()).data(data);
			try {
				emitter.send(event);
			} catch (IOException e) {
				sent = false;
			}
			}

		return sent;
	}

	private void delay() {
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	public void registerClient(Long userId, SseEmitter emitter, String userName, EClient eClient) {

		resources.putInWebEmitters(userId, emitter);
		if(sendEmitter(emitter, "Welcome " + userName, EventType.Message))
			checkmsgs(userId, resources.getWebEmitters(userId));
	}	
	
	public void unregister(Long userId, SseEmitter emitter, ERole eRole) { 
		if(eRole.equals(ERole.ROLE_Admin))
			 resources.removeFromAdminEmitters(emitter);
		  resources.removeFromEmitters(userId, emitter); 
	}
	
	public void registerAdmin(SseEmitter emitter, String userName, EClient eClient) {
		
		resources.addToAdminEmitters(emitter);
		ConcurrentMap<EventType, List<Long>> events = resources.getAdminEvents();
		List<EventType> sentTypes = new ArrayList<>();
		
		for (Map.Entry<EventType, List<Long>> e : events.entrySet()) {
			if (sendEmitter(emitter, e.getValue(), e.getKey())) {				
				sentTypes.add(e.getKey());
			}			
		}
		
		if(sentTypes.size()>0)
			sentTypes.forEach(t -> resources.removeFromAdminEvents(t));
	}

	public void notifyAdmin(EventType type, Long id) {

		List<SseEmitter> emitters = resources.getAdminEmitters();
		boolean sent = false;
		for (SseEmitter emitter : emitters) {

			if (sendEmitter(emitter, id, type)) {
				
				sent = true;
			}
		}

		if (!sent)
			resources.addInAdminEvents(type, id);
	}

}




