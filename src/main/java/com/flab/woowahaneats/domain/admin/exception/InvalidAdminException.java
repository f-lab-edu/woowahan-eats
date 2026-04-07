package com.flab.woowahaneats.domain.admin.exception;

import org.springframework.http.HttpStatus;

public class InvalidAdminException extends AdminException {
    public InvalidAdminException(String message) {
        super(
                message,
                "INVALID_ADMIN",
                HttpStatus.BAD_REQUEST
        );
    }
}
