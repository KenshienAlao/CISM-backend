package com.cism.backend.dto.system.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
    Long id,
    String receipt,
    BigDecimal subtotal,
    BigDecimal deliveryFee,
    BigDecimal totalAmount,
    String deliveryMethod,
    String paymentMethod,
    String status,
    String note,
    Instant createdAt,
    String stallName,
    List<OrderItemResponse> orderItems
) {}
