package com.inventory.inventory.Model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "event_Product")
public class EventProduct extends BaseEntity{
	
	@ManyToOne(optional = false)
	@Basic(fetch = FetchType.LAZY)
	private Event event;
	
	@ManyToOne(optional = false)
	@Basic(fetch = FetchType.LAZY)
	private Product product;
	
	//***********************************//
	@ManyToOne(optional = false)
	@Basic(fetch = FetchType.LAZY)
	private User user;
	
	public EventProduct() {}
	
	public EventProduct(Event event, Long productId, Long userId) {
		this.event = event;
		this.product = new Product(productId);
		this.user = new User(userId);
	}
	
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
