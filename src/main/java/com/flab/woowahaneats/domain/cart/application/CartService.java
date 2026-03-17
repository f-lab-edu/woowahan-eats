package com.flab.woowahaneats.domain.cart.application;

import com.flab.woowahaneats.domain.auth.AuthContextHolder;
import com.flab.woowahaneats.domain.cart.application.exception.CartNotBelongToUserException;
import com.flab.woowahaneats.domain.cart.application.exception.CartNotFoundException;
import com.flab.woowahaneats.domain.cart.application.exception.RestaurantMismatchException;
import com.flab.woowahaneats.domain.cart.domain.Cart;
import com.flab.woowahaneats.domain.cart.domain.CartMenu;
import com.flab.woowahaneats.domain.cart.repository.CartRepository;
import com.flab.woowahaneats.domain.member.domain.User;
import com.flab.woowahaneats.domain.menu.application.exception.MenuNotFoundException;
import com.flab.woowahaneats.domain.menu.domain.Menu;
import com.flab.woowahaneats.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final MenuRepository menuRepository;

    public void createCart(Long userId, Long restaurantId, List<CartMenu> menus) {
        Cart cart = Cart.create(userId, restaurantId, menus);
        cartRepository.save(cart);
    }

    public Cart getCart(UUID cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        validateCartOwnership(cart);
        return cart;
    }

    public void updateMenuQuantity(UUID cartId, Long menuId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        validateCartOwnership(cart);
        Cart updatedCart = cart.updateMenuQuantity(menuId, quantity);
        cartRepository.save(updatedCart);
    }

    public void deleteCartMenu(UUID cartId, Long menuId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        validateCartOwnership(cart);
        Cart updatedCart = cart.removeMenu(menuId);
        cartRepository.save(updatedCart);
    }

    public void deleteCart(UUID cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        validateCartOwnership(cart);
        cartRepository.deleteById(cartId);
    }

    public void addCartMenu(UUID cartId, CartMenu cartMenu) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        validateCartOwnership(cart);

        Menu menu = menuRepository.findById(cartMenu.menuId())
                .orElseThrow(MenuNotFoundException::new);
        validateMenuBelongsToRestaurant(menu, cart.getRestaurantId());

        Cart updatedCart = cart.addMenu(cartMenu);
        cartRepository.save(updatedCart);
    }

    private void validateCartOwnership(Cart cart) {
        User user = AuthContextHolder.getContext().getUser();
        if (!cart.getUserId().equals(user.getId())) {
            throw new CartNotBelongToUserException();
        }
    }

    private void validateMenuBelongsToRestaurant(Menu menu, Long restaurantId) {
        if (!menu.getRestaurantId().equals(restaurantId)) {
            throw new RestaurantMismatchException();
        }
    }
}
