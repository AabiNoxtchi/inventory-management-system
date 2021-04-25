package com.inventory.inventory.Repository.Interfaces;

import org.springframework.stereotype.Repository;

import com.inventory.inventory.auth.Models.RegisterRequest;

@Repository
public interface PendingUsersRepository extends BaseRepository<RegisterRequest>{

}
