package com.cism.backend.dto;

public record RegisterDto(
        String username,
        String studentId,
        String email,
        String password,
        String otp
) {
        
}
