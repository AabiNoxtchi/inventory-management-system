package com.inventory.inventory.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.inventory.inventory.Model.BaseEntity;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T,Long>, QuerydslPredicateExecutor<T>{
	
	//void deleteWhereIdIn(List<Integer> ids);
	
}
