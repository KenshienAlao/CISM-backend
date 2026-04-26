package com.cism.backend.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cism.backend.model.stalls.StallIncomesModel;

@Repository
public interface CreateStallIncomesRepository extends JpaRepository<StallIncomesModel, Long> {
    
}
