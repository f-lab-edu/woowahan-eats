package com.flab.woowahaneats.domain.admin.exception;

import org.springframework.http.HttpStatus;

public class AdminNotFoundException extends AdminException {
    public AdminNotFoundException() {
        super(
                "Admin을 찾을 수 없습니다.",
                "ADMIN_NOT_FOUND",
                HttpStatus.NOT_FOUND
        );
    }
}