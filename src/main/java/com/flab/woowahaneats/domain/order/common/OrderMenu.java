package com.flab.woowahaneats.domain.order.common;

public record OrderMenu(
        Long menuId,
        String menuName,
        int price,
        int quantity
) {
}
