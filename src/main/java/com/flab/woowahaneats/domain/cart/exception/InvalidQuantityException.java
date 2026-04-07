package com.flab.woowahaneats.domain.cart.exception;

import org.springframework.http.HttpStatus;

public class InvalidQuantityException extends CartException {
    public InvalidQuantityException() {
        super(
                "수량은 1개 이상 99개 이하여야 합니다.",
                "INVALID_QUANTITY",
                HttpStatus.BAD_REQUEST
        );
    }
}