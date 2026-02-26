package com.flab.woowahaneats.domain.member.application.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends MemberException {
    public UserNotFoundException() {
        super(
                "User를 찾을 수 없습니다.",
                "USER_NOT_FOUND",
                HttpStatus.NOT_FOUND
        );
    }
}