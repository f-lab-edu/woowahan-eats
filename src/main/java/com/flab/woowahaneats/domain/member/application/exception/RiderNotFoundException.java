package com.flab.woowahaneats.domain.member.application.exception;

import org.springframework.http.HttpStatus;

public class RiderNotFoundException extends MemberException {
    public RiderNotFoundException() {
        super(
                "Rider를 찾을 수 없습니다.",
                "RIDER_NOT_FOUND",
                HttpStatus.NOT_FOUND
        );
    }
}