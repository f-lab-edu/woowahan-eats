package com.flab.woowahaneats.domain.member.application.exception;

import com.flab.woowahaneats.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class MemberException extends BusinessException {
    protected MemberException(String message, String errorCode, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }
}