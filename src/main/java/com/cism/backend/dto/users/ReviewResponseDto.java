package com.cism.backend.dto.users;

public record ReviewResponseDto(
        User user,
        String comment,
        Integer star,
        String createdAt) {
    public record User(
            String clientName,
            String avatar) {
    }
}
