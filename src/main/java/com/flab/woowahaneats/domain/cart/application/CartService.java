package com.flab.woowahaneats.domain.cart.application;

import com.flab.woowahaneats.domain.cart.application.exception.CartNotFoundException;
import com.flab.woowahaneats.domain.cart.domain.Cart;
import com.flab.woowahaneats.domain.cart.domain.CartMenu;
import com.flab.woowahaneats.domain.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public void createCart(Long userId, Long restaurantId, List<CartMenu> menus) {
        Cart cart = Cart.create(userId, restaurantId, menus);
        cartRepository.save(cart);
    }

    public Cart getCart(UUID cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
    }

    public void updateMenuQuantity(UUID cartId, Long menuId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        Cart updatedCart = cart.updateMenuQuantity(menuId, quantity);
        cartRepository.save(updatedCart);
    }

    public void deleteCartMenu(UUID cartId, Long menuId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        Cart updatedCart = cart.removeMenu(menuId);
        cartRepository.save(updatedCart);
    }

    public void deleteCart(UUID cartId) {
        cartRepository.deleteById(cartId);
    }

    public void addCartMenu(UUID cartId, CartMenu menu) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        Cart updatedCart = cart.addMenu(menu);
        cartRepository.save(updatedCart);
    }
}
