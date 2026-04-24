package com.cism.backend.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cism.backend.dto.OtpDto;
import com.cism.backend.exception.BadrequestException;
import com.cism.backend.model.OtpModel;
import com.cism.backend.repository.OtpRepository;


@Service
public class OtpService {

    private static final int COOLDOW_MINUTES = 5;

    private final OtpRepository otpRepository;

    public OtpService(OtpRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    
    @Transactional
    public OtpDto storeOtp(String email, String otp, String ipAddress) {

        Instant fiveMinAgo = Instant.now().minus(Duration.ofMinutes(COOLDOW_MINUTES));


        long requestCount = otpRepository.countByIpAddressAndCreateAtAfter(ipAddress, fiveMinAgo);

        if (requestCount >= 5) { // people
            throw new BadrequestException("Too many requests for this network, please try again later", "TOO_MANY_REQUESTS");
        }


        Optional<OtpModel> existing = otpRepository.findByEmail(email);
        if (existing.isPresent()) {
            Instant created = existing.get().getCreateAt();
            Instant expiryTime = created.plus(Duration.ofMinutes(COOLDOW_MINUTES));

            if (expiryTime.isAfter(Instant.now())) {
                long remainingSeconds = Duration.between(Instant.now(), expiryTime).getSeconds();
                throw new BadrequestException(
                    "You already have an Valid OTP, please wait " + ((remainingSeconds / 60) + 1) + " minutes before requesting a new OTP", 
                    "OTP_ALREADY_SENT",
                    Map.of("retryAfterSeconds", remainingSeconds)
                );
            }
            otpRepository.delete(existing.get());
            otpRepository.flush(); // Ensure the old OTP is deleted before saving a new one with the same email
        }

        OtpModel newEntry = OtpModel.builder()
            .email(email)
            .otp(otp)
            .ipAddress(ipAddress)
            .createAt(Instant.now())
            .build();

            otpRepository.save(newEntry);
            return new OtpDto(email, otp);
        }


    @Transactional
    public OtpDto verifyOtp(String email, String codeFromUser) throws Exception {
        OtpModel stored = otpRepository.findByEmail(email).orElseThrow(() -> new BadrequestException("OTP not found", "OTP_NOT_FOUND"));

        if (stored.getCreateAt().plus(Duration.ofMinutes(COOLDOW_MINUTES)).isBefore(Instant.now())) {
            otpRepository.delete(stored);
            otpRepository.flush();
            throw new BadrequestException("OTP expired", "OTP_EXPIRED");
        }

        if (!stored.getOtp().equals(codeFromUser)) throw new BadrequestException("OTP not match", "OTP_NOT_MATCH");
        otpRepository.delete(stored);
        otpRepository.flush();
        return new OtpDto(email, codeFromUser);
    }

}
