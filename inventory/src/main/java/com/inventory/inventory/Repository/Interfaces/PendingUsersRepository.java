package com.inventory.inventory.Repository.Interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.inventory.inventory.Model.User.User;
import com.inventory.inventory.auth.Models.RegisterRequest;

@Repository
public interface PendingUsersRepository extends BaseRepository<RegisterRequest>{

}
