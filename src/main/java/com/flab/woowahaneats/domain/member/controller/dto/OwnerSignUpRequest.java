package com.flab.woowahaneats.domain.member.controller.dto;

import com.flab.woowahaneats.domain.common.vo.Address;
import com.flab.woowahaneats.domain.common.vo.BankAccount;
import com.flab.woowahaneats.domain.common.vo.Location;
import jakarta.validation.Valid;
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

        @Valid
        @NotNull
        Address address,

        @Valid
        @NotNull
        Location location,

        @NotBlank
        String businessRegistrationCertUrl,

        @NotBlank
        String businessNotificationCertUrl,

        @Valid
        @NotNull
        BankAccount bankAccount
){}
