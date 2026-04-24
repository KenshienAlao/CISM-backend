package com.cism.backend.exception;

public class ExistException extends RuntimeException {
    private final String code;
    public ExistException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
