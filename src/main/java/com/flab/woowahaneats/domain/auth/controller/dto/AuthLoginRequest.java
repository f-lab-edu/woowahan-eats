package com.flab.woowahaneats.domain.auth.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(
        @NotBlank
        String password,

        @Email
        @NotBlank
        String email
) {
}
