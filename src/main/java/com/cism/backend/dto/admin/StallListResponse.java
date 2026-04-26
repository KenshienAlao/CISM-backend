package com.cism.backend.dto.admin;

import java.math.BigDecimal;
import java.time.Instant;

public record StallListResponse(
    long id,
    String licence,
    String password,
    UserModel user,
    IncomesModel incomes
) {
    public record UserModel(
        long id,
        long stallId,
        String name,
        String description,
        String image,
        Boolean status,
        String openAt,
        String closeAt,
        Instant createdAt,
        Instant updatedAt
    ) {}

    public record IncomesModel(
        long id,
        long stallId,
        BigDecimal income,
        Instant earnedAt,
        Instant createdAt
    ) {}

}
