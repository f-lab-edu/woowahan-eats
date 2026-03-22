package com.flab.woowahaneats.domain.order.exception;

import com.flab.woowahaneats.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class OrderException extends BusinessException {
    public OrderException(String message, String errorCode, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }
}