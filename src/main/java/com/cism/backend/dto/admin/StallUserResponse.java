package com.cism.backend.dto.admin;

public record StallUserResponse(
    long id,
    long stallId,
    String name, 
    String description, 
    String image, 
    String openAt, 
    String closeAt
) {

}
