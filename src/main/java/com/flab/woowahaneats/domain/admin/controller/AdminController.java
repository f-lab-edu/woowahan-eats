package com.flab.woowahaneats.domain.admin.controller;

import com.flab.woowahaneats.domain.admin.application.AdminService;
import com.flab.woowahaneats.domain.admin.controller.dto.AdminSignUpRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUpAdmin(@Valid @RequestBody AdminSignUpRequest adminSignUpRequest) {
        adminService.signUpAdmin(adminSignUpRequest);
        return ResponseEntity.ok().build();
    }
}
