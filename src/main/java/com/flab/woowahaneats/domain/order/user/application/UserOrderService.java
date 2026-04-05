package com.flab.woowahaneats.domain.order.user.application;

import com.flab.woowahaneats.domain.auth.AuthContextHolder;
import com.flab.woowahaneats.domain.cart.exception.CartNotBelongToUserException;
import com.flab.woowahaneats.domain.cart.exception.CartNotFoundException;
import com.flab.woowahaneats.domain.cart.domain.Cart;
import com.flab.woowahaneats.domain.cart.domain.CartMenu;
import com.flab.woowahaneats.domain.cart.repository.CartRepository;
import com.flab.woowahaneats.domain.user.domain.User;
import com.flab.woowahaneats.domain.menu.exception.MenuNotFoundException;
import com.flab.woowahaneats.domain.menu.domain.Menu;
import com.flab.woowahaneats.domain.menu.repository.MenuRepository;
import com.flab.woowahaneats.domain.order.user.controller.dto.CreateOrderResponse;
import com.flab.woowahaneats.domain.payment.application.PaymentService;
import com.flab.woowahaneats.domain.payment.domain.Payment;
import com.flab.woowahaneats.domain.order.exception.MenuNotAvailableException;
import com.flab.woowahaneats.domain.order.exception.OrderNotFoundException;
import com.flab.woowahaneats.domain.order.exception.OrderNotBelongToUserException;
import com.flab.woowahaneats.domain.order.exception.RestaurantClosedException;
import com.flab.woowahaneats.domain.order.exception.RestaurantOperationInfoNotFoundException;
import com.flab.woowahaneats.domain.order.user.controller.dto.CreateOrderRequest;
import com.flab.woowahaneats.domain.order.user.controller.dto.OrderResponse;
import com.flab.woowahaneats.domain.order.user.domain.UserOrder;
import com.flab.woowahaneats.domain.order.common.OrderMenu;
import com.flab.woowahaneats.domain.order.user.event.UserOrderCancelledEvent;
import com.flab.woowahaneats.domain.order.user.repository.UserOrderRepository;
import com.flab.woowahaneats.domain.restaurant.domain.RestaurantOperationInfo;
import com.flab.woowahaneats.domain.restaurant.repository.RestaurantOperationInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserOrderService {
    private final UserOrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final MenuRepository menuRepository;
    private final RestaurantOperationInfoRepository restaurantOperationInfoRepository;
    private final PaymentService paymentService;
    private final ApplicationEventPublisher eventPublisher;

    public CreateOrderResponse createOrder(CreateOrderRequest request) {
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

        UserOrder order = UserOrder.create(
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

        Payment payment = paymentService.preparePayment(
                order.getId(),
                order.getOrderPrice().totalPrice(),
                request.paymentProvider()
        );

        return new CreateOrderResponse(
                payment.getGatewayOrderId(),
                payment.getAmount(),
                createOrderName(orderMenus),
                payment.getProvider(),
                payment.getStatus()
        );
    }

    public void cancelOrder(UUID orderId){
        User user = AuthContextHolder.getContext().getUser();
        UserOrder order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        if (!order.getUserId().equals(user.getId())) {
            throw new OrderNotBelongToUserException();
        }

        order.cancel();
        orderRepository.save(order);

        paymentService.refundPayment(orderId, "사용자 주문 취소");

        eventPublisher.publishEvent(new UserOrderCancelledEvent(orderId));
    }

    public void approveOrder(UUID orderId) {
        UserOrder order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        order.approve();
        orderRepository.save(order);
    }

    public void startCooking(UUID orderId) {
        UserOrder order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        order.startCooking();
        orderRepository.save(order);
    }

    public void completeCooking(UUID orderId) {
        UserOrder order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        order.completeCooking();
        orderRepository.save(order);
    }

    public void startDelivering(UUID orderId) {
        UserOrder order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        order.startDelivering();
        orderRepository.save(order);
    }

    public void completeOrder(UUID orderId) {
        UserOrder order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        order.complete();
        orderRepository.save(order);
    }

    public void resetOrderToReady(UUID orderId) {
        UserOrder order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        order.resetToReady();
        orderRepository.save(order);
    }

    public List<OrderResponse> getOrderList(){
        User user = AuthContextHolder.getContext().getUser();
        List<UserOrder> orders = orderRepository.findActiveOrdersByUserId(user.getId());

        return orders.stream()
                .map(OrderResponse::from)
                .toList();
    }

    private List<OrderMenu> convertToOrderMenus(Cart cart) {
        return cart.getMenus().stream()
                .map(this::convertToOrderMenu)
                .toList();
    }

    private String createOrderName(List<OrderMenu> orderMenus) {
        OrderMenu firstMenu = orderMenus.getFirst();
        if (orderMenus.size() == 1) {
            return firstMenu.menuName();
        }
        return firstMenu.menuName() + " 외 " + (orderMenus.size() - 1) + "건";
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
