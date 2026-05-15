package com.cism.backend.dto.users;

public record ChangeEmailDto(
    String newEmail,
    String password
) {}
