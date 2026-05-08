package com.cism.backend.repository.system;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cism.backend.model.system.order.OrderItemModel;

public interface OrderItemRepository extends JpaRepository<OrderItemModel, Long> {
}
