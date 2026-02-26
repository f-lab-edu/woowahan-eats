package com.flab.woowahaneats.domain.member.controller;

import com.flab.woowahaneats.domain.member.application.UserService;
import com.flab.woowahaneats.domain.member.controller.dto.UserResponse;
import com.flab.woowahaneats.domain.member.controller.dto.UserSignUpRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUpUser(@Valid @RequestBody UserSignUpRequest userSignUpRequest) {
        userService.signUpUser(userSignUpRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getUserProfile() {
        return ResponseEntity.ok(userService.getUserProfile());
    }
}
