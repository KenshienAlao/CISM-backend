package com.cism.backend.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cism.backend.model.admin.StallModel;

public interface CreateStallRepository extends JpaRepository<StallModel, Integer> {
}
