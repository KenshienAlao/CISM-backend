package com.cism.backend.service;

import com.cism.backend.repository.RegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cism.backend.dto.LoginDto;
import com.cism.backend.dto.RegisterDto;
import com.cism.backend.exception.BadrequestException;
import com.cism.backend.model.AuthModel;

import com.cism.backend.config.JwtTokenProvider;
import jakarta.transaction.Transactional;

@Service
public class AuthService {

    @Autowired
    private RegisterRepository registerRepository;

    @Autowired
    private OtpService otpService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    AuthService(RegisterRepository registerRepository, OtpService otpService, JwtTokenProvider jwtTokenProvider) {
        this.registerRepository = registerRepository;
        this.otpService = otpService;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Transactional
    public RegisterDto registerService(RegisterDto entity) throws Exception {

        String email = entity.email();
        String studentId = entity.studentId();
        String username = entity.username();
        String password = entity.password();
        String otp = entity.otp();

        if (isBlank(email) || isBlank(studentId) || isBlank(username) || isBlank(password) || isBlank(otp)) {
            throw new BadrequestException("All fields and OTP are required", "ALL_FIELDS_REQUIRED");
        }

        // Verify and Delete OTP
        otpService.verifyOtp(email, otp);

        if (!isBlank(studentId)){
            if (registerRepository.findByStudentId(studentId).isPresent()){
                throw new BadrequestException("Student ID already exists", "STUDENT_ID_ALREADY_EXISTS");
            }
        }

        if (!isBlank(email)){
            if (registerRepository.findByEmail(email).isPresent()){
                throw new BadrequestException("Email already exists", "EMAIL_ALREADY_EXISTS");
            }
        }

        
        AuthModel response = AuthModel.builder()
            .studentId(studentId)
            .email(email)
            .username(username)
            .password(passwordEncoder.encode(password))
            .build();

        registerRepository.save(response);
        return new RegisterDto(studentId, email, username, password, otp) ;
    }


    @Transactional
    public LoginDto loginService(LoginDto entity) {
        String email = entity.email();
        String password = entity.password();

        if (isBlank(email) || isBlank(password)) {
            throw new BadrequestException("Email and password are required", "EMAIL_AND_PASSWORD_REQUIRED");
        }

        AuthModel user = registerRepository.findByEmail(email).orElseThrow(() -> new BadrequestException("User not found", "USER_NOT_FOUND"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadrequestException("Invalid password", "INVALID_PASSWORD");
        }

        String accessToken = jwtTokenProvider.generateToken(user.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());

        return new LoginDto(email, password, accessToken, refreshToken, user);
    }

    public boolean isBlank(String entity){
        return entity == null || entity.isBlank();
    }
}
