package com.inventory.inventory.ViewModels.UserProfiles;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Annotations.DropDownAnnotation;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.QProductDetail;
import com.inventory.inventory.Model.QProfileDetail;
import com.inventory.inventory.Model.QUserProfile;
import com.inventory.inventory.Model.User.QEmployee;
import com.inventory.inventory.Model.User.QUser;
import com.inventory.inventory.ViewModels.Shared.BaseFilterVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

public class FilterVM extends BaseFilterVM{

	private Boolean all;
	
	@JsonIgnore
	private Long whoseAskingId;
	
	private boolean current;
	
	private boolean allUser;
	
	private boolean withDetail;
	
	@DropDownAnnotation(target = "userId", value = "user.id", name = "user.userName", title = "user name")
	private List<SelectItem> userNames;
	private Long userId;
	
	private Boolean myProfile;
	
	@JsonIgnore
	private ERole eRole;
	
	@DropDownAnnotation(target = "productDetailId", value = "productdetail.id", name = "productdetail.inventoryNumber",
			title = "inventory number", filterBy="productdetail.productId")
	private List<SelectItem> inventoryNumbers;
	private Long productDetailId;
	
	
	@DropDownAnnotation(target = "productId", value = "product.id", name = "product.name", title = "product")
	private List<SelectItem> productNames;
	private Long productId;
	//private String productName; // formula col
	
	//private Boolean current;
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate givenAfter;
	@DateTimeFormat(iso = ISO.DATE)
    private LocalDate returnedBefore;
	
	@Override
	public Predicate getPredicate() {	
		//Predicate usersForMol = QUserProfile.userProfile.user.mol.id.eq(userId);	
		//System.out.println("userId = "+userId);
		//System.out.println("whoseAskingId = "+whoseAskingId);
		
		//QUserProfile u = QUserProfile.userProfile;
		//QUserProfile.userProfile.user e = u.as(QEmployee.class); 
		
		//QUserProfile profile = new QUserProfile("UserProfile");
		//QEmployee employee = profile.user.as(QEmployee.class);
		
		//QUser employee = new QEmployee("Employee");//.as(QEmployee.class);
		//QEmployee q = u.as(QEmployee.class); 
		
		//QUserProfile employee = QUserProfile.userProfile.user.as(q);
		//QEmployee employee = user.as(QEmployee.class);
		
		//QUserProfile profile = new QUserProfile("UserProfile");
		//QEmployee employee = profile.user.as(QEmployee.class);
		
		JPQLQuery<Long> usersIds = JPAExpressions.selectFrom(QEmployee.employee).where(QEmployee.employee.mol.id.eq(whoseAskingId)).select(QEmployee.employee.id);
		Predicate molUser =  
				userId != null  ? 
						//for specific employee
						//employee.mol.id.eq(whoseAskingId)
						//.and(
						QUserProfile.userProfile.user.id.eq(userId)
						//)
						: myProfile != null && myProfile ? 
								//mol profile
							QUserProfile.userProfile.user.id.eq(whoseAskingId)
							: allUser ? // just users 
									QUserProfile.userProfile.userId.in(usersIds)//employee.mol.id.eq(whoseAskingId)
							://all profiles in inventory for mol + employees
								QUserProfile.userProfile.user.id.eq(whoseAskingId)
						.or(        QUserProfile.userProfile.userId.in(usersIds));
							//	employee.mol.isNotNull()
								//.and(employee.mol.id.eq(whoseAskingId)));
		
		Predicate users = QUserProfile.userProfile.user.id.eq(whoseAskingId) ;//Expressions.asBoolean(true).isTrue() //QUserProfile.userProfile.user.as(QEmployee.class).mol.isNotNull()
				//.and(QUserProfile.userProfile.user.id.eq(whoseAskingId));
		//Predicate pds = productDetailId == null ? Expressions.asBoolean(true).isTrue()
			//	: QUserProfile.userProfile.productDetail.id.eq(productDetailId);
		
				
					//Predicate predicate = 
		return ((BooleanExpression) (eRole.equals(ERole.ROLE_Mol) ? 
								molUser : users))
				.and(productDetailId == null ? Expressions.asBoolean(true).isTrue()
						: QUserProfile.userProfile.productDetail.id.eq(productDetailId))
				//.and(productName == null ? Expressions.asBoolean(true).isTrue()
					//	: QUserProfile.userProfile.productName.eq(productName))
				.and(productId == null ? Expressions.asBoolean(true).isTrue()
							: QUserProfile.userProfile.productDetail.deliveryDetail.productId.eq(productId))
				.and(givenAfter == null ? Expressions.asBoolean(true).isTrue() : 
					QUserProfile.userProfile.givenAt.after(givenAfter))
				.and(returnedBefore == null ? Expressions.asBoolean(true).isTrue() : 
					QUserProfile.userProfile.returnedAt.before(returnedBefore))
				.and(current ? 
					QUserProfile.userProfile.returnedAt.isNull()
					:Expressions.asBoolean(true).isTrue())
				.and(withDetail ? 
						//QUserProfile.userProfile.profileDetail.isNotNull()
						 QUserProfile.userProfile.id.in(JPAExpressions.selectFrom(QProfileDetail.profileDetail).select(QProfileDetail.profileDetail.id))
						:Expressions.asBoolean(true).isTrue())
//				.and(allUser ? 
//						QUserProfile.userProfile.user.id.eq(whoseAskingId).isFalse()
//						:Expressions.asBoolean(true).isTrue())
						;
					//System.out.println("predicate = "+predicate);
					//return  predicate;
	}

