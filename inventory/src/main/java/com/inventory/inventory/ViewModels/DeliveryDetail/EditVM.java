package com.inventory.inventory.ViewModels.DeliveryDetail;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inventory.inventory.Model.Delivery;
import com.inventory.inventory.Model.DeliveryDetail;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.ProductDetail;
import com.inventory.inventory.ViewModels.Shared.BaseEditVM;
import com.inventory.inventory.ViewModels.Shared.SelectItem;

public class EditVM extends BaseEditVM<DeliveryDetail>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long productId;
	
	private String productName;
	
	private int quantity;
	
	private BigDecimal pricePerOne;
	
	@JsonIgnore
	private Delivery delivery;
	
	private Long deliveryId;
	
	@JsonIgnore
	private Product product;
	
	private String[] numErrors;
	
	private List<SelectItem> productNums;
	
	private List<SelectItem> updatedProductNums;
	
	private List<Long> deletedNums;
	
	
	
	@Override
	public void populateModel(DeliveryDetail item) {
		setId(item.getId());
		productName=item.getProductName();
		productId=item.getProductId();
		quantity= item.getQuantity();
		pricePerOne= item.getPricePerOne();	
		//productNums= item.getProductDetails();
		
	}

	@Override
	public void populateEntity(DeliveryDetail item) {
		item.setId(getId());
		item.setProduct(new Product(productId));
		//item.setQuantity(quantity);
		item.setPricePerOne(pricePerOne);
		item.setDelivery((delivery !=null) ?delivery : new Delivery(deliveryId));	
		
	}


	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPricePerOne() {
		return pricePerOne;
	}

	public void setPricePerOne(BigDecimal pricePerOne) {
		this.pricePerOne = pricePerOne;
	}

	public Delivery getDelivery() {
		return delivery;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}
	
	

	public Long getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(Long deliveryId) {
		this.deliveryId = deliveryId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public String[] getNumErrors() {
		return numErrors;
	}

	public void setNumErrors(String[] numErrors) {
		this.numErrors = numErrors;
	}

	public List<SelectItem> getProductNums() {
		return productNums;
	}

	public void setProductNums(List<SelectItem> productNums) {
		this.productNums = productNums;
	}

	public List<SelectItem> getUpdatedProductNums() {
		return updatedProductNums;
	}

	public void setUpdatedProductNums(List<SelectItem> updatedProductNums) {
		this.updatedProductNums = updatedProductNums;
	}

	public List<Long> getDeletedNums() {
		return deletedNums;
	}

	public void setDeletedNums(List<Long> deletedNums) {
		this.deletedNums = deletedNums;
	}

	
	
}
