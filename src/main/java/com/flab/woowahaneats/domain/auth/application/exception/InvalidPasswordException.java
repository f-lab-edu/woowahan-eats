package com.flab.woowahaneats.domain.auth.application.exception;

import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends AuthException {
    public InvalidPasswordException() {
        super(
                "비밀번호가 일치하지 않습니다.",
                "INVALID_PASSWORD",
                HttpStatus.UNAUTHORIZED
        );
    }
}