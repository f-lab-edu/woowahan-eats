package com.flab.woowahaneats.domain.order.application;

import com.flab.woowahaneats.domain.auth.AuthContextHolder;
import com.flab.woowahaneats.domain.cart.application.exception.CartNotBelongToUserException;
import com.flab.woowahaneats.domain.cart.application.exception.CartNotFoundException;
import com.flab.woowahaneats.domain.cart.domain.Cart;
import com.flab.woowahaneats.domain.cart.domain.CartMenu;
import com.flab.woowahaneats.domain.cart.repository.CartRepository;
import com.flab.woowahaneats.domain.member.domain.User;
import com.flab.woowahaneats.domain.menu.application.exception.MenuNotFoundException;
import com.flab.woowahaneats.domain.menu.domain.Menu;
import com.flab.woowahaneats.domain.menu.repository.MenuRepository;
import com.flab.woowahaneats.domain.order.application.exception.MenuNotAvailableException;
import com.flab.woowahaneats.domain.order.application.exception.RestaurantClosedException;
import com.flab.woowahaneats.domain.order.application.exception.RestaurantOperationInfoNotFoundException;
import com.flab.woowahaneats.domain.order.controller.dto.CreateOrderRequest;
import com.flab.woowahaneats.domain.order.domain.Order;
import com.flab.woowahaneats.domain.order.domain.OrderMenu;
import com.flab.woowahaneats.domain.order.repository.OrderRepository;
import com.flab.woowahaneats.domain.restaurant.domain.RestaurantOperationInfo;
import com.flab.woowahaneats.domain.restaurant.repository.RestaurantOperationInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final MenuRepository menuRepository;
    private final RestaurantOperationInfoRepository restaurantOperationInfoRepository;

    public void createOrder(CreateOrderRequest request) {
        User user = AuthContextHolder.getContext().getUser();
        Cart cart = cartRepository.findById(request.cartId())
                .orElseThrow(CartNotFoundException::new);

        if (!cart.getUserId().equals(user.getId())) {
            throw new CartNotBelongToUserException();
        }

        RestaurantOperationInfo operationInfo = restaurantOperationInfoRepository
                .findById(cart.getRestaurantId())
                .orElseThrow(RestaurantOperationInfoNotFoundException::new);

        if (!operationInfo.isOpen()) {
            throw new RestaurantClosedException();
        }

        List<OrderMenu> orderMenus = convertToOrderMenus(cart);

        Order order = Order.create(
                user.getId(),
                cart.getRestaurantId(),
                orderMenus,
                request.deliveryAddress(),
                request.requestToStore(),
                request.requestToRider(),
                operationInfo.getDeliveryFee(),
                operationInfo.getMinOrderAmt()
        );

        orderRepository.save(order);
    }

    private List<OrderMenu> convertToOrderMenus(Cart cart) {
        return cart.getMenus().stream()
                .map(this::convertToOrderMenu)
                .toList();
    }

    private OrderMenu convertToOrderMenu(CartMenu cartMenu) {
        Menu menu = menuRepository.findById(cartMenu.menuId())
                .orElseThrow(MenuNotFoundException::new);

        if (!menu.isAvailable()) {
            throw new MenuNotAvailableException(menu.getDisplayName());
        }

        return new OrderMenu(
                menu.getId(),
                menu.getDisplayName(),
                menu.getPrice(),
                cartMenu.quantity()
        );
    }
}
