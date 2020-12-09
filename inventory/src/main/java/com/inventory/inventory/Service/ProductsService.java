package com.inventory.inventory.Service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Repository.BaseRepository;
import com.inventory.inventory.Repository.ProductsRepository;
import com.inventory.inventory.ViewModels.Product.EditVM;
import com.inventory.inventory.ViewModels.Product.FilterVM;
import com.inventory.inventory.ViewModels.Product.IndexVM;
import com.inventory.inventory.ViewModels.Product.OrderBy;
import com.inventory.inventory.ViewModels.Product.SelectProduct;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.inventory.inventory.auth.Models.RegisterResponse;

@Service
public class ProductsService extends BaseService<Product, FilterVM, OrderBy, IndexVM, EditVM> {
	
	private static final Logger logger = LoggerFactory.getLogger("Product Service");

	@Autowired
	private ProductsRepository repo;

	@Override
	protected BaseRepository<Product> repo() {
		return repo;
	}
	
	@Override
	protected Product newItem() {
		return new Product();
	}

	@Override
	protected FilterVM filter() {
		return new FilterVM();
	}

	@Override
	protected OrderBy orderBy() {
		return new OrderBy();
	}
	
	@Override
	protected EditVM editVM() {
		return new EditVM();
	}
	
	@Override
	public Boolean checkGetAuthorization() {
		ERole role = checkRole();
		//logger.info("role = "+role.name());
		return role.equals(ERole.ROLE_Mol) || role.equals(ERole.ROLE_Employee);
	}
	
	@Override 
	public Boolean checkSaveAuthorization() {
		ERole role = checkRole();
	   return role.equals(ERole.ROLE_Mol) ; 
    }
	
	@Override 
    public Boolean checkDeleteAuthorization() { 
		  ERole role =  checkRole(); return role.equals(ERole.ROLE_Mol) ; 
	}
	
	protected void populateModel(IndexVM model) {
		
		ERole currentUserRole = checkRole();
		
		// *** set user id to get just his products ***//
		Long id = getLoggedUser().getId();
		switch (currentUserRole) {
		case ROLE_Mol:
			model.getFilter().setUserId(id);
			break;
		case ROLE_Employee:
			model.getFilter().setEmployeeId(id);
			break;
		default:
			break;
		}

	}
	
	@Override
	protected void PopulateEditPostModel(@Valid EditVM model) {
		
		model.setUserId(getLoggedUser().getId());
		
	}	
	
	protected void dealWithEnumDropDowns(IndexVM model) {
		/*************************/
		List<SelectItem> productTypes = new ArrayList<>();
		SelectItem item = new SelectItem(ProductType.DMA.name(), ProductType.DMA.name());
		SelectItem item2 = new SelectItem(ProductType.MA.name(), ProductType.MA.name());
		productTypes.add(item);		
		productTypes.add(item2);		
		
		model.getFilter().setProductTypes(productTypes);
	}

	public List<Product> getProductsByIdIn(ArrayList<Long> ids) {

		return repo.findByIdIn(ids);
	}
	
	 @Override	 
	 protected void handleDeletingChilds(List<Product> items) { 		
	  //Auto-generated method stub	  
	 }

	@Override
	protected void handleDeletingChilds(Product e) {
		// TODO Auto-generated method stub		
	}
	
	@Transactional
	public ResponseEntity<?> nullifyEmployees(ArrayList<Long> ids) {
		List<Product> items = new ArrayList<>();
		for (Long id : ids) {
			 Product item = repo.findById(id).get();        
		       if(item == null) 
		    	   return ResponseEntity
					.badRequest()
					.body(new RegisterResponse("Error: some products not found!"));		        

		        item.setEmployee(null);
		        items.add(item);
		         
		        
		}
		repo().saveAll(items);       
		return ResponseEntity.ok(ids);
	}

	 public  ResponseEntity<?> getselectProducts() { 
		 
		 List<SelectProduct> freeProducts = 
		 
			return null; 
 
		}
}
