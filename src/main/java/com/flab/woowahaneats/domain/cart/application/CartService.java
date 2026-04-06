package com.flab.woowahaneats.domain.cart.application;

import com.flab.woowahaneats.domain.auth.AuthContextHolder;
import com.flab.woowahaneats.domain.cart.exception.CartNotBelongToUserException;
import com.flab.woowahaneats.domain.cart.exception.CartNotFoundException;
import com.flab.woowahaneats.domain.cart.exception.RestaurantMismatchException;
import com.flab.woowahaneats.domain.cart.domain.Cart;
import com.flab.woowahaneats.domain.cart.domain.CartMenu;
import com.flab.woowahaneats.domain.cart.repository.CartRepository;
import com.flab.woowahaneats.domain.restaurant.exception.RestaurantNotFoundException;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import com.flab.woowahaneats.domain.restaurant.repository.RestaurantRepository;
import com.flab.woowahaneats.domain.user.domain.User;
import com.flab.woowahaneats.domain.menu.exception.MenuNotFoundException;
import com.flab.woowahaneats.domain.menu.domain.Menu;
import com.flab.woowahaneats.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    public void createCart(Long restaurantId, List<CartMenu> menus) {
        User user = AuthContextHolder.getContext().getUser();
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);
        Cart cart = Cart.create(user, restaurant, menus);
        cartRepository.save(cart);
    }

    public Cart getCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        validateCartOwnership(cart);
        return cart;
    }

    public void updateMenuQuantity(Long cartId, Long menuId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        validateCartOwnership(cart);
        Cart updatedCart = cart.updateMenuQuantity(menuId, quantity);
        cartRepository.save(updatedCart);
    }

    public void deleteCartMenu(Long cartId, Long menuId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        validateCartOwnership(cart);
        Cart updatedCart = cart.removeMenu(menuId);
        cartRepository.save(updatedCart);
    }

    public void deleteCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        validateCartOwnership(cart);
        cartRepository.deleteById(cartId);
    }

    public void addCartMenu(Long cartId, CartMenu cartMenu) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        validateCartOwnership(cart);

        Menu menu = menuRepository.findById(cartMenu.menuId())
                .orElseThrow(MenuNotFoundException::new);
        validateMenuBelongsToRestaurant(menu, cart.getRestaurant().getId());

        Cart updatedCart = cart.addMenu(cartMenu);
        cartRepository.save(updatedCart);
    }

    private void validateCartOwnership(Cart cart) {
        User user = AuthContextHolder.getContext().getUser();
        if (!cart.getUser().getId().equals(user.getId())) {
            throw new CartNotBelongToUserException();
        }
    }

    private void validateMenuBelongsToRestaurant(Menu menu, Long restaurantId) {
        if (!menu.getRestaurant().getId().equals(restaurantId)) {
            throw new RestaurantMismatchException();
        }
    }
}
