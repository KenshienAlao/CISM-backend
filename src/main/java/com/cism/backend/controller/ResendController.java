package com.cism.backend.controller;

import java.util.Random;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cism.backend.dto.OtpDto;
import com.cism.backend.dto.common.Api;
import com.cism.backend.service.EmailService;
import com.cism.backend.service.OtpService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/resend")
public class ResendController {

    private final EmailService emailService;
    private final OtpService otpService;


    public ResendController(EmailService emailService, OtpService otpService) {
        this.emailService = emailService;
        this.otpService = otpService;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<Api<OtpDto>> sendOtp(@RequestBody OtpDto entity, HttpServletRequest request) throws Exception {
        String email = entity.email();
        String ipAddress = request.getRemoteAddr();
        String otp = String.format("%06d", new Random().nextInt(1000000));
        otpService.storeOtp(email, otp, ipAddress);

        OtpDto success = emailService.sendOtpEmail(email, otp);
        return ResponseEntity.ok(Api.ok("OTP sent successfully", "OTP_SENT", success));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity <Api<OtpDto>> verifyOtp(@RequestBody OtpDto entity) throws Exception {
        String email = entity.email();
        String otp = entity.otp();
        OtpDto success = otpService.verifyOtp(email, otp);
        return ResponseEntity.ok(Api.ok("OTP verified successfully", "OTP_VERIFIED", success));
    }

}
