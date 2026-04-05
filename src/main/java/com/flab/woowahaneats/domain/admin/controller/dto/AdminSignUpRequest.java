package com.flab.woowahaneats.domain.admin.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdminSignUpRequest(
        @NotNull
        Long id,

        @NotBlank
        String name,

        @NotBlank
        String password,

        @Email
        @NotBlank
        String email,

        @NotBlank
        String phoneNumber
) {
}