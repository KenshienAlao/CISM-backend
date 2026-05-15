package com.cism.backend.dto.users;

public record ChangePasswordDto(
    String oldPassword,
    String newPassword
) {}
