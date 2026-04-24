package com.cism.backend.util;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;

@Component
public class CookieUtil {

    @Value("${app.env:prod}")
    private String env; // for local development set to "dev" and for production set to "prod"

    public void addCookie(HttpServletResponse response, String name, String value, long maxAgeInMs) {
        boolean isProd = "prod".equalsIgnoreCase(env);

        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(isProd) 
                .path("/")
                .maxAge(Duration.ofMillis(maxAgeInMs))
                .sameSite(isProd ? "None" : "Lax") 
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public void clearCookie(HttpServletResponse response, String name) {
        boolean isProd = "prod".equalsIgnoreCase(env);

        ResponseCookie cookie = ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(isProd)
                .path("/")
                .maxAge(0)
                .sameSite(isProd ? "None" : "Lax")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
}
