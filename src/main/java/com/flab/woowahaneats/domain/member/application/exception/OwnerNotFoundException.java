package com.flab.woowahaneats.domain.member.application.exception;

import org.springframework.http.HttpStatus;

public class OwnerNotFoundException extends MemberException {
    public OwnerNotFoundException() {
        super(
                "Owner를 찾을 수 없습니다.",
                "OWNER_NOT_FOUND",
                HttpStatus.NOT_FOUND
        );
    }
}