package com.flab.woowahaneats.domain.payment.exception;

import com.flab.woowahaneats.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class PaymentException extends BusinessException {
    public PaymentException(String message, String errorCode, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }
}