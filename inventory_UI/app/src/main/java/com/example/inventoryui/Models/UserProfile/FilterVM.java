package com.example.inventoryui.Models.UserProfile;

import com.example.inventoryui.Annotations.CheckBoxAnnotation;
import com.example.inventoryui.Annotations.DateAnnotation;
import com.example.inventoryui.Annotations.DropDownAnnotation;
import com.example.inventoryui.Annotations.SkipAnnotation;
import com.example.inventoryui.Models.Shared.BaseFilterVM;
import com.example.inventoryui.Models.Shared.SelectItem;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterVM extends BaseFilterVM {

	@CheckBoxAnnotation(title="all", target = "all")
	private Boolean all;

	@CheckBoxAnnotation(title="current", target = "current")
	private boolean current;

	@CheckBoxAnnotation(title="users", target = "allUser")
	private boolean allUser;

	@CheckBoxAnnotation(title="with owings", target = "withDetail")
	private boolean withDetail;
	
	@DropDownAnnotation(target = "userId", value = "user.id", name = "user.userName", title = "user name", filterBy = "deleted")
	private List<SelectItem> userNames;
	@SkipAnnotation
	private Long userId;

	@CheckBoxAnnotation(title="my profile", target = "myProfile")
	private Boolean myProfile;

	@DropDownAnnotation(target = "productDetailId", value = "productdetail.id", name = "productdetail.inventoryNumber",
			title = "inventory number", filterBy="productdetail.productId")
	private List<SelectItem> inventoryNumbers;
	@SkipAnnotation
	private Long productDetailId;
	
	@DropDownAnnotation(target = "productId", value = "product.id", name = "product.name", title = "product")
	private List<SelectItem> productNames;
	@SkipAnnotation
	private Long productId;

	@DateAnnotation(target="givenafter",title="given After")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonAlias({"givenAfter"})
	private Date givenafter;

	@DateAnnotation(target="returnedbefore",title="returned Before")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonAlias({"returnedBefore"})
    private Date returnedbefore;

	@SkipAnnotation
	private Map<String,Object> urlParameters;

	@Override
	public Map<String, Object> getUrlParameters() {
		if(urlParameters==null)
			urlParameters = new HashMap<>();
		return urlParameters;
	}

	@Override
	public void setUrlParameters(Map<String, Object> urlParameters) {
		this.urlParameters = urlParameters;
	}

	@Override
	public Boolean getAll() {
		return all;
	}

	@Override
	public void setAll(Boolean all) {
		this.all = all;
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

	public Boolean getMyProfile() {
		return myProfile;
	}

	public void setMyProfile(Boolean myProfile) {
		this.myProfile = myProfile;
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

	public Date getGivenafter() {
		return givenafter;
	}

	public void setGivenafter(Date givenafter) {
		this.givenafter = givenafter;
	}

	public Date getReturnedbefore() {
		return returnedbefore;
	}

	public void setReturnedbefore(Date returnedbefore) {
		this.returnedbefore = returnedbefore;
	}
}


