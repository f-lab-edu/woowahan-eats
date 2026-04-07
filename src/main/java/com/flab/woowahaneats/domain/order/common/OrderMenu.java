package com.flab.woowahaneats.domain.order.common;

import jakarta.persistence.Embeddable;

@Embeddable
public record OrderMenu(
        Long menuId,
        String menuName,
        int price,
        int quantity
) {
}
