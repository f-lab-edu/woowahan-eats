package com.flab.woowahaneats.domain.payment.controller;

import com.flab.woowahaneats.domain.payment.application.PaymentService;
import com.flab.woowahaneats.domain.payment.controller.dto.PaymentConfirmRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/confirm")
    public ResponseEntity<Void> confirmPayment(@Valid @RequestBody PaymentConfirmRequest request) {
        paymentService.confirmPayment(
                request.paymentKey(),
                request.orderId(),
                request.amount()
        );
        return ResponseEntity.ok().build();
    }
}
