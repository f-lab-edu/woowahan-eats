package com.flab.woowahaneats.domain.cart.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class Cart {
    private Long id;
    private Long userId;
    private Long restaurantId;

    public static Cart create(Long id, Long userId, Long restaurantId) {
        return Cart.builder()
                .id(id)
                .userId(userId)
                .restaurantId(restaurantId)
                .build();
    }

    public Cart updateRestaurant(Long restaurantId) {
        return this.toBuilder()
                .restaurantId(restaurantId)
                .build();
    }
}
