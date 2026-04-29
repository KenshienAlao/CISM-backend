package com.cism.backend.repository.stalls;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cism.backend.model.stalls.StallDrinksModel;

public interface StallDrinksRepository extends JpaRepository<StallDrinksModel, Long> {
    
    Optional<StallDrinksModel>  findByStallId(Long stallId);
}
