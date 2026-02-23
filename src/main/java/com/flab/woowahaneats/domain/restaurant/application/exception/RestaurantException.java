package com.flab.woowahaneats.domain.restaurant.application.exception;

import com.flab.woowahaneats.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class RestaurantException extends BusinessException {
    public RestaurantException(String message, String errorCode, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }
}
