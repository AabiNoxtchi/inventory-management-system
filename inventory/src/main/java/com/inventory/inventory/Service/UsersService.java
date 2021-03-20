package com.inventory.inventory.Service;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inventory.inventory.Model.Delivery;
import com.inventory.inventory.Model.DeliveryDetail;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.ProductDetail;
import com.inventory.inventory.Model.QDelivery;
import com.inventory.inventory.Model.QDeliveryDetail;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.QProductDetail;
import com.inventory.inventory.Model.QSupplier;
import com.inventory.inventory.Model.Supplier;
import com.inventory.inventory.Model.User.InUser;
import com.inventory.inventory.Model.User.QUser;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.Repository.Interfaces.BaseRepository;
import com.inventory.inventory.Repository.Interfaces.DeliveryDetailRepository;
import com.inventory.inventory.Repository.Interfaces.DeliveryRepository;
import com.inventory.inventory.Repository.Interfaces.ProductDetailsRepository;
import com.inventory.inventory.Repository.Interfaces.ProductsRepository;
import com.inventory.inventory.Repository.Interfaces.SuppliersRepository;
import com.inventory.inventory.Repository.Interfaces.UsersRepository;
import com.inventory.inventory.ViewModels.User.EditVM;
import com.inventory.inventory.ViewModels.User.FilterVM;
import com.inventory.inventory.ViewModels.User.IndexVM;
import com.inventory.inventory.ViewModels.User.OrderBy;
import com.inventory.inventory.auth.Models.RegisterRequest;
import com.inventory.inventory.auth.Service.UserDetailsServiceImpl;
import com.querydsl.core.types.dsl.Expressions;


@Service
public class UsersService extends BaseService<User, FilterVM, OrderBy, IndexVM, EditVM>{
	
	@Autowired
	private UsersRepository repo;
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	
	
	@Autowired
	ProductsRepository productsRepository;
	
	@Autowired
	DeliveryDetailRepository ddRepo;
	
	@Autowired
	DeliveryRepository dsRepo;
	
	@Autowired
	SuppliersRepository sRepo;
	
	@Autowired
	ProductDetailsRepository ProductDtsRepo;
	
	//@Autowired
	//AvailableProductsRepository availablesRepo;

	@Override
	protected BaseRepository<User> repo() {
		return repo;
	}

	@Override
	protected User newItem() {
		return new User();
	}

	@Override
	protected FilterVM filter() {
		return new FilterVM();
	}

	@Override
	protected EditVM editVM() {
		return new EditVM();
	}

	@Override
	protected OrderBy orderBy() {
		return new OrderBy();
	}
	
	@Override
	public Boolean checkGetAuthorization() {
		
		return checkRole().equals(ERole.ROLE_Admin) || checkRole().equals(ERole.ROLE_Mol);
	}

	@Override
	public Boolean checkSaveAuthorization() {
		
		return checkRole().equals(ERole.ROLE_Admin) || checkRole().equals(ERole.ROLE_Mol);
	}

	@Override
	public Boolean checkDeleteAuthorization() {
		
		return checkRole().equals(ERole.ROLE_Admin) || checkRole().equals(ERole.ROLE_Mol);
	}
	
	@Override
	protected void populateModel(IndexVM model) {
		
		model.getFilter().setWhosAskingRole(checkRole());
		model.getFilter().setWhosAskingId(getLoggedUser().getId());
	}

	@Override
	protected void populateEditPostModel(@Valid EditVM model) {
	}
	
	public ResponseEntity<?> save(EditVM model){
		return userDetailsService.signup(model.registerRequest());
	}
	
	public ResponseEntity<?> signup(RegisterRequest model){
		return userDetailsService.signup(model);
	}
	
	

	@Override
	protected void handleDeletingChilds(List<User> items) {
		
		for(User u : items) {
			handleDeletingChilds(u);
		}
	}

	@Override
	protected void handleDeletingChilds(User e) {	
		
		
		if(e.getRole().getName().equals(ERole.ROLE_Mol)) {			
			
			List<ProductDetail> productDetails = (List<ProductDetail>)
					ProductDtsRepo.findAll(
							/*(QProductDetail.productDetail.user.mol.id.eq(e.getId()))
							.or(QProductDetail.productDetail.user.id.eq(e.getId()))*/
							QProductDetail.productDetail.deliveryDetail.product.userCategory.user.id.eq(e.getId())
							);
			//productDetailRepo.deleteAll(productDetails); //1
			
			List<User> emps = (List<User>) 
					repo.findAll(QUser.user.mol.id.eq(e.getId()));
			//repo.deleteAll(emps); //2
			
			List<DeliveryDetail> ddsList = (List<DeliveryDetail>) ddRepo
					.findAll(QDeliveryDetail.deliveryDetail.product.userCategory.user.id.eq(e.getId()));
					//.findAll(QDeliveryDetail.deliveryDetail.delivery.supplier.mol.id.eq(e.getId()));
			//ddRepo.deleteAll(ddsList); //3
			
			//List<AvailableProduct> availables = 
					//(List<AvailableProduct>) availablesRepo.findAll(QAvailableProduct.availableProduct.product.user.id.eq(e.getId()));//4
			
			ProductDtsRepo.deleteAll(productDetails); //1
			repo.deleteAll(emps); //2
			ddRepo.deleteAll(ddsList); //3
			
			List<Delivery> dsList = (List<Delivery>) dsRepo
					.findAll(QDelivery.delivery.supplier.user.id.eq(e.getId()));//****************** ?????????????????
			dsRepo.deleteAll(dsList);
			
			List<Supplier> sList = (List<Supplier>) sRepo
					.findAll(QSupplier.supplier.user.id.eq(e.getId()));
			sRepo.deleteAll(sList);
			
			//availablesRepo.deleteAll(availables);
			
			List<Product> products = (List<Product>) productsRepository.findAll(QProduct.product.userCategory.user.id.eq(e.getId()));
			productsRepository.deleteAll(products);	
		}
		
		
		if(e.getRole().getName().equals(ERole.ROLE_Employee)) {
			
			/*List<ProductDetail> productDetails = (List<ProductDetail>) ProductDtsRepo
					.findAll(QProductDetail.productDetail.user.id.eq(e.getId()));
			
			System.out.println("product2.size = "+productDetails.size());
			
			if(productDetails.size() > 0) {
				for(ProductDetail productD : productDetails)
				{
					Long LoggedUserId = getLoggedUser().getId();
					productD.setUser(new InUser(LoggedUserId));
				}
			}
			
			ProductDtsRepo.saveAll(productDetails);		*/
		
		}
	}

	@Override
	protected void populateEditGetModel(EditVM model) {
		// TODO Auto-generated method stub
		
	}
	
}
