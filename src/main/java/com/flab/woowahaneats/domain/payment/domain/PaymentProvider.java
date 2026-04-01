package com.flab.woowahaneats.domain.payment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentProvider {
    TOSS("토스페이먼츠");

    private final String displayName;
}