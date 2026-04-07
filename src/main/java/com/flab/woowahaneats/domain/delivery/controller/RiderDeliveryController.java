package com.flab.woowahaneats.domain.delivery.controller;

import com.flab.woowahaneats.domain.delivery.application.DeliveryService;
import com.flab.woowahaneats.domain.delivery.controller.dto.AcceptDeliveryRequest;
import com.flab.woowahaneats.domain.delivery.controller.dto.DeliveryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rider/deliveries")
public class RiderDeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping("/{userOrderId}")
    public ResponseEntity<Void> createDelivery(@PathVariable Long userOrderId) {
        deliveryService.createDelivery(userOrderId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{deliveryId}")
    public ResponseEntity<Void> cancelDelivery(@PathVariable Long deliveryId) {
        deliveryService.cancelDelivery(deliveryId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pending")
    public ResponseEntity<List<DeliveryResponse>> getPendingDeliveries() {
        List<DeliveryResponse> deliveries = deliveryService.getPendingDeliveries();
        return ResponseEntity.ok(deliveries);
    }

    @PostMapping("/{deliveryId}/accept")
    public ResponseEntity<Void> acceptDelivery(
            @PathVariable Long deliveryId,
            @RequestBody AcceptDeliveryRequest request
    ) {
        deliveryService.acceptDelivery(deliveryId, request.riderId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{deliveryId}/start-pickup")
    public ResponseEntity<Void> startPickup(@PathVariable Long deliveryId) {
        deliveryService.startPickup(deliveryId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{deliveryId}/complete-pickup")
    public ResponseEntity<Void> completePickup(@PathVariable Long deliveryId) {
        deliveryService.completePickup(deliveryId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{deliveryId}/start-delivery")
    public ResponseEntity<Void> startDelivery(@PathVariable Long deliveryId) {
        deliveryService.startDelivery(deliveryId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{deliveryId}/complete")
    public ResponseEntity<Void> completeDelivery(@PathVariable Long deliveryId) {
        deliveryService.completeDelivery(deliveryId);
        return ResponseEntity.ok().build();
    }
}