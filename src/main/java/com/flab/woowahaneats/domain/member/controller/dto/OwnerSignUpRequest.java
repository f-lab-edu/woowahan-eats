package com.flab.woowahaneats.domain.member.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OwnerSignUpRequest (

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
        String phoneNumber,

        @NotBlank
        String address,

        @NotBlank
        String businessNumber,

        @NotBlank
        String accountNumber,

        @NotBlank
        String bankName
){}
