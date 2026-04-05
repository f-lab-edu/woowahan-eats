package com.flab.woowahaneats.domain.owner.application.exception;

import org.springframework.http.HttpStatus;

public class OwnerNotFoundException extends OwnerException {
    public OwnerNotFoundException() {
        super(
                "Owner를 찾을 수 없습니다.",
                "OWNER_NOT_FOUND",
                HttpStatus.NOT_FOUND
        );
    }
}