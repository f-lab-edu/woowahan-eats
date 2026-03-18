package com.flab.woowahaneats.domain.card.controller;

import com.flab.woowahaneats.domain.card.application.CardService;
import com.flab.woowahaneats.domain.card.controller.dto.CardRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/card")
public class CardController {

    private final CardService cardService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerCard(@Valid @RequestBody CardRequest cardRequest) {
        cardService.registerCard(cardRequest);
        return ResponseEntity.ok().build();
    }
}
