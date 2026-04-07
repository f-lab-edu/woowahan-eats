package com.flab.woowahaneats.domain.restaurant.admin.controller;

import com.flab.woowahaneats.domain.restaurant.admin.service.AdminRestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/restaurant")
public class AdminRestaurantController {

    private final AdminRestaurantService adminRestaurantService;

    @PostMapping("/{restaurantId}/approve")
    public ResponseEntity<Void> approveRestaurant(@PathVariable Long restaurantId) {
        adminRestaurantService.approveRestaurant(restaurantId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{restaurantId}/reject")
    public ResponseEntity<Void> rejectRestaurant(@PathVariable Long restaurantId) {
        adminRestaurantService.rejectRestaurant(restaurantId);
        return ResponseEntity.ok().build();
    }
}
