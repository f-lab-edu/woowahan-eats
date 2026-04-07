package com.flab.woowahaneats.domain.cart.controller;

import com.flab.woowahaneats.domain.cart.application.CartService;
import com.flab.woowahaneats.domain.cart.domain.CartMenu;
import com.flab.woowahaneats.domain.cart.controller.dto.CartMenuRequest;
import com.flab.woowahaneats.domain.cart.controller.dto.CartResponse;
import com.flab.woowahaneats.domain.cart.controller.dto.CreateCartRequest;
import com.flab.woowahaneats.domain.cart.controller.dto.UpdateCartMenuQuantityRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/create/{restaurantId}")
    public ResponseEntity<Void> createCart(@PathVariable Long restaurantId,
                                           @Valid @RequestBody CreateCartRequest request) {
        cartService.createCart(
                restaurantId,
                request.menus().stream()
                        .map(menu -> new CartMenu(menu.menuId(), menu.quantity()))
                        .toList()
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long cartId) {
        return ResponseEntity.ok().body(CartResponse.from(cartService.getCart(cartId)));
    }

    @PatchMapping("/{cartId}/menu/{menuId}")
    public ResponseEntity<Void> updateMenuQuantity(@PathVariable Long cartId,
                                                   @PathVariable Long menuId,
                                                   @Valid @RequestBody UpdateCartMenuQuantityRequest request) {
        cartService.updateMenuQuantity(cartId, menuId, request.quantity());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{cartId}/{menuId}")
    public ResponseEntity<Void> deleteCartMenu(@PathVariable Long cartId,
                                               @PathVariable Long menuId) {
        cartService.deleteCartMenu(cartId, menuId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{cartId}")
    public ResponseEntity<Void> addCartMenu(@PathVariable Long cartId,
                                            @Valid @RequestBody CartMenuRequest request) {
        cartService.addCartMenu(cartId, new CartMenu(request.menuId(), request.quantity()));
        return ResponseEntity.ok().build();
    }
}
