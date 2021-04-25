package com.inventory.inventory.Repository.Interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.inventory.inventory.Model.ProfileDetail;

@Repository
public interface ProfileDetailRepository extends JpaRepository<ProfileDetail,Long>, QuerydslPredicateExecutor<ProfileDetail>{
	
	@Modifying
	@Query(value = "delete from ProfileDetail u where u.id in ?1")//:ids ")
	void deleteByIdIn(@Param("ids") List<Long> ids);

}
