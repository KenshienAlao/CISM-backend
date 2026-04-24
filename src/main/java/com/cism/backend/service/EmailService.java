package com.cism.backend.service;

import com.cism.backend.dto.OtpDto;
import com.cism.backend.exception.BadrequestException;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final Resend resend;

    public EmailService(@Value("${resend.api-key}") String apiKey) {
        this.resend = new Resend(apiKey);
    }

    public OtpDto sendOtpEmail(String to, String otp) throws Exception {
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("onboarding@resend.dev") 
                .to(to)
                .subject("OTP Verification")
                .html("<p>Your verification code is: <strong>" + otp + "</strong></p>")
                .build();

        CreateEmailResponse response = resend.emails().send(params);

        if (response == null || response.getId() == null) throw new BadrequestException("Failed to send OTP", "OTP_FAILED");
        return new OtpDto(to, otp);
    }
}
