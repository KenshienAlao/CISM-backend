package com.cism.backend.dto.common;

public record Api<T>(
    boolean success,
    String message,
    String code,
    T data
) {
    public static <T> Api<T> ok(String message, String code, T data) {
        return new Api<T>(true, message, code, data);
    }

    public static <T> Api<T> error(String message, String code, T data) {
        return new Api<T>(false, message, code, data);
    }

}
