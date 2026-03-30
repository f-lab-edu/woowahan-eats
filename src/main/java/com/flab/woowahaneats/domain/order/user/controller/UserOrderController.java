package com.flab.woowahaneats.domain.order.user.controller;

import com.flab.woowahaneats.domain.order.user.application.UserOrderService;
import com.flab.woowahaneats.domain.order.user.controller.dto.CreateOrderRequest;
import com.flab.woowahaneats.domain.order.user.controller.dto.CreateOrderResponse;
import com.flab.woowahaneats.domain.order.user.controller.dto.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/orders")
public class UserOrderController {
    private final UserOrderService orderService;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        CreateOrderResponse response = orderService.createOrder(createOrderRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable UUID orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrderList() {
        return ResponseEntity.ok(orderService.getOrderList());
    }
}
