package com.flab.woowahaneats.domain.cart.repository;

import com.flab.woowahaneats.domain.cart.domain.Cart;

import java.util.UUID;

public interface CartRepository {
    void save(Cart cart);
    Cart findById(UUID cartId);
}
