package com.flab.woowahaneats.domain.owner.controller;

import com.flab.woowahaneats.domain.owner.application.OwnerService;
import com.flab.woowahaneats.domain.owner.controller.dto.OwnerSignUpRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owner")
public class OwnerController {
    private final OwnerService ownerService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUpOwner(@Valid @RequestBody OwnerSignUpRequest ownerSignUpRequest) {
        ownerService.signUpOwner(ownerSignUpRequest);
        return ResponseEntity.ok().build();
    }
}
