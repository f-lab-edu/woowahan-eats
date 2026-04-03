package com.flab.woowahaneats.domain.member.application.exception;

import org.springframework.http.HttpStatus;

public class InvalidRiderStatusException extends MemberException {
    public InvalidRiderStatusException(String message) {
        super(
                message,
                "INVALID_RIDER_STATUS",
                HttpStatus.BAD_REQUEST
        );
    }
}