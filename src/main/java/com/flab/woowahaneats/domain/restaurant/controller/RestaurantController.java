package com.flab.woowahaneats.domain.restaurant.controller;

import com.flab.woowahaneats.domain.restaurant.application.RestaurantService;
import com.flab.woowahaneats.domain.restaurant.controller.dto.RestaurantRequest;
import com.flab.woowahaneats.domain.restaurant.controller.dto.RestaurantResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerRestaurant(@Valid @RequestBody RestaurantRequest restaurantRequest) {

        restaurantService.registerRestaurant(restaurantRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponse> getRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(restaurantService.getRestaurant(restaurantId));
    }

    @PostMapping("/open/{restaurantId}")
    public ResponseEntity<Void> openRestaurant(@PathVariable Long restaurantId) {

        restaurantService.openRestaurant(restaurantId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @GetMapping("/search")
    public ResponseEntity<RestaurantResponse> searchRestaurant(@RequestParam String name) {
        return ResponseEntity.ok(restaurantService.searchRestaurant(name));
    }
}
