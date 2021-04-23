package com.inventory.inventory.Events;

import static java.time.temporal.ChronoUnit.MONTHS;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventory.inventory.Model.Delivery;
import com.inventory.inventory.Model.DeliveryDetail;
import com.inventory.inventory.Model.ERole;
import com.inventory.inventory.Model.Product;
import com.inventory.inventory.Model.ProductDetail;
import com.inventory.inventory.Model.ProductType;
import com.inventory.inventory.Model.QCity;
import com.inventory.inventory.Model.QDelivery;
import com.inventory.inventory.Model.QDeliveryDetail;
import com.inventory.inventory.Model.QProduct;
import com.inventory.inventory.Model.QProductDetail;
import com.inventory.inventory.Model.QUserCategory;
import com.inventory.inventory.Model.QUserProfile;
import com.inventory.inventory.Model.User.QMOL;
import com.inventory.inventory.Model.User.QUser;
import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.Repository.DeliveryRepositoryImpl;
import com.inventory.inventory.Repository.EventProductRepositoryImpl;
import com.inventory.inventory.Repository.ProductDetailRepositoryImpl;
import com.inventory.inventory.Repository.RepositoryImpl;
import com.inventory.inventory.Repository.Interfaces.CityRepository;
import com.inventory.inventory.Repository.Interfaces.DeliveryDetailRepository;
import com.inventory.inventory.Repository.Interfaces.DeliveryRepository;
import com.inventory.inventory.Repository.Interfaces.ProductDetailsRepository;
import com.inventory.inventory.Repository.Interfaces.ProductsRepository;
import com.inventory.inventory.Repository.Interfaces.UsersRepository;
import com.inventory.inventory.ViewModels.ProductDetail.ProductDetailDAO;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

