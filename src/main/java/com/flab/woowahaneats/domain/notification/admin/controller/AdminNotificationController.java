package com.flab.woowahaneats.domain.notification.admin.controller;

import com.flab.woowahaneats.domain.notification.admin.controller.dto.AdminNotificationResponse;
import com.flab.woowahaneats.domain.notification.admin.service.AdminNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/admin/notifications")
@RequiredArgsConstructor
public class AdminNotificationController {

    private final AdminNotificationService adminNotificationService;

    @GetMapping
    public ResponseEntity<List<AdminNotificationResponse>> getUnreadNotifications() {
        return ResponseEntity.ok(adminNotificationService.getUnreadNotifications());
    }

    @PatchMapping("/read")
    public ResponseEntity<Void> markAllAsRead() {
        adminNotificationService.markAllAsRead();
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamNotifications() {
        return adminNotificationService.subscribe();
    }
}
