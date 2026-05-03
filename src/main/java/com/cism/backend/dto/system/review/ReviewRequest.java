package com.cism.backend.dto.system.review;

public record ReviewRequest(
        Long stallId,
        Long itemId,
        String comment,
        Integer star) {

}
