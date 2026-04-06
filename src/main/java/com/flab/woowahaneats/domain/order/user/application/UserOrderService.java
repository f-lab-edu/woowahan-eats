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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserOrderService {
    private final UserOrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final MenuRepository menuRepository;
    private final RestaurantOperationInfoRepository restaurantOperationInfoRepository;
    private final PaymentService paymentService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        User user = AuthContextHolder.getContext().getUser();
        Cart cart = cartRepository.findById(request.cartId())
                .orElseThrow(CartNotFoundException::new);

        if (!cart.getUser().getId().equals(user.getId())) {
            throw new CartNotBelongToUserException();
        }

        RestaurantOperationInfo operationInfo = restaurantOperationInfoRepository
                .findById(cart.getRestaurant().getId())
                .orElseThrow(RestaurantOperationInfoNotFoundException::new);

        if (!operationInfo.isOpen()) {
            throw new RestaurantClosedException();
        }

        List<OrderMenu> orderMenus = convertToOrderMenus(cart);

        UserOrder order = UserOrder.create(
                user,
                cart.getRestaurant(),
                orderMenus,
                request.deliveryAddress(),
                request.requestToStore(),
                request.requestToRider(),
                operationInfo.getDeliveryFee(),
                operationInfo.getMinOrderAmt()
        );

        orderRepository.save(order);

        Payment payment = paymentService.preparePayment(
                order,
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

    @Transactional
    public void cancelOrder(Long orderId){
        User user = AuthContextHolder.getContext().getUser();
        UserOrder order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        if (!order.getUser().getId().equals(user.getId())) {
            throw new OrderNotBelongToUserException();
        }

        order.cancel();
        orderRepository.save(order);

        paymentService.refundPayment(orderId, "사용자 주문 취소");

        eventPublisher.publishEvent(new UserOrderCancelledEvent(orderId));
    }

    @Transactional
    public void approveOrder(Long orderId) {
        UserOrder order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        order.approve();
        orderRepository.save(order);
    }

    @Transactional
    public void startCooking(Long orderId) {
        UserOrder order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        order.startCooking();
        orderRepository.save(order);
    }

    @Transactional
    public void completeCooking(Long orderId) {
        UserOrder order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        order.completeCooking();
        orderRepository.save(order);
    }

    @Transactional
    public void startDelivering(Long orderId) {
        UserOrder order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        order.startDelivering();
        orderRepository.save(order);
    }

    @Transactional
    public void completeOrder(Long orderId) {
        UserOrder order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        order.complete();
        orderRepository.save(order);
    }

    @Transactional
    public void resetOrderToReady(Long orderId) {
        UserOrder order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        order.resetToReady();
        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
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
