package com.flab.woowahaneats.domain.cart.repository;

import com.flab.woowahaneats.domain.cart.domain.Cart;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository {
    void save(Cart cart);
    Optional<Cart> findById(UUID cartId);
    void deleteById(UUID cartId);
}
