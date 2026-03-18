package com.flab.woowahaneats.domain.card.application.exception;

import com.flab.woowahaneats.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class CardException extends BusinessException {
    public CardException(String message, String errorCode, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }
}