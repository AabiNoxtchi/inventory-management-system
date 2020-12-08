package com.inventory.inventory.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "event")
public class Event extends BaseEntity{
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20 ,nullable = false)
	private EventType name;	
	
	public Event() {}

	public Event(EventType name) {
		super();
		this.name = name;
	}

	public EventType getName() {
		return name;
	}

	public void setName(EventType name) {
		this.name = name;
	}
	
}
