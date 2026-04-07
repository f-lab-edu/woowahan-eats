package com.flab.woowahaneats.domain.cart.exception;

import org.springframework.http.HttpStatus;

public class CartNotBelongToUserException extends CartException {
    public CartNotBelongToUserException() {
        super(
                "해당 장바구니에 접근할 권한이 없습니다.",
                "CART_NOT_BELONG_TO_USER",
                HttpStatus.FORBIDDEN
        );
    }
}