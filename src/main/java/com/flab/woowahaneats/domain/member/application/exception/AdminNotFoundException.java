package com.flab.woowahaneats.domain.member.application.exception;

import org.springframework.http.HttpStatus;

public class AdminNotFoundException extends MemberException {
    public AdminNotFoundException() {
        super(
                "Admin을 찾을 수 없습니다.",
                "ADMIN_NOT_FOUND",
                HttpStatus.NOT_FOUND
        );
    }
}