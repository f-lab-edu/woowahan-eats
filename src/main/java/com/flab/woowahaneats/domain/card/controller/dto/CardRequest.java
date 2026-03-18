package com.flab.woowahaneats.domain.card.controller.dto;

import com.flab.woowahaneats.domain.card.domain.CardCompany;
import com.flab.woowahaneats.domain.card.domain.CardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CardRequest(
        @NotNull
        CardCompany cardCompany,

        @NotBlank
        String cardNumber,

        @NotBlank
        String expiryDate,

        @NotNull
        CardType cardType
){}
