package com.flab.woowahaneats.domain.cart.repository;

import com.flab.woowahaneats.domain.cart.domain.Cart;

public interface CartRepository {
    void save(Cart cart);
}
