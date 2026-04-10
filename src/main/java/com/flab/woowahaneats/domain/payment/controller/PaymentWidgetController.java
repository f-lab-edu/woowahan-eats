package com.flab.woowahaneats.domain.payment.controller;

import com.flab.woowahaneats.domain.payment.controller.dto.PaymentWidgetConfigResponse;
import com.flab.woowahaneats.global.config.TossPaymentsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment-widget")
public class PaymentWidgetController {

    private final TossPaymentsProperties tossPaymentsProperties;

    @GetMapping("/config")
    public ResponseEntity<PaymentWidgetConfigResponse> getConfig() {
        return ResponseEntity.ok(new PaymentWidgetConfigResponse(tossPaymentsProperties.clientKey()));
    }
}
