package com.flab.woowahaneats.domain.notification.owner.controller;

import com.flab.woowahaneats.domain.notification.owner.controller.dto.OwnerNotificationResponse;
import com.flab.woowahaneats.domain.notification.owner.service.OwnerNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/owner/notifications")
@RequiredArgsConstructor
public class OwnerNotificationController {

    private final OwnerNotificationService ownerNotificationService;

    @GetMapping
    public ResponseEntity<List<OwnerNotificationResponse>> getUnreadNotifications() {
        return ResponseEntity.ok(ownerNotificationService.getUnreadNotifications());
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamNotifications() {
        return ownerNotificationService.subscribe();
    }
}
