package com.flab.woowahaneats.domain.member.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record OwnerLoginRequest(

        @NotBlank
        String password,

        @Email
        @NotBlank
        String email
) {
}
