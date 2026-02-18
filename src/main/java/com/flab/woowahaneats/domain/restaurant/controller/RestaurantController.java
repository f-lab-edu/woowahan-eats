package com.flab.woowahaneats.domain.restaurant.controller;

import com.flab.woowahaneats.domain.restaurant.application.RestaurantService;
import com.flab.woowahaneats.domain.restaurant.controller.dto.RestaurantRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping("/register")
    public ResponseEntity<Void> restaurantRegister(@Valid @RequestBody RestaurantRequest restaurantRequest) {

        restaurantService.restaurantRegister(restaurantRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
