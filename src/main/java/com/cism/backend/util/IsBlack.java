package com.cism.backend.util;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Component
public class IsBlack {
    

    public Boolean isBlank(String value) {
        return value == null || value.isBlank() || value.trim().isEmpty(); 
    }

    public Boolean isBlankInteger(Integer value) {
        return value == null || value < 0;
    }

    public Boolean isBlankMultipartFile(MultipartFile file) {
        return file == null || file.isEmpty(); 
    }

    public Boolean isBlankBigDecimal(BigDecimal value) {
        return value == null || value.compareTo(BigDecimal.ZERO) < 0;
    }

    public Boolean isBlankLong(Long value) {
        return value == null || value < 0;
    }
}
