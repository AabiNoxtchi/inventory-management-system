package com.inventory.inventory.ViewModels.Supplier;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Annotations.DropDownAnnotation;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.QSupplier;
import com.inventory.inventory.Model.User.QUser;
import com.inventory.inventory.ViewModels.Shared.BaseFilterVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;

public class FilterVM extends BaseFilterVM {

	@JsonIgnore
	private Long whosAskingId;

	private Boolean all;

	@DropDownAnnotation(target = "name", value = "name", name = "name", title = "name")
	private List<SelectItem> names;
	private String name;

	@DropDownAnnotation(target = "phoneNumber", value = "phoneNumber", name = "phoneNumber", title = "phone number")
	private List<SelectItem> phoneNumbers;
	private String phoneNumber;

	@DropDownAnnotation(target = "DDCnumber", value = "DDCnumber", name = "DDCnumber", title = "DDC number")
	private List<SelectItem> DDCnumbers;
	private String DDCnumber;

	@DropDownAnnotation(target = "email", value = "email", name = "email", title = "email")
	private List<SelectItem> emails;
	private String email;

	@Override
	public Predicate getPredicate() {
		
		System.out.println("phoneNumber = _"+phoneNumber);
		System.out.println("ddcnumber = _"+DDCnumber);

		Predicate predicate = null;			
		Predicate main = predicateMain();		  
		  predicate = 
				  all != null && all ? main 
						  : (
							(name == null ? Expressions.asBoolean(true).isTrue() 
							:QSupplier.supplier.name.contains(name)) 
							.and(phoneNumber == null ? Expressions.asBoolean(true).isTrue() 
							: QSupplier.supplier.phoneNumber.contains(phoneNumber))
							.and(DDCnumber == null ? Expressions.asBoolean(true).isTrue()
							:QSupplier.supplier.DDCnumber.contains(DDCnumber)) 
							.and(email == null ? Expressions.asBoolean(true).isTrue() 
							:QSupplier.supplier.email.contains(email))
							).and(main)
						  ;
		return predicate;
	}
	
	@JsonIgnore
	private Predicate predicateMain() {
		
		return whosAskingId != null ? QSupplier.supplier.user.id.eq(whosAskingId):null;						
	}

	@Override
	public void setDropDownFilters() {
	
		
		Predicate main = predicateMain();		
		dropDownFilters = new HashMap<String, Predicate>() {
			{
				put("names", main);
				put("phoneNumbers", ((BooleanExpression) main).and(QSupplier.supplier.phoneNumber.isNotNull()));
				put("DDCnumbers", main);
				put("emails", ((BooleanExpression) main).and(QSupplier.supplier.email.isNotNull()));

			}};
	}

	public Long getWhosAskingId() {
		return whosAskingId;
	}

	public void setWhosAskingId(Long whosAskingId) {
		this.whosAskingId = whosAskingId;
	}

	public Boolean getAll() {
		return all;
	}

	public void setAll(Boolean all) {
		this.all = all;
	}

	public List<SelectItem> getNames() {
		return names;
	}

	public void setNames(List<SelectItem> names) {
		this.names = names;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SelectItem> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(List<SelectItem> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<SelectItem> getDDCnumbers() {
		return DDCnumbers;
	}

	public void setDDCnumbers(List<SelectItem> dDCnumbers) {
		DDCnumbers = dDCnumbers;
	}

	public String getDDCnumber() {
		return DDCnumber;
	}

	public void setDDCnumber(String dDCnumber) {
		DDCnumber = dDCnumber;
	}

	public List<SelectItem> getEmails() {
		return emails;
	}

	public void setEmails(List<SelectItem> emails) {
		this.emails = emails;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	

	@Override
	public Predicate getFurtherAuthorizePredicate(Long id, Long userId) {
		// TODO Auto-generated method stub
		return QSupplier.supplier.user.id.eq(userId).and(QSupplier.supplier.id.eq(id));
	}

}
