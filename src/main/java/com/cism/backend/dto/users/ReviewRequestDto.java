package com.cism.backend.dto.users;

public record ReviewRequestDto(
        Long stallId,
        Long itemId,
        Integer star,
        String comment) {

}
