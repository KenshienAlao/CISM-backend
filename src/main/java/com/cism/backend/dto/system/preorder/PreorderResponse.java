package com.cism.backend.dto.system.preorder;

import java.math.BigDecimal;
import java.time.Instant;

public record PreorderResponse(
        Long id,
        Long itemId,
        String itemName,
        BigDecimal price,
        Long variationId,
        String variationName,
        Long stallId,
        String stallName,
        Integer initialStock,
        Integer quantity,
        Instant createdAt
) {}
