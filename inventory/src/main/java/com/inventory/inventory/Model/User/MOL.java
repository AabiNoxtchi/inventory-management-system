package com.inventory.inventory.Model.User;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.Role;
import com.inventory.inventory.Model.Supplier;

@Entity
@DiscriminatorValue("mol")
//@Table( name = "mol")
public class MOL extends InUser{
	
	private static final long serialVersionUID = 1L;

	@OneToMany()
    @Basic(fetch = FetchType.LAZY)
	@JsonIgnore
    private List<Employee> employees;// for Mol
	
	@OneToMany(mappedBy = "mol")
    @Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Product> products;
	
	@OneToMany()
	@Basic(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Supplier> suppliers;
	
	public MOL() {}
	
	public MOL(Long id) {
		super(id);
	}

	public MOL(String userName, String encode, String string, Role molRole) {
		super(userName, encode, string,  molRole);
	}
	
	

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public List<Supplier> getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(List<Supplier> suppliers) {
		this.suppliers = suppliers;
	}
	
	
	
	
}
