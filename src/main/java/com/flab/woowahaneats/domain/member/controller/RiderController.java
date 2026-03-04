package com.flab.woowahaneats.domain.member.controller;

import com.flab.woowahaneats.domain.member.application.RiderService;
import com.flab.woowahaneats.domain.member.controller.dto.OwnerSignUpRequest;
import com.flab.woowahaneats.domain.member.controller.dto.RiderSignUpRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
