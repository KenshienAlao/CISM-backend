package com.cism.backend.dto.system.preorder;

import jakarta.validation.constraints.NotNull;

public record PreorderRequest(
        @NotNull(message = "Item ID is required") Long itemId,
        Long variationId,
        @NotNull(message = "Quantity is required") Integer quantity
) {}
