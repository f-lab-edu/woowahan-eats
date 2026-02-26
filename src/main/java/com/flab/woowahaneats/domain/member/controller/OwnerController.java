package com.flab.woowahaneats.domain.member.controller;

import com.flab.woowahaneats.domain.member.application.OwnerService;
import com.flab.woowahaneats.domain.member.controller.dto.OwnerSignUpRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
