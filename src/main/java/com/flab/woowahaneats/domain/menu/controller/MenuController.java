package com.flab.woowahaneats.domain.menu.controller;

import com.flab.woowahaneats.domain.menu.application.MenuService;
import com.flab.woowahaneats.domain.menu.controller.dto.MenuRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
