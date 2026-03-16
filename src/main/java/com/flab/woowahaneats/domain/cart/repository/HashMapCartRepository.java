package com.flab.woowahaneats.domain.cart.repository;

import com.flab.woowahaneats.domain.cart.domain.Cart;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.UUID;

@Repository
public class HashMapCartRepository implements CartRepository {

    HashMap<UUID, Cart> cartMap = new HashMap<>();

    @Override
    public void save(Cart cart) {
        cartMap.put(cart.getId(), cart);
    }

    @Override
    public Cart findById(UUID cartId) {
        return cartMap.get(cartId);
    }

    @Override
    public void deleteById(UUID cartId) {
        cartMap.remove(cartId);
    }
}
