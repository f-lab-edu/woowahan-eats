package com.flab.woowahaneats.domain.restaurant.user.controller;

import ch.hsr.geohash.GeoHash;
import com.flab.woowahaneats.domain.auth.AuthContextHolder;
import com.flab.woowahaneats.domain.common.vo.Location;
import com.flab.woowahaneats.domain.restaurant.domain.RestaurantCategory;
import com.flab.woowahaneats.domain.restaurant.user.controller.dto.RestaurantResponse;
import com.flab.woowahaneats.domain.restaurant.user.service.UserRestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/restaurant")
public class UserRestaurantController {

    private final UserRestaurantService userRestaurantService;

    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantResponse> getRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(userRestaurantService.getRestaurant(restaurantId));
    }

@GetMapping("/category/{category}")
    public ResponseEntity<List<RestaurantResponse>> getRestaurantsByCategory(@PathVariable RestaurantCategory category) {
        return ResponseEntity.ok(userRestaurantService.getRestaurantsByCategory(category));
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<RestaurantResponse>> getNearbyRestaurants() {
        return ResponseEntity.ok(userRestaurantService.getNearbyRestaurants());
    }

    private static final int GEOHASH_PRECISION = 5;

    @GetMapping("/nearby/category/{category}")
    public ResponseEntity<List<RestaurantResponse>> getNearbyRestaurantsByCategory(@PathVariable RestaurantCategory category) {
        Location userLocation = AuthContextHolder.getContext().getUserLocation();
        String geoHash = GeoHash.withCharacterPrecision(userLocation.latitude(), userLocation.longitude(), GEOHASH_PRECISION).toBase32();
        return ResponseEntity.ok(userRestaurantService.getNearbyRestaurantsByCategory(category, geoHash));
    }

    @GetMapping("/search")
    public ResponseEntity<RestaurantResponse> searchRestaurant(@RequestParam String name) {
        return ResponseEntity.ok(userRestaurantService.searchRestaurant(name));
    }
}