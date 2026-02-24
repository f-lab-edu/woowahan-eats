package com.flab.woowahaneats.domain.auth.application.exception;

import org.springframework.http.HttpStatus;

public class AccountNotFoundException extends AuthException {
    public AccountNotFoundException() {
        super(
                "해당 email의 계정이 없습니다.",
                "ACCOUNT_NOT_FOUND",
                HttpStatus.NOT_FOUND
        );
    }
}