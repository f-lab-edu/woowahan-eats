package com.flab.woowahaneats.domain.user.application.exception;

import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends UserException {
    public DuplicateEmailException() {
        super(
                "이미 사용 중인 이메일입니다.",
                "DUPLICATE_EMAIL",
                HttpStatus.CONFLICT
        );
    }
}