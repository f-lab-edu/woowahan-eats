package com.flab.woowahaneats.domain.delivery.controller;

import com.flab.woowahaneats.domain.delivery.application.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rider/deliveries")
public class RiderDeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping("/{userOrderId}")
    public ResponseEntity<Void> createDelivery(@PathVariable UUID userOrderId) {
        deliveryService.createDelivery(userOrderId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{deliveryId}")
    public ResponseEntity<Void> cancelDelivery(@PathVariable UUID deliveryId) {
        deliveryService.cancelDelivery(deliveryId);
        return ResponseEntity.ok().build();
    }
}