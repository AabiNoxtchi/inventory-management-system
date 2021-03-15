package com.inventory.inventory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.inventory.inventory.Model.Category;
import com.inventory.inventory.Model.Delivery;
import com.inventory.inventory.Model.DeliveryDetail;
import com.inventory.inventory.Model.ECondition;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.Event;
import com.inventory.inventory.Model.EventType;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.ProductDetail;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.QDelivery;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.QProductDetail;
import com.inventory.inventory.Model.QUserProfile;
import com.inventory.inventory.Model.Role;
import com.inventory.inventory.Model.SubCategory;
import com.inventory.inventory.Model.Supplier;
import com.inventory.inventory.Model.UserProfile;
import com.inventory.inventory.Model.User.QUser;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.Repository.ProductDetailRepositoryImpl;
import com.inventory.inventory.Repository.Interfaces.CategoryRepository;
import com.inventory.inventory.Repository.Interfaces.DeliveryDetailRepository;
import com.inventory.inventory.Repository.Interfaces.DeliveryRepository;
import com.inventory.inventory.Repository.Interfaces.EventsRepository;
import com.inventory.inventory.Repository.Interfaces.ProductDetailsRepository;
import com.inventory.inventory.Repository.Interfaces.ProductsRepository;
import com.inventory.inventory.Repository.Interfaces.RolesRepository;
import com.inventory.inventory.Repository.Interfaces.SubCategoryRepository;
import com.inventory.inventory.Repository.Interfaces.SuppliersRepository;
import com.inventory.inventory.Repository.Interfaces.UserProfilesRepository;
import com.inventory.inventory.Repository.Interfaces.UsersRepository;
import com.inventory.inventory.ViewModels.ProductDetail.ProductDetailDAO;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;

@Component
public class UserDataSeeder implements CommandLineRunner {
	
	@Autowired
	EventHandler handler;		

	@Autowired
	UsersRepository usersRepository;
	
//	@Autowired
//	MOLRepository molRepository;
//	
//	@Autowired
//	EmployeeRepository empRepository;
	
	@Autowired
	RolesRepository roleRepository;
	
	@Autowired
	EventsRepository eventRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	SubCategoryRepository subCategoryRepository;
	
	@Autowired
	ProductsRepository productsRepository;
	
	@Autowired
	SuppliersRepository suppliersRepository;
	
	@Autowired
	DeliveryRepository deliveryRepository;
	
	@Autowired
	DeliveryDetailRepository deliveryDetailRepository;
	
	@Autowired
	ProductDetailsRepository productDtsRepository;
	
	@Autowired
	ProductDetailRepositoryImpl productDtsRepositoryImpl;
	
	@Autowired
	UserProfilesRepository upRepo;
	
	//@Autowired
	//AvailableProductsRepository availablesRepo;
	
	@Autowired
	PasswordEncoder encoder ;
	
	@Value("${app.password}")
	private String password;
	
	List<User> usersMol ;
	Map<User, List<User>> usersEmp ;

	@Override
	public void run(String... args) throws Exception {
		loadUserData();	
		System.out.println("seeding ok");
		
		//handler.runHandler();
	}

