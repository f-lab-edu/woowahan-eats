package com.flab.woowahaneats.domain.auth.controller;

import com.flab.woowahaneats.domain.auth.application.AuthService;
import com.flab.woowahaneats.domain.auth.controller.dto.AuthLoginRequest;
import com.flab.woowahaneats.domain.member.domain.Account;
import com.flab.woowahaneats.domain.member.domain.Owner;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody AuthLoginRequest authLoginRequest, HttpServletRequest request) {
        Account account = authService.login(authLoginRequest);

        HttpSession oldSession = request.getSession(false);
        if(oldSession != null) {
            oldSession.invalidate();
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("accountId", account.getId());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if(session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok().build();
    }
}

