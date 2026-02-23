package com.flab.woowahaneats.domain.member.application.exception;

import org.springframework.http.HttpStatus;

public class AccountNotFoundException extends MemberException {
    public AccountNotFoundException() {
        super(
                "해당 email의 계정이 없습니다.",
                "ACCOUNT_NOT_FOUND",
                HttpStatus.NOT_FOUND
        );
    }
}