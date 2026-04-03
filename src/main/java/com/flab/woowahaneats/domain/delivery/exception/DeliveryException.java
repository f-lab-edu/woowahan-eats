package com.flab.woowahaneats.domain.delivery.exception;

import com.flab.woowahaneats.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DeliveryException extends BusinessException {
    public DeliveryException(String message, String errorCode, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }
}