package com.inventory.inventory.Events;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.joda.money.CurrencyUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.inventory.inventory.Events.EventHandler;
import com.inventory.inventory.Model.Category;
import com.inventory.inventory.Model.City;
import com.inventory.inventory.Model.Country;
import com.inventory.inventory.Model.Delivery;
import com.inventory.inventory.Model.DeliveryDetail;
import com.inventory.inventory.Model.ECondition;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.ProductDetail;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.QDelivery;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.QUserCategory;
import com.inventory.inventory.Model.Supplier;
import com.inventory.inventory.Model.UserCategory;
import com.inventory.inventory.Model.UserProfile;
import com.inventory.inventory.Model.User.Employee;
import com.inventory.inventory.Model.User.MOL;
import com.inventory.inventory.Model.User.QUser;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.Repository.ProductDetailRepositoryImpl;
import com.inventory.inventory.Repository.UserProfileRepositoryImpl;
import com.inventory.inventory.Repository.Interfaces.CategoryRepository;
import com.inventory.inventory.Repository.Interfaces.CountryRepository;
import com.inventory.inventory.Repository.Interfaces.DeliveryDetailRepository;
import com.inventory.inventory.Repository.Interfaces.DeliveryRepository;
import com.inventory.inventory.Repository.Interfaces.ProductDetailsRepository;
import com.inventory.inventory.Repository.Interfaces.ProductsRepository;
import com.inventory.inventory.Repository.Interfaces.SuppliersRepository;
import com.inventory.inventory.Repository.Interfaces.UserCategoryRepository;
import com.inventory.inventory.Repository.Interfaces.UserProfilesRepository;
import com.inventory.inventory.Repository.Interfaces.UsersRepository;

@Component
public class UserDataSeeder implements CommandLineRunner {
	
	@Autowired
	EventHandler handler;		

	@Autowired
	UsersRepository usersRepository;	
	
	@Autowired
	CountryRepository countryRepo;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	UserCategoryRepository userCategoryRepository;
	
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
	
	@Autowired
	UserProfileRepositoryImpl upRepoImpl;	
	
	@Autowired
	PasswordEncoder encoder ;
	
	@Value("${app.password}")
	private String password;
	
	List<User> usersMol ;
	List<User> emps = new ArrayList<>();
	Country BG;

	@Override
	public void run(String... args) throws Exception {
		
		loadUserData();	
		System.out.println("seeding ok");		
		handler.runHandler();
	}

