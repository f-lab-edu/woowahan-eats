package com.flab.woowahaneats.domain.cart.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class Cart {
    private UUID id;
    private Long userId;
    private Long restaurantId;

    public static Cart create(UUID id, Long userId, Long restaurantId) {
        return Cart.builder()
                .id(UUID.randomUUID())
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
