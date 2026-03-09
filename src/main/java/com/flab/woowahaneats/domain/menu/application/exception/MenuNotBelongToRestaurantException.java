package com.flab.woowahaneats.domain.menu.application.exception;

import org.springframework.http.HttpStatus;

public class MenuNotBelongToRestaurantException extends MenuException {
    public MenuNotBelongToRestaurantException() {
        super(
                "해당 음식점의 메뉴가 아닙니다.",
                "MENU_NOT_BELONG_TO_RESTAURANT",
                HttpStatus.FORBIDDEN
        );
    }
}