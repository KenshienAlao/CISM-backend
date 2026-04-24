package com.cism.backend.exception;

public class BadrequestException extends RuntimeException {
    private final String code;
    private final Object data;

    public BadrequestException(String message, String code) {
        this(message, code, null);
    }

    public BadrequestException(String message, String code, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }
}
