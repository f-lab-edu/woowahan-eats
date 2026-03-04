package com.flab.woowahaneats.domain.member.controller.dto;

import com.flab.woowahaneats.domain.common.vo.BankAccount;
import com.flab.woowahaneats.domain.common.vo.Location;
import com.flab.woowahaneats.domain.member.domain.VehicleType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RiderSignUpRequest(
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
        Location location,

        @Valid
        @NotNull
        BankAccount bankAccount,

        @NotNull
        VehicleType vehicleType
) {
}