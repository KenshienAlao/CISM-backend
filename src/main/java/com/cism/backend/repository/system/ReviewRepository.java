package com.cism.backend.repository.system;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cism.backend.model.system.review.ReviewModel;

public interface ReviewRepository extends JpaRepository<ReviewModel, Long> {
    Optional<ReviewModel> findByStallId(Long stallId);
    Optional<ReviewModel> findByUsersId(Long usersId);
}
