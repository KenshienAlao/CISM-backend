package com.cism.backend.dto.system.order;

import java.util.List;

public record OrderRequest(
    List<Long> cartItemIds,
    String deliveryMethod,
    String paymentMethod,
    String note
) {}
