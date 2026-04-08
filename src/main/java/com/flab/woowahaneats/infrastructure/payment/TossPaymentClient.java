package com.flab.woowahaneats.infrastructure.payment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.woowahaneats.infrastructure.payment.dto.TossPaymentResponse;
import com.flab.woowahaneats.domain.payment.exception.PaymentException;
import com.flab.woowahaneats.global.config.TossPaymentsProperties;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.InputStream;
import java.util.Base64;
import java.util.Map;

@Component
@Slf4j
public class TossPaymentClient {

    private final TossPaymentsProperties tossPaymentsProperties;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public TossPaymentClient(TossPaymentsProperties tossPaymentsProperties, RestClient.Builder restClientBuilder, ObjectMapper objectMapper) {
        this.tossPaymentsProperties = tossPaymentsProperties;
        this.objectMapper = objectMapper;
        this.restClient = restClientBuilder
                .baseUrl(tossPaymentsProperties.baseUrl())
                .build();
    }

    @PostConstruct
    void logLoadedConfiguration() {
        log.info(
                "TossPayments config loaded. baseUrl={}, clientKeyPrefix={}, secretKeyPrefix={}",
                tossPaymentsProperties.baseUrl(),
                maskKey(tossPaymentsProperties.clientKey()),
                maskKey(tossPaymentsProperties.secretKey())
        );
    }

    public TossPaymentResponse confirmPayment(String paymentKey, String orderId, int amount) {
        Map<String, Object> params = Map.of(
                "paymentKey", paymentKey,
                "orderId", orderId,
                "amount", amount
        );

        return restClient.post()
                .uri("/v1/payments/confirm")
                .header("Authorization", "Basic " + getEncodedSecretKey())
                .contentType(MediaType.APPLICATION_JSON)
                .body(params)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        (request, response) -> {
                            String errorMessage = parseErrorMessage(response.getBody());
                            throw new PaymentException(errorMessage, "PAYMENT_APPROVAL_FAILED", HttpStatus.BAD_REQUEST);
                        }
                )
                .body(TossPaymentResponse.class);
    }

    public void cancelPayment(String paymentKey, String cancelReason) {
        Map<String, Object> params = Map.of(
                "cancelReason", cancelReason
        );

        restClient.post()
                .uri("/v1/payments/{paymentKey}/cancel", paymentKey)
                .header("Authorization", "Basic " + getEncodedSecretKey())
                .contentType(MediaType.APPLICATION_JSON)
                .body(params)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        (request, response) -> {
                            String errorMessage = parseErrorMessage(response.getBody());
                            throw new PaymentException(errorMessage, "PAYMENT_CANCEL_FAILED", HttpStatus.BAD_REQUEST);
                        }
                )
                .toBodilessEntity();
    }

    private String getEncodedSecretKey() {
        return Base64.getEncoder()
                .encodeToString((tossPaymentsProperties.secretKey() + ":").getBytes());
    }

    private String parseErrorMessage(InputStream body) {
        try {
            JsonNode root = objectMapper.readTree(body);
            String code = root.path("code").asText("UNKNOWN");
            String message = root.path("message").asText("결제 처리 중 오류가 발생했습니다.");
            return String.format("[%s] %s", code, message);
        } catch (Exception e) {
            return "결제 처리 중 오류가 발생했습니다.";
        }
    }

    private String maskKey(String key) {
        if (key == null || key.isBlank()) {
            return "empty";
        }

        int visibleLength = Math.min(12, key.length());
        return key.substring(0, visibleLength) + "...";
    }
}
