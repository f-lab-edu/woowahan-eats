package com.flab.woowahaneats.domain.restaurant.owner.controller;

import com.flab.woowahaneats.domain.restaurant.controller.dto.RestaurantRequest;
import com.flab.woowahaneats.domain.restaurant.owner.service.OwnerRestaurantService;
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
@RequestMapping("/owner/restaurant")
public class OwnerRestaurantController {

    private final OwnerRestaurantService ownerRestaurantService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerRestaurant(@Valid @RequestBody RestaurantRequest restaurantRequest) {
        ownerRestaurantService.registerRestaurant(restaurantRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/open/{restaurantId}")
    public ResponseEntity<Void> openRestaurant(@PathVariable Long restaurantId) {
        ownerRestaurantService.openRestaurant(restaurantId);
        return ResponseEntity.ok().build();
    }
}