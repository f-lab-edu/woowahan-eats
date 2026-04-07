package com.flab.woowahaneats.domain.rider.exception;

import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends RiderException {
    public DuplicateEmailException() {
        super(
                "이미 사용 중인 이메일입니다.",
                "DUPLICATE_EMAIL",
                HttpStatus.CONFLICT
        );
    }
}