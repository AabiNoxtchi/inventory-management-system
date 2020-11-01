package com.inventory.inventory.Model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "roles")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column(length = 20 ,nullable = false)	
	private ERole name;
	

	public Role() {

	}

	public Role(ERole name) {
		this.name = name;
		System.out.println(this.name +" + "+name);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public ERole getName() {
		return name;
	}

	public void setName(ERole name) {
		this.name = name;
	}
	

}