	@Override
	public void setDropDownFilters() {
//		System.out.println("whoseAskingId = "+whoseAskingId);
//		System.out.println("eRole.equals(ERole.ROLE_Mol) = "+(eRole.equals(ERole.ROLE_Mol)));
//		//Predicate toPrint1 = QProductDetail.productDetail.deliveryDetail.pr
//		Predicate toPrint = QProductDetail.productDetail.deliveryDetail.product.userCategory.userId.eq((long) 4);//QProductDetail.productDetail.deliveryDetail.product.userCategory.user.id.eq((long) 4);
//		System.out.println("predicate toPrint = "+toPrint);
		Predicate userNames = 
				eRole.equals(ERole.ROLE_Mol) ? QUser.user.as(QEmployee.class).mol.isNotNull()
						.and(QUser.user.as(QEmployee.class).mol.id.eq(whoseAskingId))
						: null;
				Predicate productNames = 
						eRole.equals(ERole.ROLE_Mol) ? 
								QProduct.product.userCategory.userId.eq(whoseAskingId)
								: eRole.equals(ERole.ROLE_Employee) ? 
										QProduct.product.userCategory.userId.eq(
										JPAExpressions.
										selectFrom(QUser.user)
										.where(QUser.user.id.eq(whoseAskingId))
										.select(QUser.user.as(QEmployee.class).mol.id)) : null;
				Predicate inventoryNumbers = 
						eRole.equals(ERole.ROLE_Mol) ? 
						QProductDetail.productDetail.deliveryDetail.product.userCategory.userId.eq(whoseAskingId)
						: eRole.equals(ERole.ROLE_Employee) ? 
								QProductDetail.productDetail.id.in
								(
										JPAExpressions.selectFrom(QUserProfile.userProfile)
										.where(QUserProfile.userProfile.user.id.eq(whoseAskingId))
										.distinct()
										.select(QUserProfile.userProfile.productDetail.id)
										
								)
								:null;
						
				dropDownFilters = new HashMap<String, Predicate>() {
					{
						put("userNames", userNames);
						put("productNames", productNames);
						put("inventoryNumbers", inventoryNumbers);
						
					}};
		
	}

	@Override
	public Boolean getAll() {
		return all;
	}

	@Override
	public void setAll(Boolean all) {
		this.all = all;
		
	}

	public List<SelectItem> getUserNames() {
		return userNames;
	}

	public void setUserNames(List<SelectItem> userNames) {
		this.userNames = userNames;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<SelectItem> getInventoryNumbers() {
		return inventoryNumbers;
	}

	public void setInventoryNumbers(List<SelectItem> inventoryNumbers) {
		this.inventoryNumbers = inventoryNumbers;
	}

	public Long getProductDetailId() {
		return productDetailId;
	}

	public void setProductDetailId(Long productDetailId) {
		this.productDetailId = productDetailId;
	}
	
	public void seteRole(ERole eRole) {
		this.eRole = eRole;
	}

	public Long getWhoseAskingId() {
		return whoseAskingId;
	}

	public void setWhoseAskingId(Long whoseAskingId) {
		this.whoseAskingId = whoseAskingId;
	}

//	public String getProductName() {
//		return productName;
//	}
//
//	public void setProductName(String productName) {
//		this.productName = productName;
//	}

	public List<SelectItem> getProductNames() {
		return productNames;
	}

	public void setProductNames(List<SelectItem> productNames) {
		this.productNames = productNames;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Boolean getMyProfile() {
		return myProfile;
	}

	public void setMyProfile(Boolean myProfile) {
		this.myProfile = myProfile;
	}

	public LocalDate getGivenAfter() {
		return givenAfter;
	}

	public void setGivenAfter(LocalDate givenAfter) {
		this.givenAfter = givenAfter;
	}

	public LocalDate getReturnedBefore() {
		return returnedBefore;
	}

	public void setReturnedBefore(LocalDate returnedBefore) {
		this.returnedBefore = returnedBefore;
	}

	public boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}

	public boolean isAllUser() {
		return allUser;
	}

	public void setAllUser(boolean allUser) {
		this.allUser = allUser;
	}

	public boolean isWithDetail() {
		return withDetail;
	}

	public void setWithDetail(boolean withDetail) {
		this.withDetail = withDetail;
	}

	

}
