package com.cism.backend.util;

import org.springframework.http.ResponseCookie;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;

@Component
public class CookieUtil {

    @Value("${app.jwt.cookie.secure:true}")
    private boolean isSecure;

    @Value("${app.jwt.cookie.same-site:None}")
    private String sameSite;

    @PostConstruct
    public void init() {
        System.out.println("CookieUtil initialized: Secure=" + isSecure + ", SameSite=" + sameSite);
    }

    public void addCookie(HttpServletResponse response, String name, String value, long maxAgeInMs) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(isSecure)
                .path("/")
                .maxAge(Duration.ofMillis(maxAgeInMs))
                .sameSite(sameSite)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public void clearCookie(HttpServletResponse response, String name) {
        ResponseCookie cookie = ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(isSecure)
                .path("/")
                .maxAge(0)
                .sameSite(sameSite)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
}
