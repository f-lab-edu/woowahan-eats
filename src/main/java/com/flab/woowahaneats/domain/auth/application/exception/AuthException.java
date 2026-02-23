package com.flab.woowahaneats.domain.auth.application.exception;

import com.flab.woowahaneats.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class AuthException extends BusinessException {
    protected AuthException(String message, String errorCode, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }
}