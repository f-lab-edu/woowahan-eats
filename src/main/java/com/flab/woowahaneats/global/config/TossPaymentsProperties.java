package com.flab.woowahaneats.global.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "toss.payments")
public record TossPaymentsProperties (
     @NotBlank String clientKey,
     @NotBlank String secretKey,
     @NotBlank String baseUrl
){}
