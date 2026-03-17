package com.flab.woowahaneats.domain.card.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class Card {
    private UUID id;
    private Long userId;
    private String cardCompany;
    private String cardNumber;
    private boolean isDefault;
    private String expiryDate;
    private CardType cardType;
}
