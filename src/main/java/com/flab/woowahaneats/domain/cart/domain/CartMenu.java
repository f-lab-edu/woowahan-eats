package com.flab.woowahaneats.domain.cart.domain;

import com.flab.woowahaneats.domain.cart.application.exception.InvalidCartException;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class CartMenu {
    private UUID id;
    private UUID cartId;
    private Long menuId;
    private int quantity;

    public static CartMenu create(UUID id, UUID cartId, Long menuId, int quantity) {
        validateQuantity(quantity);

        return CartMenu.builder()
                .id(UUID.randomUUID())
                .cartId(cartId)
                .menuId(menuId)
                .quantity(quantity)
                .build();
    }

    public CartMenu updateQuantity(int newQuantity) {
        validateQuantity(newQuantity);
        return this.toBuilder()
                .quantity(newQuantity)
                .build();
    }

    private static void validateQuantity(int quantity) {
        if (quantity < 1 || quantity > 99) {
            throw new InvalidCartException("수량은 1개 이상 99개 이하여야 합니다");
        }
    }
}
