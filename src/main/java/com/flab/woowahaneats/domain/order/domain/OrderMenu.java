package com.flab.woowahaneats.domain.order.domain;

public record OrderMenu(
        Long menuId,
        String menuName,
        int price,
        int quantity
) {
}
