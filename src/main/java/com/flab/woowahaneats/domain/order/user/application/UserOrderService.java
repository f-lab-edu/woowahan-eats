package com.flab.woowahaneats.domain.order.user.application;

import com.flab.woowahaneats.domain.order.user.controller.dto.CreateOrderRequest;
import com.flab.woowahaneats.domain.order.user.controller.dto.CreateOrderResponse;
import com.flab.woowahaneats.domain.order.user.controller.dto.OrderResponse;

import java.util.List;

public interface UserOrderService {

    CreateOrderResponse createOrder(CreateOrderRequest request);

    void cancelOrder(Long orderId);

    void approveOrder(Long orderId);

    void startCooking(Long orderId);

    void completeCooking(Long orderId);

    void startDelivering(Long orderId);

    void completeOrder(Long orderId);

    void resetOrderToReady(Long orderId);

    List<OrderResponse> getOrderList();
}
