package com.inventory.inventory.Repository.Interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventory.inventory.Model.LTADetail;
import com.inventory.inventory.Model.UserCategory;

@Repository
public interface LTADetailRepository extends JpaRepository<LTADetail,Long>{

}
