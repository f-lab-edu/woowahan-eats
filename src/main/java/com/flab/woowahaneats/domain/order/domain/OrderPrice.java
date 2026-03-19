package com.flab.woowahaneats.domain.order.domain;

public record OrderPrice(
        int menuTotalPrice,
        int deliveryFee,
        int totalPrice
) {
    public static OrderPrice of(int menuTotalPrice, int deliveryFee) {
        return new OrderPrice(menuTotalPrice, deliveryFee, menuTotalPrice + deliveryFee);
    }
}

