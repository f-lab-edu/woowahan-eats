package com.flab.woowahaneats.domain.user.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends UserException {
    public UserNotFoundException() {
        super(
                "User를 찾을 수 없습니다.",
                "USER_NOT_FOUND",
                HttpStatus.NOT_FOUND
        );
    }
}