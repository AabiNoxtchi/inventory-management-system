package com.inventory.inventory.Repository.Interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.inventory.inventory.Model.UserProfile;

@Repository
public interface UserProfilesRepository extends BaseRepository<UserProfile>{
	
	@Modifying
	@Query(value = "delete from UserProfile u where u.id in ?1")
	void deleteByIdIn(@Param("ids") List<Long> ids);

}
