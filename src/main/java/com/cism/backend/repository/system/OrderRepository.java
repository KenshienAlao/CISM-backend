package com.cism.backend.repository.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cism.backend.model.system.order.OrderModel;

public interface OrderRepository extends JpaRepository<OrderModel, Long> {
    List<OrderModel> findByUser_IdOrderByCreatedAtDesc(Long userId);
    List<OrderModel> findByStall_IdOrderByCreatedAtDesc(Long stallId);
    boolean existsByReceipt(String receipt);
}
