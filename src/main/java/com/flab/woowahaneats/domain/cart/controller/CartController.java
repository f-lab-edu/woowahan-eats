package com.flab.woowahaneats.domain.cart.controller;

import com.flab.woowahaneats.domain.cart.application.CartService;
import com.flab.woowahaneats.domain.cart.domain.Cart;
import com.flab.woowahaneats.domain.cart.domain.CartMenu;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCart(@PathVariable UUID cartId) {
        return ResponseEntity.ok().body(cartService.getCart(cartId));
    }

    @PatchMapping("/{cartId}/menu/{menuId}")
    public ResponseEntity<Void> updateMenuQuantity(@PathVariable UUID cartId,
                                                   @PathVariable Long menuId,
                                                   @RequestBody int quantity) {
        cartService.updateMenuQuantity(cartId, menuId, quantity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{cartId}/{menuId}")
    public ResponseEntity<Void> deleteCartMenu(@PathVariable UUID cartId,
                                               @PathVariable Long menuId) {
        cartService.deleteCartMenu(cartId, menuId);
        return ResponseEntity.ok().build();
    }
}