@Service
public
 class EventHandler {
	
	private static final Logger logger = LoggerFactory.getLogger("EventHandler");
	

	
	@Autowired
	ProductDetailRepositoryImpl pdRepoImpl;
	
	@Autowired
	ProductDetailsRepository pdRepo;
	
	@Autowired
	UsersRepository usersRepo;
	
	@Autowired
	CityRepository cityRepo;
	
	@Autowired
	DeliveryRepository deliveryRepo;
	
	@Autowired
	DeliveryDetailRepository ddRepo;
	
	@Autowired
	DeliveryRepositoryImpl deliveryRepoImpl;
	
	@Autowired
	EventSender sender;

	@Autowired
	Resources resources ;
	
	//Long sleep = (long) (60 * 1000 * 60*24); //every m * 60*24 
	Long sleep = (long) (60 * 1000 *2); //every  m * x for test
	
	public void runHandler() {
		
		resources.populateResources();
		sender.runHandler();
		
		ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> 
        {
        	while(true) {
          		
        		// update total amortization
          		Map<Long, List<ProductDetailDAO>> pdsToUpdate = getInventoriesForUpdate();
          		updateInventories(pdsToUpdate);  
          		
          		// see all deliveries with all discarded inventories          		
          		//resources.setAllDiscardedDeliveries(getAllDiscardedDeliveries()); 
          		resources.addInEvents(EventType.AllDiscarded, getAllDiscardedDeliveries());
          		
          		
          		// see all empty deliveries
          		resources.addInEvents(EventType.EmptyDeliveries, getAllEmptyDeliveries());
          		
          		sender.notifyUsers();
          		
          		// delete all soft deleted employees who are left without profiles and owings
          		 deleteDeletedEmployeesWithNoProfiles();
          		
          		delay(); 
          		
             }
        });
        
        executor.shutdown();
          
    }	

	private Map<Long, List<Long>> getAllEmptyDeliveries() {
		
		QDelivery dq = QDelivery.delivery;		
		QDeliveryDetail ddq = QDeliveryDetail.deliveryDetail;		
		
		Predicate p = ddq.productDetails.size().gt(0);
		JPQLQuery<Long> expression = JPAExpressions.selectFrom(ddq).where(p).select(ddq.delivery.id);
					
		Predicate dP = dq.deliveryDetails.size().eq(0).or(dq.id.notIn(expression));		
		
		return deliveryRepoImpl.getAllUsersDeliveries(dP);
		
	}

	private Map<Long, List<Long>> getAllDiscardedDeliveries() {
		
		QDelivery dq = QDelivery.delivery;		
		QDeliveryDetail ddq = QDeliveryDetail.deliveryDetail;		
		QProductDetail pdq = QProductDetail.productDetail;		
		NumberPath<Long> parentIdq = pdq.deliveryDetailId;		
							
		Predicate ddP = 
				ddq.id.in(
						JPAExpressions.selectFrom(pdq)
						.where(parentIdq.eq(ddq.id).and(pdq.isDiscarded.eq(false)))
						.select(parentIdq).distinct()								
								).or(ddq.productDetails.size().eq(0));
		JPQLQuery<Long> expression = JPAExpressions.selectFrom(ddq).where(ddP).select(ddq.delivery.id);		
		Predicate dP = dq.deliveryDetails.size().gt(0).and(dq.id.notIn(expression));
		return deliveryRepoImpl.getAllUsersDeliveries(dP); 
	}

	private void deleteDeletedEmployeesWithNoProfiles() {
		List<User> Emps = (List<User>) usersRepo.findAll(
				QUser.user.deleted.isNotNull()
				.and(QUser.user.userProfiles.isEmpty()));
				
		usersRepo.deleteAll(Emps);
		
	}

	private Map<Long, List<ProductDetailDAO>> getInventoriesForUpdate() {
		
		List<User> mols = (List<User>) usersRepo.findAll(QUser.user.erole.eq(ERole.ROLE_Mol));
		Map<Long, List<ProductDetailDAO>> inventoriesToUpdate = new HashMap<>();		
		
		QProductDetail qpd = QProductDetail.productDetail;
		QUserCategory quc = QUserCategory.userCategory;
		
		for(User u : mols) {
			
		Predicate typeAndUser =  qpd.deliveryDetail.product.userCategory.id.in(
  				JPAExpressions.selectFrom(quc)
  				.where(quc.userId.eq(u.getId())
  						.and(quc.category.productType.eq(ProductType.LTA)))
  				.select(quc.id));
		
    	Predicate p = qpd.isDiscarded.isFalse().and(typeAndUser);
    			//.and(qpd.totalAmortizationPercent.lt(100));    	
    			
    			List<ProductDetailDAO> DAOs = 
				pdRepoImpl.getDAOs(p, (long) 0, Long.MAX_VALUE);
    			inventoriesToUpdate.put(u.getId(), DAOs);    			
    			
		}    		
		
		return inventoriesToUpdate;
	}
	
	 private void updateInventories(Map<Long, List<ProductDetailDAO>> pdsToUpdate) {
	    	
	    	List<ProductDetail> updated = new ArrayList<>();
	    	
	    	for(Map.Entry e : pdsToUpdate.entrySet()) {
	    		
	    		List<ProductDetailDAO> DAOs = (List<ProductDetailDAO>) e.getValue();    		
	    		Long userId = (Long) e.getKey();
	    		
	    		QMOL qmol = QMOL.mOL;
	    		Predicate predicate = QCity.city.id.eq(JPAExpressions.selectFrom(qmol).where(qmol.id.eq(userId)).select(qmol.city.id));
	    		String timeZone = cityRepo.findOne(predicate).get().getTimeZone();    		
	    		
	    		DAOs.stream().forEach(pd -> {
	    			
		    		Double d = pd.getTotalAmortizationPercent();
		    		double before = d == null ? 0 : d ;
		    		
					updateAmortization(pd, getDateNow(timeZone));
					
					if(before != pd.getTotalAmortizationPercent()) {
						updated.add(pd.getProductDetail());
					}
					
					if(pd.getTotalAmortizationPercent() >= 100 && before < 100)
					{
						
						resources.addFullyAmortizedInventories(userId, pd.getId());
						
					}	
			
	    		});
	    	}
	    	
	    	/**************** for test *********************/ // pdRepo.saveAll(updated); 
			
		}

	
	private LocalDate getDateNow(String timeZone) {
		LocalDate now = LocalDate.now();		 
		ZonedDateTime zonedDateTime = now.atStartOfDay(ZoneId.of(timeZone));// ???
		now = zonedDateTime.toLocalDate();	
		return now;
	}

	private void updateAmortization(ProductDetailDAO pd, LocalDate now) {
						
			Long months = MONTHS.between(pd.getDateCreated(), now);			
			Double total = ( pd.getAmortizationPercent() * ( months/12.0 ));
			pd.setTotalAmortizationPercent( total <= 100 ? total : 100);					
		
	}
	

	private void delay() {
          try {
        	  
                Thread.sleep(sleep); 
          } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
          }
    }

}
