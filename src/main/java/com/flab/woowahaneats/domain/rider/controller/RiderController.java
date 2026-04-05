package com.flab.woowahaneats.domain.rider.controller;

import com.flab.woowahaneats.domain.rider.application.RiderService;
import com.flab.woowahaneats.domain.rider.controller.dto.RiderSignUpRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rider")
@RequiredArgsConstructor
public class RiderController {

    private final RiderService riderService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUpOwner(@Valid @RequestBody RiderSignUpRequest riderSignUpRequest) {
        riderService.signUpRider(riderSignUpRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{riderId}/start-work")
    public ResponseEntity<Void> startWork(@PathVariable Long riderId) {
        riderService.startWork(riderId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{riderId}/end-work")
    public ResponseEntity<Void> endWork(@PathVariable Long riderId) {
        riderService.endWork(riderId);
        return ResponseEntity.ok().build();
    }
}