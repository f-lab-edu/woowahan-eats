package com.flab.woowahaneats.domain.cart.controller;

import com.flab.woowahaneats.domain.cart.application.CartService;
import com.flab.woowahaneats.domain.cart.domain.Cart;
import com.flab.woowahaneats.domain.cart.domain.CartMenu;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/create/{userId}/{restaurantId}")
    public ResponseEntity<Void> createCart(@PathVariable Long userId, @PathVariable Long restaurantId,
                                           @RequestBody List<CartMenu> menus) {
        cartService.createCart(userId, restaurantId, menus);
        return ResponseEntity.ok().build();
    }
}
