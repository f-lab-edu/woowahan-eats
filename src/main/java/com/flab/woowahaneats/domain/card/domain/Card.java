package com.flab.woowahaneats.domain.card.domain;

import com.flab.woowahaneats.domain.card.application.exception.InvalidCardException;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(toBuilder = true)
public class Card {
    private UUID id;
    private Long userId;
    private CardCompany cardCompany;
    private String cardNumber;
    private boolean isDefault;
    private String expiryDate;
    private CardType cardType;

    public static Card create(Long userId, CardCompany cardCompany, String cardNumber,
                             String expiryDate, CardType cardType) {
        validateExpiryDate(expiryDate);
        validateCardNumber(cardNumber);

        return Card.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .cardCompany(cardCompany)
                .cardNumber(cardNumber)
                .expiryDate(expiryDate)
                .cardType(cardType)
                .isDefault(false)
                .build();
    }

    private static void validateExpiryDate(String expiryDate) {
        if (!expiryDate.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            throw new InvalidCardException("유효기간 형식이 올바르지 않습니다. (MM/YY 형식)");
        }
    }

    private static void validateCardNumber(String cardNumber) {
        String cleanNumber = cardNumber.replaceAll("[^0-9]", "");
        if (cleanNumber.length() < 13 || cleanNumber.length() > 19) {
            throw new InvalidCardException("카드번호 길이가 올바르지 않습니다.");
        }
    }
}
