package com.flab.woowahaneats.domain.admin.controller;

import com.flab.woowahaneats.domain.auth.AuthContextHolder;
import com.flab.woowahaneats.domain.admin.application.AdminService;
import com.flab.woowahaneats.domain.admin.controller.dto.AdminSignUpRequest;
import com.flab.woowahaneats.domain.admin.domain.Admin;
import com.flab.woowahaneats.domain.notification.application.NotificationService;
import com.flab.woowahaneats.domain.restaurant.application.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final NotificationService notificationService;
    private final RestaurantService restaurantService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUpAdmin(@Valid @RequestBody AdminSignUpRequest adminSignUpRequest) {
        adminService.signUpAdmin(adminSignUpRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/notifications/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamNotifications() {
        return notificationService.subscribe();
    }

    @PostMapping("/restaurant/{restaurantId}/approve")
    public ResponseEntity<Void> approveRestaurant(@PathVariable Long restaurantId) {
        restaurantService.approveRestaurant(restaurantId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/restaurant/{restaurantId}/reject")
    public ResponseEntity<Void> rejectRestaurant(@PathVariable Long restaurantId) {
        restaurantService.rejectRestaurant(restaurantId);
        return ResponseEntity.ok().build();
    }
}