package com.cism.backend.dto.system.review;

import java.time.Instant;

public record ReviewResponse(
    Long id,
    Long stallId,
    Long userId,
    Long itemId,
    Integer star,
    String comment,
    Instant createAt
) {
    
}
