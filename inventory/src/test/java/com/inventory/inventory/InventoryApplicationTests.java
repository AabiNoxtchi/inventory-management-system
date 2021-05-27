package com.inventory.inventory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.inventory.inventory.Model.City;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.ProductDetail;
import com.inventory.inventory.Model.QCity;
import com.inventory.inventory.Model.UserCategory;
import com.inventory.inventory.Model.User.MOL;
import com.inventory.inventory.Model.User.QUser;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.Repository.Interfaces.CityRepository;
import com.inventory.inventory.Repository.Interfaces.ProductDetailsRepository;
import com.inventory.inventory.Repository.Interfaces.ProductsRepository;
import com.inventory.inventory.Repository.Interfaces.UserCategoryRepository;
import com.inventory.inventory.Repository.Interfaces.UsersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.springframework.test.annotation.Rollback;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = Replace.NONE)
class InventoryApplicationTests {
	
	 @Autowired
	 private UsersRepository usersRepo;
	 
	 @Autowired
	 private CityRepository cityRepo;
	 
	 @Autowired
	 UserCategoryRepository userCategoryRepo;
	 
	 @Autowired
	 ProductDetailsRepository productDtsRepo;
	 
	 @Autowired
	 ProductsRepository productsRepo;
	 
	 @Autowired
	 PasswordEncoder encoder;	
	
	@Test
	void contextLoads() {
		
	}	
	
	@Test
	@Rollback(false)
	public void testReadUsers() { 
		List<User> mols = (List<User>) usersRepo.findAll(QUser.user.erole.eq(ERole.ROLE_Mol));		
		assertThat(mols.size()).as("check mol's size").isEqualTo(3);
		
		List<User> employees = (List<User>) usersRepo.findAll(QUser.user.erole.eq(ERole.ROLE_Employee));
		assertThat(employees.size()).as("check employees's size").isEqualTo(15);		
		
	}
	
	@Test
	@Rollback(false)
	public void testReadUserCategory() { 
		List<UserCategory> items = (List<UserCategory>) userCategoryRepo.findAll();		
		assertThat(items.size()).as("check userCategories size").isEqualTo(12);
		
	}
	
	@Test
	@Rollback(false)
	public void testReadProduct() { 
		List<Product> items = (List<Product>) productsRepo.findAll();		
		assertThat(items.size()).as("check products size").isEqualTo(12);
		
	}
	
	@Test
	@Rollback(false)
	public void testReadAndDeleteProductDts() { 
		List<ProductDetail> items = (List<ProductDetail>) productDtsRepo.findAll();		
		assertThat(items.size()).as("check productDetails size").isEqualTo(88);
		
		ProductDetail pdLast = items.get(items.size()-1);
		
		productDtsRepo.delete(pdLast);
		
		Optional<ProductDetail> optPdDeleted = productDtsRepo.findById(pdLast.getId());
		
		ProductDetail pdDeleted = optPdDeleted.isPresent() ? optPdDeleted.get() : null;
		
		assertThat(pdDeleted).as("check deleted productDetail").isNull();
		
	}	
	
	
	@Test
	@Rollback(false)
	public void testUpdateUser() {	
		
		Optional<User> optUserMol = usersRepo.findOne(QUser.user.userName.eq("user0"));
		User user = optUserMol.isPresent() ? optUserMol.get() : null;
		String userName = "New userName";
		
		user.setUserName(userName);		
		user = usersRepo.save(user);
		
		assertThat(user.getUserName()).as("check user name").isEqualTo(userName);
		
	}
	
	@Test
	@Rollback(false)
	public void testCreateAndDeleteUser() {	
		
		ERole MolRole = ERole.ROLE_Mol;
		City city = cityRepo.findOne(QCity.city.name.eq("Varna")).get();		
		String userName = "New MOL";
		MOL userMol = new MOL(userName, encoder.encode("NewMol!23"),"newMol@gmail.com", MolRole);
		userMol.setCity(city);		
		userMol = usersRepo.save(userMol);	
		
		assertThat(userMol.getId()).isGreaterThan(0);
		
		usersRepo.delete(userMol);		
		
		Optional<User> optUserMol = usersRepo.findOne(QUser.user.userName.eq(userName));
		User user = optUserMol.isPresent() ? optUserMol.get() : null;		
		
		assertThat(user).as("check user deleted").isNull();
		
	}
	
	
}
