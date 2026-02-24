package com.flab.woowahaneats.domain.member.application.exception;

import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends MemberException {
    public DuplicateEmailException() {
        super(
                "이미 사용 중인 이메일입니다.",
                "DUPLICATE_EMAIL",
                HttpStatus.CONFLICT
        );
    }
}