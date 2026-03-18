package com.flab.woowahaneats.domain.card.repository;

import com.flab.woowahaneats.domain.card.domain.Card;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.UUID;

@Repository
public class HashMapCardRepository implements CardRepository {
    HashMap<UUID, Card> cardMap = new HashMap<>();
    @Override
    public void save(Card card) {
        cardMap.put(card.getId(), card);
    }
}