	private void loadUserData() {		

		if(countryRepo.count() == 0) {
			Map<String, Country> countries = new HashMap<String, Country>(); 
			
			 String[] isoCountries = Locale.getISOCountries();
		     for (String country : isoCountries) {
		    	 Locale locale = new Locale("en", country);
		         String name = locale.getDisplayCountry();
		         		         
		         Country c = new Country((long) -1, country, name);
		         if(country.equals("BG")) {		        	 
		        	 c.addCity(new City("Varna", "EET"));
						c.addCity(new City("Sofia", "EET"));
						BG = c;
		         }else
		        	 countries.put(country, c);
		         }
		     
		     List<CurrencyUnit> currencyUnits = CurrencyUnit.registeredCurrencies();
		     for(CurrencyUnit cu : currencyUnits) {
		    	 for(String code : cu.getCountryCodes()) {		    		 
		    		 if(countries.get(code) != null) countries.get(code).setCurrency(cu+"");
		    		 else if(code.equals("BG"))BG.setCurrency(cu+"");
		    	 }
		     }
		     
		  		for (String cc : PhoneNumberUtil.getInstance().getSupportedRegions()) {					
		            int phoneCode = PhoneNumberUtil.getInstance().getCountryCodeForRegion(cc);
		            if(countries.get(cc) != null) {
		            	countries.get(cc).setPhoneCode("+"+phoneCode+"");
		           } else if(cc.equals("BG")) {
		    			 BG.setPhoneCode("+"+phoneCode+"");
		    		 }		            
		         }	
		     countryRepo.saveAll(countries.values());
		     BG = countryRepo.save(BG);
		   }	
		
		if (usersRepository.count() == 0) {			
			

			ERole adminRole = ERole.ROLE_Admin;		
			User user = new User("admin", encoder.encode(password), "admin123@gmail.com", adminRole);			
			usersRepository.save(user);
			
			//**************************************************  development       *********************************************************//

			ERole MolRole = ERole.ROLE_Mol;
			City city = BG.getCities().get(0);
			if(usersMol == null) usersMol = new ArrayList<>();
			for(int i = 0; i < 2; i++) {
				String userName = "user"+i;
				MOL userMol = new MOL(userName, encoder.encode("user"+i+""+i+""+i), userName+"@gmail.com", MolRole);				
				userMol.setCity(city);
				usersMol.add(userMol);
			}
			
			MOL userMol = new MOL("aabi", encoder.encode("aabi123"),"aabi@gmail.com", MolRole);
			userMol.setCity(city);
			usersMol.add(userMol);
			usersMol = usersRepository.saveAll(usersMol);
			
		}
		
		if ( ((List<User>)usersRepository.findAll(QUser.user.erole.eq(ERole.ROLE_Employee))).size() == 0) {			
			if(usersMol == null) usersMol = (List<User>) usersRepository.findAll(QUser.user.erole.eq(ERole.ROLE_Mol));
			
			ERole empRole = ERole.ROLE_Employee;		
			for(int k = 0; k < usersMol.size(); k++) {
				MOL mol = (MOL) usersMol.get(k);				
				for(int i = 0; i < 5; i++) {
					String emp1stName = "emp"+i+""+mol.getId();
					String emp2ndName = "lName"+i;
					Employee userEmp =  new Employee(
							emp1stName, emp2ndName, emp1stName+" "+emp2ndName, 
							encoder.encode("emp"+i+""+i+""+i), emp1stName+"@gmail.com", empRole);
					userEmp.setMol(mol );					
					emps.add(userEmp);						
				} 
			}
			usersRepository.saveAll(emps);
		}
		
		if(categoryRepository.count() == 0) {
			
			List<Category> categories = new ArrayList<>();
			Category c1 = new Category("vehicles",ProductType.LTA);
			Category c2 = new Category("computers",ProductType.LTA);
			Category c3 = new Category("machinery",ProductType.LTA);
			Category c4 = new Category("production equipment",ProductType.LTA);
			
			Category c5 = new Category("computer peripherals",ProductType.LTA);
			Category c6 = new Category("mobile phones",ProductType.LTA);
			Category c7 = new Category("cars",ProductType.LTA);
			Category c8 = new Category("other",ProductType.LTA);
			
			Category c9 = new Category("office supplies",ProductType.STA);
			Category c10 = new Category("cleaning supplies",ProductType.STA);
			Category c11 = new Category("kitchen supplies",ProductType.STA);
			Category c12 = new Category("Raw materials",ProductType.STA);
			
			categories.add(c1);
			categories.add(c2);
			categories.add(c3);
			categories.add(c4);
			categories.add(c5);
			categories.add(c6);
			categories.add(c7);
			categories.add(c8);
			categories.add(c9);
			categories.add(c10);
			categories.add(c11);
			categories.add(c12);
			
			categories = categoryRepository.saveAll(categories);
			
			if(usersMol == null) usersMol = (List<User>) usersRepository.findAll(QUser.user.erole.eq(ERole.ROLE_Mol));
			List<UserCategory> usercategories = new ArrayList<>();
			Random rand = new Random(); 

			for(User user : usersMol) {
				
				for(int i = 0; i <categories.size(); i+=3 ) {
					Category c = categories.get(i);
					
					double rnd = 0.0;
					
					if(c.getProductType().equals(ProductType.LTA)) {
					    rnd = (rand.nextDouble())*50.0;
						rnd = Math.round(rnd*100.0)/100.0;						
					}
					
					UserCategory uc = new UserCategory(c, (MOL) user, rnd);
					usercategories.add(uc);					
				}
				
				userCategoryRepository.saveAll(usercategories);
			}
		}
		
		if(productsRepository.count() == 0) {			
			if(usersMol == null) usersMol = (List<User>) usersRepository.findAll(QUser.user.erole.eq(ERole.ROLE_Mol));
			
			List<Product> products = new ArrayList<>();
			
			for(int i = 0; i < usersMol.size(); i++) {
				Long molId = usersMol.get(i).getId();
				List<UserCategory> userCategories = 
						(List<UserCategory>) userCategoryRepository.findAll(QUserCategory.userCategory.user.id.eq(molId));
			
				for(int k = 0; k < userCategories.size(); k++) {
					String productName = "product-"+ molId +""+ k;					
					Product p = new Product(productName, userCategories.get(k));
					products.add(p);				
				}		
			}			
			productsRepository.saveAll(products);			
		}
		
		if(suppliersRepository.count() == 0) {
			
			if(usersMol == null) usersMol = (List<User>) usersRepository.findAll(QUser.user.erole.eq(ERole.ROLE_Mol));			
			List<Supplier> suppliers = new ArrayList<>();
			
			for(int i = 0; i < usersMol.size(); i++) {
				for(int k = 0; k < 4 ; k++) {
					
					User mol = usersMol.get(i);
					String name = "Supplier"+k+""+mol.getId();
					String email = name+"@gmail.com";
					
					String PhoneNumber = "+359 89";					
					PhoneNumber+="7 "+i+""+i;
					PhoneNumber +=  k +" "+ k+""+k+""+k;
					
					String ddc = "BG";
					int c2 = (i/10>0 )? 3 : 6 ;
					for(int n = 0 ; n< c2 ; n++) ddc+=""+i;					
					ddc+=""+k+""+k+""+k;	
					
					Supplier s = new Supplier(name, email, PhoneNumber, ddc );
					s.setUser((MOL) mol);
					suppliers.add(s);				
				}				
			}			
			suppliersRepository.saveAll(suppliers);
		}
		
		if(deliveryRepository.count() == 0) {			
			List<Supplier> suppliers = suppliersRepository.findAll();			
			for(int i = 0; i < suppliers.size()-1; i++) {
				
				User mol = suppliers.get(i).getUser();
				List<UserProfile> ups = new ArrayList<>();				
				List<Product> products = (List<Product>) productsRepository
						.findAll(QProduct.product.userCategory.user.id.eq(mol.getId()));
				
				for(int k = 0 ; k < 2 ; k++) {					
					int y = (i+k)%2 == 0 ? 2018 : (i+k)%2 == 0 ? y = 2019 : 2020;
					int m = k+i ;
					m= m>12 ? 12 : m < 1 ? 1 : m;

					LocalDate date = LocalDate.of(y, m, 1);										
					Delivery delivery = new Delivery(suppliers.get(i), date);
					Long number = deliveryRepository.count(QDelivery.delivery.supplier.user.id.eq(mol.getId()));
					delivery.setNumber(number+1);
					delivery = deliveryRepository.save(delivery);
					
					for(int c = 0; c < 2; c++)
					{
						int productIndex = k == 0 ? products.size() -1 : c+k;
						Product p = products.get(productIndex);
						String priceStr = k+c+100+"";
						BigDecimal price = new BigDecimal(priceStr);
						
						int quantity = 2;
						DeliveryDetail dd = new DeliveryDetail( price, delivery, p);
						dd = deliveryDetailRepository.save(dd);
						
					    for(int j = 0; j < quantity ; j++) {
					    	UUID uuid = UUID.randomUUID();
					    	String inventoryNumber = uuid+"-"+ mol.getId()+""+p.getName().charAt(p.getName().length()-1);
					    	ProductDetail pd = new ProductDetail( inventoryNumber, false, ECondition.Available, dd);
					    	pd = productDtsRepository.save(pd);
					    	UserProfile up = new UserProfile(mol, pd, date, null) ;
					    	ups.add(up);					    	
					    }		
				   }				
			    }
				upRepo.saveAll(ups);
			}
		}		
   }
	
	
}
		
 

		

