package com.flab.woowahaneats.domain.cart.application;

import com.flab.woowahaneats.domain.auth.AuthContextHolder;
import com.flab.woowahaneats.domain.cart.domain.Cart;
import com.flab.woowahaneats.domain.cart.domain.CartMenu;
import com.flab.woowahaneats.domain.cart.exception.CartNotBelongToUserException;
import com.flab.woowahaneats.domain.cart.exception.CartNotFoundException;
import com.flab.woowahaneats.domain.cart.exception.RestaurantClosedException;
import com.flab.woowahaneats.domain.cart.exception.RestaurantMismatchException;
import com.flab.woowahaneats.domain.cart.repository.CartRepository;
import com.flab.woowahaneats.domain.menu.domain.Menu;
import com.flab.woowahaneats.domain.menu.exception.MenuNotFoundException;
import com.flab.woowahaneats.domain.menu.repository.MenuRepository;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import com.flab.woowahaneats.domain.restaurant.domain.RestaurantOperationInfo;
import com.flab.woowahaneats.domain.restaurant.exception.RestaurantNotFoundException;
import com.flab.woowahaneats.domain.restaurant.repository.RestaurantOperationInfoRepository;
import com.flab.woowahaneats.domain.restaurant.repository.RestaurantRepository;
import com.flab.woowahaneats.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantOperationInfoRepository restaurantOperationInfoRepository;

    @Override
    @Transactional
    public void createCart(Long restaurantId, List<CartMenu> cartMenus) {
        User user = AuthContextHolder.getContext().getUser();
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);

        RestaurantOperationInfo operationInfo = restaurantOperationInfoRepository.findById(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);
        if (!operationInfo.isOpen()) {
            throw new RestaurantClosedException();
        }

        validateMenus(cartMenus, restaurantId);

        Cart cart = Cart.create(user, restaurant, cartMenus);
        cartRepository.save(cart);
    }

    @Override
    @Transactional(readOnly = true)
    public Cart getCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        validateCartOwnership(cart);
        return cart;
    }

    @Override
    @Transactional
    public void updateMenuQuantity(Long cartId, Long menuId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        validateCartOwnership(cart);
        cart.updateMenuQuantity(menuId, quantity);
    }

    @Override
    @Transactional
    public void deleteCartMenu(Long cartId, Long menuId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        validateCartOwnership(cart);
        cart.removeMenu(menuId);
    }

    @Override
    @Transactional
    public void deleteCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        validateCartOwnership(cart);
        cartRepository.delete(cart);
    }

    @Override
    @Transactional
    public void addCartMenu(Long cartId, CartMenu cartMenu) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        validateCartOwnership(cart);

        Menu menu = menuRepository.findById(cartMenu.menuId())
                .orElseThrow(MenuNotFoundException::new);
        validateMenuBelongsToRestaurant(menu, cart.getRestaurant().getId());
        menu.validateAvailable();

        cart.addMenu(cartMenu);
    }

    @Override
    @Transactional
    public void deleteCartBySystem(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(CartNotFoundException::new);
        cartRepository.delete(cart);
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

    private void validateMenus(List<CartMenu> cartMenus, Long restaurantId) {
        List<Long> requestedMenuIds = cartMenus.stream()
                .map(CartMenu::menuId)
                .distinct()
                .toList();

        List<Menu> foundMenus = menuRepository.findAllById(requestedMenuIds);

        if (foundMenus.size() != requestedMenuIds.size()) {
            throw new MenuNotFoundException();
        }

        foundMenus.forEach(menu -> {
            validateMenuBelongsToRestaurant(menu, restaurantId);
            menu.validateAvailable();
        });
    }
}
