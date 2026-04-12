package com.flab.woowahaneats.domain.cart.application;

import com.flab.woowahaneats.domain.cart.domain.Cart;
import com.flab.woowahaneats.domain.cart.domain.CartMenu;

import java.util.List;

public interface CartService {

    void createCart(Long restaurantId, List<CartMenu> menus);

    Cart getCart(Long cartId);

    void updateMenuQuantity(Long cartId, Long menuId, int quantity);

    void deleteCartMenu(Long cartId, Long menuId);

    void deleteCart(Long cartId);

    void addCartMenu(Long cartId, CartMenu cartMenu);

    void deleteCartBySystem(Long cartId);
}
