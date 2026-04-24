package com.cism.backend.exception;

public class NotfoundException extends RuntimeException {
    private final String code;
    public NotfoundException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
