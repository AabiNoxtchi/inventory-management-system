package com.inventory.inventory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.joda.money.CurrencyUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.inventory.inventory.Model.Category;
import com.inventory.inventory.Model.City;
import com.inventory.inventory.Model.Country;
import com.inventory.inventory.Model.Delivery;
import com.inventory.inventory.Model.DeliveryDetail;
import com.inventory.inventory.Model.ECondition;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.Event;
import com.inventory.inventory.Model.EventType;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.ProductDetail;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.ProfileDetail;
import com.inventory.inventory.Model.QCountry;
import com.inventory.inventory.Model.QDelivery;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.QProductDetail;
import com.inventory.inventory.Model.QProfileDetail;
import com.inventory.inventory.Model.QUserCategory;
import com.inventory.inventory.Model.QUserProfile;
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
import com.inventory.inventory.Repository.Interfaces.MOLRepository;
import com.inventory.inventory.Repository.Interfaces.ProductDetailsRepository;
import com.inventory.inventory.Repository.Interfaces.ProductsRepository;
import com.inventory.inventory.Repository.Interfaces.SuppliersRepository;
import com.inventory.inventory.Repository.Interfaces.UserCategoryRepository;
import com.inventory.inventory.Repository.Interfaces.UserProfilesRepository;
import com.inventory.inventory.Repository.Interfaces.UsersRepository;
import com.inventory.inventory.ViewModels.ProductDetail.ProductDetailDAO;
import com.inventory.inventory.ViewModels.Shared.SelectItem;
import com.inventory.inventory.ViewModels.UserProfiles.UserProfileDAO;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;

@Component
public class UserDataSeeder implements CommandLineRunner {
	
	@Autowired
	EventHandler handler;		

	@Autowired
	UsersRepository usersRepository;
	
	//@Autowired
	//MOLRepository molRepository;
//	
//	@Autowired
//	EmployeeRepository empRepository;
	
//	@Autowired
//	RolesRepository roleRepository;
	
//	@Autowired
//	EventsRepository eventRepository;
	
	@Autowired
	CountryRepository countryRepo;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	UserCategoryRepository userCategoryRepository;
	
//	@Autowired
//	LTADetailRepository lTADetailRepo;
	
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
	
	//@Autowired
	//AvailableProductsRepository availablesRepo;
	
	@Autowired
	PasswordEncoder encoder ;
	
	@Value("${app.password}")
	private String password;
	
	List<User> usersMol ;
	//Map<User, List<User>> usersEmp ;
	List<User> emps = new ArrayList<>();
	Country BG;

	@Override
	public void run(String... args) throws Exception {
		loadUserData();	
		System.out.println("seeding ok");
		
		//handler.runHandler();
	}

