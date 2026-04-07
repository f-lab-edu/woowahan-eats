package com.flab.woowahaneats.domain.common.vo;

import jakarta.persistence.Embeddable;

@Embeddable
public record BankAccount(
         String accountNumber,
         String bankName
) {
}
