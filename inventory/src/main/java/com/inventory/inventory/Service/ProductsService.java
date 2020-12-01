package com.inventory.inventory.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.Employee;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.UpdatedProductResponse;
import com.inventory.inventory.Model.User;
import com.inventory.inventory.Repository.BaseRepository;
import com.inventory.inventory.Repository.ProductsRepository;
import com.inventory.inventory.ViewModels.Product.EditVM;
import com.inventory.inventory.ViewModels.Product.FilterVM;
import com.inventory.inventory.ViewModels.Product.IndexVM;
import com.inventory.inventory.ViewModels.Product.OrderBy;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.inventory.inventory.auth.Models.UserDetailsImpl;

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
		// TODO Auto-generated method stub
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
		logger.info("role = "+role.name());
		return role.equals(ERole.ROLE_Mol) || role.equals(ERole.ROLE_Employee);
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

	

	/*
	 * public ResponseEntity<UpdatedProductResponse> save(Product product) { return
	 * save(product, null);
	 * 
	 * }
	 */

	/*
	 * public ResponseEntity<UpdatedProductResponse> save(Product product, Long
	 * employeeId) {
	 * 
	 * UpdatedProductResponse updatedProductResponse = new UpdatedProductResponse();
	 * updatedProductResponse.setId(product.getId());
	 * updatedProductResponse.setProductName(product.getName());
	 * 
	 * Long userId = ((UserDetailsImpl)
	 * SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(
	 * ); product.setUser(new User(userId));
	 * 
	 * if (product.getId() != null && product.getId() > 0) { Optional<Product>
	 * existing = repo.findById(product.getId()); if (!existing.isPresent()) throw
	 * new NullPointerException("No record with that ID"); else { if
	 * (!existing.get().isDiscarded() && product.isDiscarded()) {
	 * updatedProductResponse.setDiscarded(true); } if
	 * (existing.get().getProductType() == ProductType.DMA &&
	 * product.getProductType() == ProductType.MA) {
	 * updatedProductResponse.setConvertedToMA(true); } if
	 * (existing.get().getEmployee() != null) {
	 * product.setEmployee(existing.get().getEmployee()); } }
	 * updatedProductResponse.setResponse("updated"); } else
	 * updatedProductResponse.setResponse("saved"); if (employeeId != null &&
	 * employeeId > 0) product.setEmployee(new Employee(employeeId)); else if
	 * (employeeId != null && employeeId == 0) product.setEmployee(null);
	 * repo.save(product); if (product.getEmployee() != null)
	 * updatedProductResponse.setEmployeeId(product.getEmployee().getId()); return
	 * ResponseEntity.ok(updatedProductResponse); }
	 */
	
	 /*@Override
	 *
	 * protected void handleDeletingChilds(List<Product> items) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 * 
	 */	
	
	/*
	 * @Override protected void PopulateEditGetModel(EditVM model) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 */	


}