	private void loadUserData() {
		
//		if(roleRepository.count()==0) {
//			roleRepository.save(new Role(ERole.ROLE_Admin));			
//			roleRepository.save(new Role(ERole.ROLE_Employee));
//			roleRepository.save(new Role(ERole.ROLE_Mol));
//		}
//		
//		if(eventRepository.count()==0) {
//			eventRepository.save(new Event(EventType.Discarded));			
//		}
		
		if(countryRepo.count() == 0) {
			System.out.println("countries : **********************************************");
			Map<String, Country> countries = new HashMap<String, Country>(); 
			
			 String[] isoCountries = Locale.getISOCountries();
		     for (String country : isoCountries) {
		    	 System.out.println(country);
		         Locale locale = new Locale("en", country);        
		        // String iso = locale.getISO3Country();
		        // String code = locale.getCountry();
		         String name = locale.getDisplayCountry();
		         
		         Country c = new Country((long) -1, country, name);
		         if(country.equals("BG")) {
		        	 
		        	 c.addCity(new City("Varna", "EET"));
						c.addCity(new City("Sofia", "EET"));
						BG = c;
		         }else
		        	 countries.put(country, c);
		        // System.out.println(iso + " " + code + " " + name);
		     }
		     
		     System.out.println("currencies = ***************************************");
		     List<CurrencyUnit> currencyUnits = CurrencyUnit.registeredCurrencies();
		     for(CurrencyUnit cu : currencyUnits) {
		    	 System.out.println("cu = "+cu);
		    	 for(String code : cu.getCountryCodes()) {
		    		 
		    		 if(countries.get(code) != null) countries.get(code).setCurrency(cu+"");
		    		 else if(code.equals("BG"))BG.setCurrency(cu+"");
//		    		 else {
//		    			 countries.remove(code);
//		    			 System.out.println("code"+code);
//		    			 
//		    		 }
		    	 }
		    	 System.out.println(cu.getCurrencyCode()+" "+cu.getCode()+" "+cu.getSymbol()+" "+cu.getCountryCodes());
		     }
		     
		   //  List<SelectItem> phoneCodes = new ArrayList<>();
				for (String cc : PhoneNumberUtil.getInstance().getSupportedRegions()) {
					
		            int phoneCode = PhoneNumberUtil.getInstance().getCountryCodeForRegion(cc);
		            if(countries.get(cc) != null) {
		            	countries.get(cc).setPhoneCode("+"+phoneCode+"");
		            	System.out.println("code found = "+phoneCode);
		            }
		            
		    		 else if(cc.equals("BG")) {
		    			 BG.setPhoneCode("+"+phoneCode+"");
		    			 System.out.println("BG code found = "+phoneCode);
		    		 }
		            
		           // String displayCountry = (new Locale("", cc)).getDisplayCountry();
		           // SelectItem si = new SelectItem(cc, displayCountry+"/+"+phoneCode);
		           // phoneCodes.add(si);
		            
		           // if((phoneCode+"").equals(currentCode) || displayCountry.equals(molCountryName)) 
		            		//{model.setDefaultCodeValue(si);}//model.setSelectedCode(currentCode+"");}
		          //  else if() {model.setDefaultCodeValue(si);}//model.setSelectedCode(phoneCode+"");}
		           
		           // if(molCountryName!=null ) model.setDefaultCodeValue(displayCountry);
		            
		          System.out.println("cc = "+cc+" code = "+phoneCode );
		        }
				//model.setPhoneCodes(phoneCodes);*/
		     
		     countryRepo.saveAll(countries.values());
		     BG = countryRepo.save(BG);
		    //// Long BGid =  countryRepo.findOne(QCountry.country.code.eq("BG")).get().getId();
		   //  BG.setId(BGid);
		   //  BG.getCities().stream().forEach(c -> c.setCountry(BG));
		    // BG.setCities(BG.getCities());
		    // System.out.println("BG id = "+BG.getId());
			//BG.getCities().stream().forEach(x->System.out.println(x.toString()));
		   //  BG=new Country("Bulgaria", "BGN");
//				BG.addCity(new City("Varna", "EET"));
//				BG.addCity(new City("Sofia", "EET"));
			//BG = countryRepo.save(BG);
			//BG.getCities().stream().forEach(x->System.out.println(x.toString()));
			
		}
//		
		
		if (usersRepository.count() == 0) {			
			
//			Role adminRole = null;
//			Optional<Role> opt = roleRepository.findByName(ERole.ROLE_Admin);
//			if (!opt.isPresent())
//				adminRole = roleRepository.save(new Role(ERole.ROLE_Admin));
//			else 
//				adminRole=opt.get();
			ERole adminRole = ERole.ROLE_Admin;
//		
			User user = new User("admin", encoder.encode(password), "admin123@gmail.com", adminRole);			
			usersRepository.save(user);
			
			//**************************************************  development       *********************************************************//
//			Role MolRole = null;
//			Optional<Role> optMol = roleRepository.findByName(ERole.ROLE_Mol);
//			if (!optMol.isPresent())
//				MolRole = roleRepository.save(new Role(ERole.ROLE_Mol));
//			else 
//				MolRole=optMol.get();
			
			ERole MolRole = ERole.ROLE_Mol;
			City city = BG.getCities().get(0);
			if(usersMol == null) usersMol = new ArrayList<>();
			for(int i = 0; i < 2; i++) {			
				
				String userName = "user"+i;
				MOL userMol = new MOL(userName, encoder.encode("user"+i+""+i+""+i), userName+"@gmail.com", MolRole);
				//userMol = usersRepository.save(userMol);
				//userMol.setMolUser( new MOL(city));
				userMol.setCity(city);
				usersMol.add(userMol);				
				
			}
			
			MOL userMol = new MOL("aabi", encoder.encode("aabi123"),"aabi@gmail.com", MolRole);
			//userMol.setMolUser( new MOL(city));
			userMol.setCity(city);
			//userMol = usersRepository.save(userMol);
			usersMol.add(userMol);
			usersMol = usersRepository.saveAll(usersMol);
//			List<MOL> mols = new ArrayList<>();
//			for(User molUser : usersMol) {
//				
//				MOL mol = new MOL(city);
//				
//				mols.add(mol);
//				
//			}
			
		}
		
		if ( ((List<User>)usersRepository.findAll(QUser.user.erole.eq(ERole.ROLE_Employee))).size() == 0) {
			
			if(usersMol == null) usersMol = (List<User>) usersRepository.findAll(QUser.user.erole.eq(ERole.ROLE_Mol));
			
//			Role empRole = null;
//			Optional<Role> opt = roleRepository.findByName(ERole.ROLE_Employee);
//			if (!opt.isPresent())
//				empRole = roleRepository.save(new Role(ERole.ROLE_Employee));
//			else 
//				empRole=opt.get();
			
			ERole empRole = ERole.ROLE_Employee;
		
			//if(usersEmp == null) usersEmp = new HashMap<>();
			for(int k = 0; k < usersMol.size(); k++) {
				
				//List<User> emps = new ArrayList<>();
				MOL mol = (MOL) usersMol.get(k);
				
				for(int i = 0; i < 5; i++) {			
					
					String emp1stName = "emp"+i+""+mol.getId();
					String emp2ndName = "lName"+i;
					Employee userEmp =  new Employee(
							emp1stName, emp2ndName, emp1stName+" "+emp2ndName, 
							encoder.encode("emp"+i+""+i+""+i), emp1stName+"@gmail.com", empRole);
					userEmp.setMol(mol );
					
					//userEmp = usersRepository.save(userEmp);
					//User mol = usersMol.get(k); 
					//if(usersEmp.containsKey(mol)) usersEmp.get(mol).add(userEmp);
					//else {
						
						emps.add(userEmp);
						//usersEmp.put(mol, emps);
					//}
				}
				
				 
			}
			usersRepository.saveAll(emps);
		}
		
		if(categoryRepository.count() == 0) {
			
			List<Category> categories = new ArrayList<>();
			//Category(String name, ProductType productType)
			Category c1 = new Category("vehicles",ProductType.LTA);
			//categoryRepository.save(c1);
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
			
			//categ
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
			
			//categories =  categoryRepository.saveAll( categories);
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
						
						//lTADetailRepo.save(new LTADetail(uc,rnd));
						
					}
					
					UserCategory uc = new UserCategory(c, (MOL) user, rnd);
					usercategories.add(uc);
					//uc = userCategoryRepository.save(uc);
					//uc = userCategoryRepository.findById(uc.getId()).get();
					
					
				}
				
