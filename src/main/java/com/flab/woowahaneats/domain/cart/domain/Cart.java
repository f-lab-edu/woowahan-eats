package com.flab.woowahaneats.domain.cart.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class Cart {
    private UUID id;
    private Long userId;
    private Long restaurantId;
    private List<CartMenu> menus;

    public static Cart create(Long userId, Long restaurantId, List<CartMenu> menus) {
        return Cart.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .restaurantId(restaurantId)
                .menus(menus)
                .build();
    }

    public Cart updateRestaurant(Long restaurantId) {
        return this.toBuilder()
                .restaurantId(restaurantId)
                .build();
    }
}
