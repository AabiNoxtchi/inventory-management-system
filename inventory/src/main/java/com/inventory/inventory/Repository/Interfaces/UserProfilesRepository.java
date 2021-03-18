package com.inventory.inventory.Repository.Interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.inventory.inventory.Model.UserProfile;
import org.springframework.transaction.annotation.Transactional;

@Repository
//@Transactional
public interface UserProfilesRepository extends BaseRepository<UserProfile>{
	
	//void deleteByIdIn(List<Long> ids);
	
	@Modifying
	@Query(value = "delete from UserProfile u where u.id in ?1")//:ids ")
	void deleteByIdIn(@Param("ids") List<Long> ids);
	
	/*@Modifying
	@Query("delete from User u where u.id in ?1")
	void deleteUsersWithIds(List<Integer> ids);*/
	
	/*The @Query method creates a single JPQL query against the database.
		By comparison, the deleteBy methods execute a read query,
		 then delete each of the items one by one.*/

}
