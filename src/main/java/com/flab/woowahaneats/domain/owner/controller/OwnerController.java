package com.flab.woowahaneats.domain.owner.controller;

import com.flab.woowahaneats.domain.owner.application.OwnerService;
import com.flab.woowahaneats.domain.owner.controller.dto.OwnerSignUpRequest;
import com.flab.woowahaneats.domain.notification.application.NotificationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owner")
public class OwnerController {
    private final OwnerService ownerService;
    private final NotificationService notificationService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUpOwner(@Valid @RequestBody OwnerSignUpRequest ownerSignUpRequest) {
        ownerService.signUpOwner(ownerSignUpRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/notifications/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamNotifications() {
        return notificationService.subscribeOwner();
    }
}