				userCategoryRepository.saveAll(usercategories);
			}
			
			
			
			/*List<SubCategory> childs = new ArrayList<>();
			
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
			*/
		}
		
		if(productsRepository.count() == 0) {
			
			if(usersMol == null) usersMol = (List<User>) usersRepository.findAll(QUser.user.erole.eq(ERole.ROLE_Mol));
			
			List<Product> products = new ArrayList<>();
			
			
			for(int i = 0; i < usersMol.size(); i++) {
				Long molId = usersMol.get(i).getId();
				List<UserCategory> userCategories = 
						(List<UserCategory>) userCategoryRepository.findAll(QUserCategory.userCategory.user.id.eq(molId));
				/*for(int k = 0; k<5 ; k++) {
					
					String productName = "product"+k+""+molId;					
					Product p = new Product(productName,ProductType.MA);
					//p.setUser(molId);					
					products.add(p);				
				}	*/	
				
				for(int k = 0; k < userCategories.size(); k++) {  // ~~ 4
					
					String productName = "product-"+molId+""+k;					
					Product p = new Product(productName, userCategories.get(k));
					//p.setUser(usersMol.get(i).getId());					
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
					
					String PhoneNumber = "+359/89 ";					
					//int c = (i/10 > 0) ? 2 : 4 ;
					//for(int n = 0 ; n < 2 ;  n++) PhoneNumber+=""+i;
					PhoneNumber+="7"+i+""+i;
					PhoneNumber += " " + k +""+ k+""+k+""+k;
					
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
				//List<ProductDetail> productDetails = new ArrayList<>();
				List<UserProfile> ups = new ArrayList<>(); 
				
				List<Product> products = (List<Product>) productsRepository
						.findAll(QProduct.product.userCategory.user.id.eq(mol.getId()));
				
				for(int k = 0 ; k < 2 ; k++) {
					
					//int y = 2021 - k ;
					int y = i == 0 ? 2018 : i == 1 ? y = 2019 : 2020;
					int m = k==0 ? 1 : 2;
//							y == 2021 ?
//							(k % 2 == 0) ? 1 : 2  //1, 2
//									: ( i % 2 == 0) ? (k + 1)*2 /*2, 4, 6, 8, 10, 12*/
//											: (k*2 + 1) ; /* 1, 3, 5, 7, 9, 11 */
					
					
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
						int productIndex = c+k;//products.size() > k ? k : products.size()-1 ;//+( k + c > 2 ? 10 : 0);
						Product p = products.get(productIndex);
						String priceStr = k+c+100+"";
						BigDecimal price = new BigDecimal(priceStr);
						
						int quantity = 2;//k + c + i;//>0?k:1;
						//DeliveryDetail dd = new DeliveryDetail(quantity, price, delivery, p);
						DeliveryDetail dd = new DeliveryDetail( price, delivery, p);
						dd = deliveryDetailRepository.save(dd);
						
					    for(int j = 0; j < quantity ; j++) {
					    	
					    	//ProductDetail(String inventoryNumber, boolean isDiscarded, boolean isAvailable,DeliveryDetail deliveryDetail)
					    	UUID uuid = UUID.randomUUID();
					    	String inventoryNumber = uuid+"-"+ mol.getId()+""+p.getName().charAt(p.getName().length()-1);
					    	
					    
//					    	ProductDetail pd = new ProductDetail(/*uuid.toString()*/ inventoryNumber, false, true, dd);
					    	ProductDetail pd = new ProductDetail(/*uuid.toString()*/ inventoryNumber, false, ECondition.Available, dd);
					    	//productDetails.add(pd);
					    	pd = productDtsRepository.save(pd);
					    	//GregorianCalendar calendar = new GregorianCalendar(y, m, 1, 10, 0, 0);				
							//Date date1 = calendar.getTime();
					    	//LocalDate date1 = LocalDate.of(y, m, 1);
					    	UserProfile up = new UserProfile(mol, pd, date, null) ;
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
		
		//UserProfile up = upRepo.findById((long) 65).get();
		
		//ProfileDetail pd = new ProfileDetail();
		
		/********************  test ********************/
		/*Predicate p =  QProductDetail.productDetail.deliveryDetail.product.user.id.eq((long) 4);
		List<ProductDetailDAO> daos = productDtsRepositoryImpl.getDAOs(p, (long)0, (long)100);//, null);
		System.out.println("dao's. size = "+daos.size());
		daos.stream().forEach(x-> System.out.println(x.toString()));*/
		//System.out.println("date = "+LocalDate.now());
		
		//productDtsRepositoryImpl.getSelectables();
		
		/*Predicate predicate = QProductDetail.productDetail.id.in(
				  JPAExpressions.selectFrom(QUserProfile.userProfile)
				  .where(QUserProfile.userProfile.userId.eq((long) 4)
						  .and(QUserProfile.userProfile.returnedAt.isNull()))
				  .select(QUserProfile.userProfile.productDetail.id)
				  );
		List<ProductDetail> pds = (List<ProductDetail>) productDtsRepository.findAll(predicate);
		List<SelectItem> pdsItems = productDtsRepositoryImpl.getInventoryNumbers(predicate);
		System.out.println("pds size = "+pds.size());
		System.out.println("pdsItems size = "+pdsItems.size());*/
		
		/*********************** test ************************/
		
//		References r = new References();
//		List<CurrencyUnit> currencyUnits = r.getCurrencyUnits();
//		for(CurrencyUnit c : currencyUnits) {System.out.println("c = "+c.toString());}
//		List<String> zids = r.getZids();
//		for(String z : zids){System.out.println("z = "+z);}
//		List<String> countries = r.getCountries();
//		for(String c : countries) {System.out.println("country = "+c);}
//		
//		List<Country> testCountry = countryRepo.findAll();
//		System.out.println("testCountry.size = "+testCountry.size());
//		for(Country c : testCountry) {
//			System.out.println(c.toString());
//			System.out.println("c.getCities == null = "+(c.getCities()==null));
//		}
		
		/*********************** test ************************/
//		System.out.println("countries : **********************************************");
//		Map<String, Country> countries = new HashMap<String, Country>(); 
//		
//		 String[] isoCountries = Locale.getISOCountries();
//	     for (String country : isoCountries) {
//	    	 System.out.println(country);
//	         Locale locale = new Locale("en", country);        
//	        // String iso = locale.getISO3Country();
//	        // String code = locale.getCountry();
//	         String name = locale.getDisplayCountry();
//	         countries.put(country, new Country((long) -1, country, name));
//	        // System.out.println(iso + " " + code + " " + name);
//	     }
//	     
//	     System.out.println("currencies = ***************************************");
//	     List<CurrencyUnit> currencyUnits = CurrencyUnit.registeredCurrencies();
//	     for(CurrencyUnit cu : currencyUnits) {
//	    	 for(String code : cu.getCountryCodes()) {
//	    		 countries.get(code).setCode(code);
//	    	 }
//	    	 System.out.println(cu.getCurrencyCode()+" "+cu.getCode()+" "+cu.getSymbol()+" "+cu.getCountryCodes());
//	     }
//	     
//	     System.out.println("zones : *************************************");
//	     List<String> getZids = 
//	    		 (ZoneId.getAvailableZoneIds()).stream().collect(Collectors.toList());
//	     for(String z : getZids)
//	    	 System.out.println(z);
	 		//Collections.sort(zids, (o1, o2) -> o1.compareTo(o2));
		
		/********************************************* test ************************************/
		
//		List<UserProfileDAO> updaos = upRepoImpl.getDAOs(
//				QUserProfile.userProfile.userId.eq((long) 4).or(
//						QUserProfile.userProfile.user.mol.isNotNull().and(QUserProfile.userProfile.user.mol.id.eq((long) 4))), (long)0, (long)1000);
//		updaos.parallelStream().forEach(x-> System.out.println(x.toString()));
		
		
		/*(
						QUserProfile.userProfile.userId.eq((long) 4).or(
						QUserProfile.userProfile.user.mol.isNotNull().and(QUserProfile.userProfile.user.mol.id.eq((long) 4)))
						.and()
						)*/
		
//		System.out.println("daos with profiledetail != null");
//		
//		Predicate p = QUserProfile.userProfile.profileDetail.isNotNull(); // not working ???
//		Predicate p2 = QUserProfile.userProfile.id.in(JPAExpressions.selectFrom((QProfileDetail.profileDetail)).select(QProfileDetail.profileDetail.id));
		
		
//		List<UserProfileDAO> updaos2 = upRepoImpl.getDAOs( p2
//				
//				, (long)0, (long)1000);
//		updaos2.parallelStream().forEach(x-> System.out.println(x.toString()));
//		
		//****************************************************  development       ************************************************************//
		
  }
	
}
		
 

		

