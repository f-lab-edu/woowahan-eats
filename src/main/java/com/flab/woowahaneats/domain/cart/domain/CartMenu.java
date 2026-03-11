package com.flab.woowahaneats.domain.cart.domain;

import com.flab.woowahaneats.domain.cart.application.exception.InvalidCartException;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class CartMenu {
    private Long id;
    private Long cartId;
    private Long menuId;
    private int quantity;

    public static CartMenu create(Long id, Long cartId, Long menuId, int quantity) {
        validateQuantity(quantity);

        return CartMenu.builder()
                .id(id)
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
