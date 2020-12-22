package com.inventory.inventory;

import java.math.BigDecimal;
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
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.Event;
import com.inventory.inventory.Model.EventType;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.ProductDetail;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.Role;
import com.inventory.inventory.Model.SubCategory;
import com.inventory.inventory.Model.Supplier;
import com.inventory.inventory.Model.User.Employee;
import com.inventory.inventory.Model.User.InUser;
import com.inventory.inventory.Model.User.MOL;
import com.inventory.inventory.Model.User.QUser;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.Repository.CategoryRepository;
import com.inventory.inventory.Repository.DeliveryDetailRepository;
import com.inventory.inventory.Repository.DeliveryRepository;
import com.inventory.inventory.Repository.EmployeeRepository;
import com.inventory.inventory.Repository.EventsRepository;
import com.inventory.inventory.Repository.MOLRepository;
import com.inventory.inventory.Repository.ProductDetailRepository;
import com.inventory.inventory.Repository.ProductsRepository;
import com.inventory.inventory.Repository.RolesRepository;
import com.inventory.inventory.Repository.SubCategoryRepository;
import com.inventory.inventory.Repository.SuppliersRepository;
import com.inventory.inventory.Repository.UsersRepository;

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
	ProductDetailRepository productDetailRepository;
	
	@Autowired
	PasswordEncoder encoder ;
	
	@Value("${app.password}")
	private String password;
	
	List<User> usersMol ;//= new ArrayList<>();
	Map<User, List<User>> usersEmp ;

	@Override
	public void run(String... args) throws Exception {
		loadUserData();	
		System.out.println("seeding ok");
		handler.runHandler();
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
			for(int i = 0; i < 10; i++) {			
				
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
					
					String empName = "emp"+k+""+i;
					User userEmp =  new User(empName, encoder.encode("emp"+i+""+i+""+i), empName+"@gmail.com", empRole);
					userEmp.setUser_mol( usersMol.get(k).getId());
					
					userEmp = usersRepository.save(userEmp);
					User mol = usersMol.get(k); 
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
				for(int k = 0; k<10 ; k++) {
					
					String productName = "product"+k;
					
					Product p = new Product(productName,ProductType.MA);
					p.setMol(usersMol.get(i).getId());
					products.add(p);
				
				}		
				
				for(int k = 10; k<subCategories.size() ; k++) {
					
					String productName = "product"+k;
					
					Product p = new Product(productName,  ProductType.DMA , 
							subCategories.get(k).getCategory().getAmortizationPercent() , subCategories.get(k));
					p.setMol(usersMol.get(i).getId());
					products.add(p);
				
				}		
			}
			
			productsRepository.saveAll(products);
			List<Product> products2 = productsRepository.findAll();
			System.out.println("products2.size = "+products2.size());
			User user = products2.get(0).getMol();
			System.out.println("user = "+user.getUserName());
			
		}
		
		
		if(suppliersRepository.count() == 0) {
			
			if(usersMol == null) usersMol = (List<User>) usersRepository.findAll(QUser.user.role.name.eq(ERole.ROLE_Mol));
			
			List<Supplier> suppliers = new ArrayList<>();
			for(int i = 0; i < usersMol.size(); i++) {
				for(int k = 0; k<5 ; k++) {
					//Supplier(String name, String email, String phoneNumber, String dDCnumber)
					String name = "Supplier"+i+""+k;
					String email = name+"@gmail.com";
					System.out.println("name = "+name+" email = "+email);
					
					// 0897 xx xx kk 
					String PhoneNumber = "0897";
					int c = (i/10 > 0) ? 2 : 4 ;
					System.out.println("c = "+c);
					for(int n = 0 ; n < c ;  n++) PhoneNumber+=""+i;
					System.out.println("phone number = "+PhoneNumber);
					PhoneNumber += "" + k + "" + k;
					System.out.println("phone number = "+PhoneNumber);
					
					//BG123456789
					//BGixixixkkk
					String ddc = "BG";
					int c2 = (i/10>0 )? 3 : 6 ;
					System.out.println("c2 = "+c2);
					for(int n = 0 ; n< c2 ; n++) ddc+=""+i;
					System.out.println("ddc = "+ddc);
					ddc+=""+k+""+k+""+k;
					
					Supplier s = new Supplier(name, email, PhoneNumber, ddc );
					s.setMol(usersMol.get(i).getId());
					suppliers.add(s);
				
				}				
			}
			
			suppliersRepository.saveAll(suppliers);
		}
		
//		if(deliveryRepository.count() == 0) {
//			List<Delivery> deliveries = new ArrayList<>();
//			List<Supplier> suppliers = suppliersRepository.findAll();
//			
//			for(int i = 0; i < suppliers.size(); i++) {
//				for(int k = 0 ; k < 5 ; k++) {
//					
//					int m = (i%2==0) ?  k : k * 2 ;
//					
//					GregorianCalendar calendar = new GregorianCalendar(2020, m, 1, 10, 0, 0);				
//					Date date = calendar.getTime();
//					
//					Delivery delivery = new Delivery(suppliers.get(i), date);
//					deliveries.add(delivery);				
//					
//				}				
//			}
//			
//			deliveryRepository.saveAll(deliveries);
//		}
		
		if(deliveryRepository.count() == 0) {
			
		
			List<ProductDetail> productDetails = new ArrayList<>();
			List<Supplier> suppliers = suppliersRepository.findAll();
			
			
			for(int i = 0; i < suppliers.size(); i++) {
				
				User mol = suppliers.get(i).getMol();
				//MOL mol = (MOL) user;
				
				List<Product> products = (List<Product>) productsRepository
						.findAll(QProduct.product.mol.id.eq(suppliers.get(i).getMol().getId()));
				
				for(int k = 0 ; k < 5 ; k++) {
					
					int m = (i%2==0) ?  k : k * 2 ;
					
					GregorianCalendar calendar = new GregorianCalendar(2020, m, 1, 10, 0, 0);				
					Date date = calendar.getTime();
					
					Delivery delivery = new Delivery(suppliers.get(i), date);
					delivery = deliveryRepository.save(delivery);
					
					for(int c = 0; c < 2; c++)
					{
					//DeliveryDetail(int quantity, @DecimalMin(value = "0.0", inclusive = false) BigDecimal price, Delivery delivery,
					//Product product)
						int productIndex = k+c;
						Product p = products.get(productIndex);
						String priceStr = k+c+100+"";
						BigDecimal price = new BigDecimal(priceStr);
						int quantity = k+c + 5;
						DeliveryDetail dd = new DeliveryDetail(quantity, price, delivery, p);
						dd = deliveryDetailRepository.save(dd);
					   // deliveryDetails.add(dd);
					    for(int j = 0; j < quantity ; j++) {
					    	//ProductDetail(String inventoryNumber, boolean isDiscarded, boolean isAvailable,
							//DeliveryDetail deliveryDetail, InUser inUser)
					    	
					    	UUID uuid = UUID.randomUUID();					    	
					    	
					    	ProductDetail pd = new ProductDetail(uuid.toString(), false, true, dd, mol);
					    	productDetails.add(pd);
					    	
					    }		
					
				   }				
			    }
			}
			
				productDetailRepository.saveAll(productDetails);
		}
		
		
		
		
		//****************************************************  development       ************************************************************//
		
  }
	
}
		
 

		

