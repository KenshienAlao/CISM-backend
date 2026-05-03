package com.cism.backend.dto.system.review;

import java.time.Instant;

public record ReviewResponse(
        User user,
        String comment,
        Integer star,
        Long itemId,
        Instant createAt) {

    public record User(
            String clientName,
            String avatar,
            String role) {
    }

}