	private void loadUserData() {
		
		if(roleRepository.count()==0) {
			roleRepository.save(new Role(ERole.ROLE_Admin));			
			roleRepository.save(new Role(ERole.ROLE_Employee));
			roleRepository.save(new Role(ERole.ROLE_Mol));
		}
		
		if(eventRepository.count()==0) {
			eventRepository.save(new Event(EventType.Discarded));			
		}
		
		
		if (usersRepository.count() == 0) {			
			
			Role adminRole = null;
			Optional<Role> opt = roleRepository.findByName(ERole.ROLE_Admin);
			if (!opt.isPresent())
				adminRole = roleRepository.save(new Role(ERole.ROLE_Admin));
			else 
				adminRole=opt.get();
		
			User user = new User("admin", encoder.encode(password), "admin123@gmail.com", adminRole);			
			usersRepository.save(user);
			
			//**************************************************  development       *********************************************************//
			Role MolRole = null;
			Optional<Role> optMol = roleRepository.findByName(ERole.ROLE_Mol);
			if (!optMol.isPresent())
				MolRole = roleRepository.save(new Role(ERole.ROLE_Mol));
			else 
				MolRole=optMol.get();		
		
			if(usersMol == null) usersMol = new ArrayList<>();
			for(int i = 0; i < 2; i++) {			
				
				String userName = "user"+i;
				User userMol = new User(userName, encoder.encode("user"+i+""+i+""+i), userName+"@gmail.com", MolRole);
				userMol = usersRepository.save(userMol);				
				usersMol.add(userMol);				
				
			}
			
			User userMol = new User("aabi", encoder.encode("aabi123"),"aabi@gmail.com", MolRole);
			userMol = usersRepository.save(userMol);
			usersMol.add(userMol);
			
		}
		
		if ( ((List<User>)usersRepository.findAll(QUser.user.role.name.eq(ERole.ROLE_Employee))).size() == 0) {
			
			if(usersMol == null) usersMol = (List<User>) usersRepository.findAll(QUser.user.role.name.eq(ERole.ROLE_Mol));
			
			Role empRole = null;
			Optional<Role> opt = roleRepository.findByName(ERole.ROLE_Employee);
			if (!opt.isPresent())
				empRole = roleRepository.save(new Role(ERole.ROLE_Employee));
			else 
				empRole=opt.get();
		
			if(usersEmp == null) usersEmp = new HashMap<>();
			for(int k = 0; k < usersMol.size(); k++) {
				
				for(int i = 0; i<5; i++) {			
					User mol = usersMol.get(k);
					String emp1stName = "emp"+i+""+mol.getId();
					String emp2ndName = "lName"+i;
					User userEmp =  new User(
							emp1stName, emp2ndName, emp1stName+" "+emp2ndName, 
							encoder.encode("emp"+i+""+i+""+i), emp1stName+"@gmail.com", empRole);
					userEmp.setMol(mol );
					
					userEmp = usersRepository.save(userEmp);
					//User mol = usersMol.get(k); 
					if(usersEmp.containsKey(mol)) usersEmp.get(mol).add(userEmp);
					else {
						List<User> emps = new ArrayList<>();
						emps.add(userEmp);
						usersEmp.put(mol, emps);
					}
				}
			}
		}
		
		if(categoryRepository.count() == 0) {
			List<SubCategory> childs = new ArrayList<>();
			
			// категория I - масивни сгради, включително инвестиционни имоти, съоръжения,
			// предавателни устройства, преносители на електрическа енергия, съобщителни линии;
			Category c1 = new Category(1, "I", 4);
			c1 = categoryRepository.save(c1);
			childs.add(new SubCategory("massive buildings", c1));
			childs.add(new SubCategory("investment property", c1));
			childs.add(new SubCategory("facilities", c1));
			childs.add(new SubCategory("transmission devices", c1));
			childs.add(new SubCategory("electricity carriers", c1));
			childs.add(new SubCategory("communication lines", c1));
			
			//категория II - машини, производствено оборудване, апаратура;
			Category c2 = new Category(2,"II", 30);
			c2 = categoryRepository.save(c2);
			childs.add(new SubCategory("machines", c2));
			childs.add(new SubCategory("production equipment", c2));
			childs.add(new SubCategory("equipment", c2));
			
			//vehicles, excluding cars; road and runway coverage;
			Category c3 = new Category(3,"III", 10);
			c3 = categoryRepository.save(c3);
			childs.add(new SubCategory("vehicles (excluding cars)", c3));
			childs.add(new SubCategory("road coverage", c3));
			childs.add(new SubCategory("runway coverage", c3));
			
			//computers, their peripherals, software and software rights, mobile phones
			Category c4 = new Category(4,"IV", 50);
			c4 = categoryRepository.save(c4);
			childs.add(new SubCategory("computers", c4));
			childs.add(new SubCategory("computer peripherals", c4));
			childs.add(new SubCategory("software", c4));
			childs.add(new SubCategory("software rights", c4));
			childs.add(new SubCategory("mobile phones", c4));
			
			//5. category V - cars;
			Category c5 = new Category(5,"V", 25);
			c5 = categoryRepository.save(c5);
			childs.add(new SubCategory("cars", c5));
			
			//category VI - tax fixed tangible and intangible assets for which there 
			//is a limited period of use according to contractual relations or legal obligation;
			Category c6 = new Category(6,"VI", 33.33);
			c6 = categoryRepository.save(c6);
			childs.add(new SubCategory("limited period DMA", c6));
			
			// all other depreciable assets
			Category c7 = new Category(7,"VII", 15);
			c7 = categoryRepository.save(c7);
			childs.add(new SubCategory("all other depreciable assets", c7));
			
			subCategoryRepository.saveAll(childs);
		}
		
		if(productsRepository.count() == 0) {
			
			if(usersMol == null) usersMol = (List<User>) usersRepository.findAll(QUser.user.role.name.eq(ERole.ROLE_Mol));
			
			List<Product> products = new ArrayList<>();
			List<SubCategory> subCategories = 
					(List<SubCategory>) subCategoryRepository.findAll();
			
			for(int i = 0; i < usersMol.size(); i++) {
				for(int k = 0; k<5 ; k++) {
					Long molId = usersMol.get(i).getId();
					String productName = "product"+k+""+molId;					
					Product p = new Product(productName,ProductType.MA);
					p.setUser(molId);					
					products.add(p);				
				}		
				
				for(int k = 5; k<11 ; k++) {
					
					String productName = "product"+k;					
					Product p = new Product(productName,  ProductType.DMA , 
							subCategories.get(k).getCategory().getAmortizationPercent() , subCategories.get(k));//??????????????
					p.setUser(usersMol.get(i).getId());					
					products.add(p);
				
				}		
			}
			
			productsRepository.saveAll(products);
			
		}
		
		if(suppliersRepository.count() == 0) {
			
			if(usersMol == null) usersMol = (List<User>) usersRepository.findAll(QUser.user.role.name.eq(ERole.ROLE_Mol));			
			List<Supplier> suppliers = new ArrayList<>();
			
			for(int i = 0; i < usersMol.size(); i++) {
				for(int k = 0; k < 4 ; k++) {
					
					User mol = usersMol.get(i);
					String name = "Supplier"+k+""+mol.getId();
					String email = name+"@gmail.com";
					
					String PhoneNumber = "0897";					
					int c = (i/10 > 0) ? 2 : 4 ;
					for(int n = 0 ; n < c ;  n++) PhoneNumber+=""+i;
					PhoneNumber += "" + k + "" + k;
					
					String ddc = "BG";
					int c2 = (i/10>0 )? 3 : 6 ;
					for(int n = 0 ; n< c2 ; n++) ddc+=""+i;					
					ddc+=""+k+""+k+""+k;	
					
					Supplier s = new Supplier(name, email, PhoneNumber, ddc );
					s.setUser(mol);
					suppliers.add(s);
				
				}				
			}
			
			suppliersRepository.saveAll(suppliers);
		}
		
		if(deliveryRepository.count() == 0) {
		
			
			List<Supplier> suppliers = suppliersRepository.findAll();
			
			for(int i = 0; i < suppliers.size(); i++) {
				
				User mol = suppliers.get(i).getUser();
				//List<ProductDetail> productDetails = new ArrayList<>();
				List<UserProfile> ups = new ArrayList<>(); 
				
				List<Product> products = (List<Product>) productsRepository
						.findAll(QProduct.product.user.id.eq(mol.getId()));
				
				for(int k = 0 ; k < 5 ; k++) {
					
					int y = 2021 - k ;
					int m = y == 2021 ?
							(k % 2 == 0) ? 1 : 2  //1, 2
									: ( i % 2 == 0) ? (k + 1)*2 /*2, 4, 6, 8, 10, 12*/
											: (k*2 + 1) ; /* 1, 3, 5, 7, 9, 11 */
					
					
					//GregorianCalendar calendar = new GregorianCalendar(y, m, 1, 10, 0, 0);				
					//Date date = calendar.getTime();
					
					LocalDate date = LocalDate.of(y, m, 1);
										
					Delivery delivery = new Delivery(suppliers.get(i), date);
					Long number = deliveryRepository.count(QDelivery.delivery.supplier.user.id.eq(mol.getId()));
					delivery.setNumber(number+1);
					//delivery.setNumber((long) (k+1));
					delivery = deliveryRepository.save(delivery);
					
					for(int c = 0; c < 2; c++)
					{
						int productIndex = k+c;//+( k + c > 2 ? 10 : 0);
						Product p = products.get(productIndex);
						String priceStr = k+c+100+"";
						BigDecimal price = new BigDecimal(priceStr);
						
						int quantity = k>0?k:1;
						//DeliveryDetail dd = new DeliveryDetail(quantity, price, delivery, p);
						DeliveryDetail dd = new DeliveryDetail( price, delivery, p);
						dd = deliveryDetailRepository.save(dd);
						
					    for(int j = 0; j < quantity ; j++) {
					    	UUID uuid = UUID.randomUUID();	    	
					    	ProductDetail pd = new ProductDetail(p.getName()+"-"+number+"-"+i, false, true, dd);
					    	//productDetails.add(pd);
					    	pd = productDtsRepository.save(pd);
					    	//GregorianCalendar calendar = new GregorianCalendar(y, m, 1, 10, 0, 0);				
							//Date date1 = calendar.getTime();
					    	LocalDate date1 = LocalDate.of(y, m, 1);
					    	UserProfile up = new UserProfile(mol, pd, date1, null) ;
					    	ups.add(up);
					    	//upRepo.save(up);
					    	
					    }		
				   }				
			    }
				
//				productDetails = productDtsRepository.saveAll(productDetails);
//				List<UserProfile> ups = new ArrayList<>(); 
//				for(ProductDetail pd : productDetails) {
//					
//					UserProfile up = new UserProfile(mol, pd, date,// ECondition conditionGiving,
//							ECondition conditionReturned) );
//					ups.add(up);
//				}
//				
			upRepo.saveAll(ups);				
				
			}			
			
		}
		
		/********************  test ********************/
		/*Predicate p =  QProductDetail.productDetail.deliveryDetail.product.user.id.eq((long) 4);
		List<ProductDetailDAO> daos = productDtsRepositoryImpl.getDAOs(p, (long)0, (long)100);//, null);
		System.out.println("dao's. size = "+daos.size());
		daos.stream().forEach(x-> System.out.println(x.toString()));*/
		//System.out.println("date = "+LocalDate.now());
		
		//productDtsRepositoryImpl.getSelectables();
		
		Predicate predicate = QProductDetail.productDetail.id.in(
				  JPAExpressions.selectFrom(QUserProfile.userProfile)
				  .where(QUserProfile.userProfile.userId.eq((long) 4)
						  .and(QUserProfile.userProfile.returnedAt.isNull()))
				  .select(QUserProfile.userProfile.productDetail.id)
				  );
		List<ProductDetail> pds = (List<ProductDetail>) productDtsRepository.findAll(predicate);
		List<SelectItem> pdsItems = productDtsRepositoryImpl.getInventoryNumbers(predicate);
		System.out.println("pds size = "+pds.size());
		System.out.println("pdsItems size = "+pdsItems.size());
		
		/***********************************************/
		
		//****************************************************  development       ************************************************************//
		
  }
	
}
		
 

		

