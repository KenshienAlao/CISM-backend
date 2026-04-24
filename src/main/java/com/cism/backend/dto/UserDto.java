package com.cism.backend.dto;

public record UserDto(
    String email,
    String studentId,
    String username,
    String avatar,
    String password,
    String role
) {
    
}
