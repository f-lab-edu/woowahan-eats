package com.flab.woowahaneats.domain.card.application;

import com.flab.woowahaneats.domain.auth.AuthContextHolder;
import com.flab.woowahaneats.domain.card.controller.dto.CardRequest;
import com.flab.woowahaneats.domain.card.domain.Card;
import com.flab.woowahaneats.domain.card.repository.CardRepository;
import com.flab.woowahaneats.domain.member.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;

    public void registerCard(CardRequest cardRequest) {
        User user = AuthContextHolder.getContext().getUser();

        Card card = Card.create(
                user.getId(),
                cardRequest.cardCompany(),
                cardRequest.cardNumber(),
                cardRequest.expiryDate(),
                cardRequest.cardType()
        );

        cardRepository.save(card);
    }
}
