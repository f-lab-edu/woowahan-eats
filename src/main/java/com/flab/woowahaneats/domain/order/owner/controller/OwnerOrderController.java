package com.flab.woowahaneats.domain.order.owner.controller;

import com.flab.woowahaneats.domain.order.owner.application.OwnerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("owner/orders")
public class OwnerOrderController {

    private final OwnerOrderService ownerOrderService;

    @PostMapping("/approve/{userOrderId}")
    public ResponseEntity<Void> approveOrder(@PathVariable UUID userOrderId) {
        ownerOrderService.approveOrder(userOrderId);
        return ResponseEntity.ok().build();
    }
}
