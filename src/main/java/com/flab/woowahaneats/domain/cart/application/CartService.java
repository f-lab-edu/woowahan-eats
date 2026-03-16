package com.flab.woowahaneats.domain.cart.application;

import com.flab.woowahaneats.domain.cart.domain.Cart;
import com.flab.woowahaneats.domain.cart.domain.CartMenu;
import com.flab.woowahaneats.domain.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public void createCart(Long userId, Long restaurantId, List<CartMenu> menus) {
        Cart cart = Cart.create(userId, restaurantId, menus);
        cartRepository.save(cart);
    }
}
