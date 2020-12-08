package com.inventory.inventory.Repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import com.inventory.inventory.Model.Event;
import com.inventory.inventory.Model.EventType;

@Repository
public interface EventsRepository extends BaseRepository<Event>{
	
	Optional<Event> findByName(EventType name);

}
