package com.cism.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cism.backend.dto.LoginDto;
import com.cism.backend.dto.RegisterDto;
import com.cism.backend.dto.common.Api;
import com.cism.backend.service.AuthService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Api<RegisterDto>> register(@RequestBody RegisterDto entity) throws Exception {  
        RegisterDto success = authService.registerService(entity);
        return ResponseEntity.ok(Api.ok("User registered successfully", "USER_REGISTERED", success));
    }

    @PostMapping("/login")
    public ResponseEntity<Api<LoginDto>> login(@RequestBody LoginDto entity) throws Exception {
        LoginDto success = authService.loginService(entity);
        return ResponseEntity.ok(Api.ok("Login success", "LOGIN_SUCCESS", success));
    }
}
