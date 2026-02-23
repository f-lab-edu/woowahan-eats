package com.flab.woowahaneats.domain.member.application.exception;

import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends MemberException {
    public InvalidPasswordException() {
        super(
                "비밀번호가 일치하지 않습니다.",
                "INVALID_PASSWORD",
                HttpStatus.UNAUTHORIZED
        );
    }
}