package com.flab.woowahaneats.domain.restaurant.controller;

import com.flab.woowahaneats.domain.restaurant.application.RestaurantService;
import com.flab.woowahaneats.domain.restaurant.controller.dto.RestaurantRequest;
import com.flab.woowahaneats.domain.restaurant.domain.Restaurant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{restaurantId}")
    public ResponseEntity<Restaurant> getRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(restaurantService.getRestaurant(restaurantId));
    }

}
