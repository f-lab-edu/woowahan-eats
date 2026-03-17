package com.flab.woowahaneats.domain.cart.application.exception;

import org.springframework.http.HttpStatus;

public class CartNotFoundException extends CartException {
    public CartNotFoundException() {
        super(
                "장바구니를 찾을 수 없습니다.",
                "CART_NOT_FOUND",
                HttpStatus.NOT_FOUND
        );
    }
}