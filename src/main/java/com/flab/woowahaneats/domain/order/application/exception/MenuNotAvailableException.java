package com.flab.woowahaneats.domain.order.application.exception;

import org.springframework.http.HttpStatus;

public class MenuNotAvailableException extends OrderException {
    public MenuNotAvailableException(String menuName) {
        super(
                menuName + "은(는) 현재 판매하지 않습니다.",
                "MENU_NOT_AVAILABLE",
                HttpStatus.BAD_REQUEST
        );
    }
}