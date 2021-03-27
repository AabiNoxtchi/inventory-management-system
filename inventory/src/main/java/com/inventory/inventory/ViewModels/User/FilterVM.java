package com.inventory.inventory.ViewModels.User;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Annotations.DropDownAnnotation;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.User.QUser;
import com.inventory.inventory.ViewModels.Shared.BaseFilterVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;

public class FilterVM extends BaseFilterVM {

	private ERole whosAskingRole;
	private Long whosAskingId;

	private Boolean all;

	@DropDownAnnotation(target = "firstName", value = "firstName", name = "firstName", title = "first name")
	private List<SelectItem> firstNames;
	private String firstName;

	@DropDownAnnotation(target = "lastName", value = "lastName", name = "lastName", title = "last name")
	private List<SelectItem> lastNames;
	private String lastName;

	@DropDownAnnotation(target = "userName", value = "userName", name = "userName", title = "user name")
	private List<SelectItem> userNames;
	private String userName;

	@DropDownAnnotation(target = "email", value = "email", name = "email", title = "email")
	private List<SelectItem> emails;
	private String email;

	@Override
	public Predicate getPredicate() {

		Predicate predicate = null;	
		
		Predicate main = predicateMain();
		  
		  predicate = 
				  all != null && all ? main 
						  : (
							(firstName == null ? Expressions.asBoolean(true).isTrue() 
							:QUser.user.firstName.contains(firstName)) 
							.and(lastName == null ? Expressions.asBoolean(true).isTrue() 
							: QUser.user.lastName.contains(lastName))
							.and(userName == null ? Expressions.asBoolean(true).isTrue()
							:QUser.user.userName.contains(userName)) 
							.and(email == null ? Expressions.asBoolean(true).isTrue() 
							: QUser.user.email.contains(email))
							).and(main)
						  ;

		return predicate;

	}
	
	@JsonIgnore
	private Predicate predicateMain() {
		return 
				whosAskingRole.equals(ERole.ROLE_Admin) ? QUser.user.erole.eq(ERole.ROLE_Mol) 
				: whosAskingRole.equals(ERole.ROLE_Mol) && whosAskingId != null ? 
						//Expressions.asBoolean(true).isTrue() 
						Expressions.numberTemplate(Long.class, "COALESCE({0},{1})", QUser.user.mol.id, 0).eq(whosAskingId)
						: null;
	}

	@Override
	public void setDropDownFilters() {
		
		System.out.println(" whosAskingRole.equals(ERole.ROLE_Mol) = "+ whosAskingRole.equals(ERole.ROLE_Mol));
		System.out.println(" whosAskingId != null = "+ whosAskingId != null);		
		System.out.println(" whosAskingRole.equals(ERole.ROLE_Mol) && whosAskingId != null = "+
				(whosAskingRole.equals(ERole.ROLE_Mol) && whosAskingId != null));
		
		Predicate main = predicateMain();
		//t(Expressions.numberTemplate
			//		( Integer.class , "FLOOR((TO_DAYS({0})-TO_DAYS({1}))/365)",date, QProduct.product.dateCreated ))
		//Predicate notNull = ((BooleanExpression) main).and(QUser.user.firstName.isNull().eq(false));
		System.out.println("predicateMain = "+ main);

		dropDownFilters = new HashMap<String, Predicate>() {
			{
				put("userNames", main);
				put("firstNames", ((BooleanExpression) main).and(QUser.user.firstName.isNotNull()));
				put("lastNames", ((BooleanExpression) main).and(QUser.user.lastName.isNotNull()));
				put("emails", main);

			}};
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonIgnore
	public ERole getWhosAskingRole() {
		return whosAskingRole;
	}

	@JsonIgnore
	public void setWhosAskingRole(ERole whosAskingRole) {
		this.whosAskingRole = whosAskingRole;
	}

	public Boolean getAll() {
		return all;
	}

	public void setAll(Boolean all) {
		this.all = all;
	}

	public List<SelectItem> getUserNames() {
		return userNames;
	}

	public void setUserNames(List<SelectItem> userNames) {
		this.userNames = userNames;
	}

	public List<SelectItem> getFirstNames() {
		return firstNames;
	}

	public void setFirstNames(List<SelectItem> firstNames) {
		this.firstNames = firstNames;
	}

	public List<SelectItem> getLastNames() {
		return lastNames;
	}

	public void setLastNames(List<SelectItem> lastNames) {
		this.lastNames = lastNames;
	}

	public List<SelectItem> getEmails() {
		return emails;
	}

	public void setEmails(List<SelectItem> emails) {
		this.emails = emails;
	}

	@JsonIgnore
	public Long getWhosAskingId() {
		return whosAskingId;
	}

	@JsonIgnore
	public void setWhosAskingId(Long whosAskingId) {
		this.whosAskingId = whosAskingId;
	}

}
