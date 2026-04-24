package com.cism.backend.dto;

import com.cism.backend.model.AuthModel;

public record LoginDto(
    String email, 
    String password, 
    String accessToken, 
    String refreshToken, 
    AuthModel user
) {
    
}
