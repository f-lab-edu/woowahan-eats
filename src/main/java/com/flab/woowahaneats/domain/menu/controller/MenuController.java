package com.flab.woowahaneats.domain.menu.controller;

import com.flab.woowahaneats.domain.menu.application.MenuService;
import com.flab.woowahaneats.domain.menu.controller.dto.MenuRequest;
import com.flab.woowahaneats.domain.menu.controller.dto.MenuUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurant/{restaurantId}/menu")
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerMenu (
            @PathVariable Long restaurantId,
            @Valid @RequestBody MenuRequest menuRequest) {
        menuService.registerMenu(restaurantId, menuRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PatchMapping("/{menuId}/update")
    public ResponseEntity<Void> updateMenu (
            @PathVariable Long restaurantId,
            @PathVariable Long menuId,
            @Valid @RequestBody MenuUpdateRequest menuUpdateRequest){

        menuService.updateMenu(restaurantId, menuId, menuUpdateRequest);
        return ResponseEntity.ok().build();
    }

}
