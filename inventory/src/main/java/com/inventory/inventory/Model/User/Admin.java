package com.inventory.inventory.Model.User;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("admin")
//@Table( name = "admin")
public class Admin extends User{

	public Admin() {}
}
