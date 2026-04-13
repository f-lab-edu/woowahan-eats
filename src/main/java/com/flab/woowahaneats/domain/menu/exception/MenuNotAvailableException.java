package com.flab.woowahaneats.domain.menu.exception;

import org.springframework.http.HttpStatus;

public class MenuNotAvailableException extends MenuException {
    public MenuNotAvailableException() {
        super(
                "판매 중이 아닌 메뉴입니다.",
                "MENU_NOT_AVAILABLE",
                HttpStatus.BAD_REQUEST
        );
    }
}