package com.flab.woowahaneats.domain.order.controller;

import com.flab.woowahaneats.domain.order.application.OrderService;
import com.flab.woowahaneats.domain.order.controller.dto.CreateOrderRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<Void> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        orderService.createOrder(createOrderRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable UUID orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
