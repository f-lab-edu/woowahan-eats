package com.flab.woowahaneats.domain.menu.exception;

import org.springframework.http.HttpStatus;

public class MenuNotFoundException extends MenuException {
    public MenuNotFoundException() {
        super(
                "메뉴를 찾을 수 없습니다.",
                "MENU_NOT_FOUND",
                HttpStatus.NOT_FOUND
        );
    }
}