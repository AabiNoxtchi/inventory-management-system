package com.inventory.inventory.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Repository.ProductsRepository;

@Service
public class ProductsManagerService {

	@Autowired
	private ProductsRepository repo;

	public List<Long> discardProducts(Long userId) {

		List<Long> discardedProductsIdsList = null;
		
		 if(userId>0) {
		  
			  List<Product> products = repo.findByUserIdAndIsDiscarded(userId , false);
			  for(Product product : products) {			  
			  long daysLeft =
			  getTimeLeft(product.getDateCreated(),product.getYearsToDiscard());
				  if(daysLeft <= 0) {    // when left days for the product is 0 it gets discarded				  
				  product.setDiscarded(true); 
				  if(discardedProductsIdsList == null) 
					  discardedProductsIdsList = new ArrayList<Long>();				  
				  discardedProductsIdsList.add(product.getId());
				  }
			  }
		 }
			  
		return discardedProductsIdsList;
	}

	private long getTimeLeft(Date dateCreated, int lifeYears) {

		long diffInMillies = new Date().getTime() - dateCreated.getTime();
		long livedDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS); // products lived days till now
		long lifeDays = lifeYears * 365; // how many days the product is given
		long daysLeft = lifeDays - livedDays; // how many days are left for the product

		return daysLeft;

	}

}
