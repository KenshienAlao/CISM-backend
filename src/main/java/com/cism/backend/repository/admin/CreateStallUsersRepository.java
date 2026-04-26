package com.cism.backend.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cism.backend.model.stalls.StallUsersModel;

public interface CreateStallUsersRepository extends JpaRepository<StallUsersModel, Integer> {
}
