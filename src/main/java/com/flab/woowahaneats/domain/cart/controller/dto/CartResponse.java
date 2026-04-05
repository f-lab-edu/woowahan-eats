package com.flab.woowahaneats.domain.cart.controller.dto;

import com.flab.woowahaneats.domain.cart.domain.Cart;
import com.flab.woowahaneats.domain.cart.domain.CartMenu;

import java.util.List;

public record CartResponse(
        Long id,
        Long userId,
        Long restaurantId,
        List<CartMenu> menus
) {
    public static CartResponse from(Cart cart) {
        return new CartResponse(
                cart.getId(),
                cart.getUserId(),
                cart.getRestaurantId(),
                cart.getMenus()
        );
    }
}
