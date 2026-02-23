package com.flab.woowahaneats.domain.auth.application;

import com.flab.woowahaneats.domain.auth.controller.dto.AuthLoginRequest;
import com.flab.woowahaneats.domain.member.application.exception.AccountNotFoundException;
import com.flab.woowahaneats.domain.member.application.exception.InvalidPasswordException;
import com.flab.woowahaneats.domain.member.domain.Account;
import com.flab.woowahaneats.domain.member.domain.Owner;
import com.flab.woowahaneats.domain.member.repository.AccountRepository;
import com.flab.woowahaneats.domain.member.repository.OwnerRepository;
import com.flab.woowahaneats.global.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public Account login(AuthLoginRequest authLoginRequest) {

        Account account = accountRepository.findByEmail(authLoginRequest.email());

        if (account == null) {
            throw new AccountNotFoundException();
        }

        if (!passwordEncoder.matches(authLoginRequest.password(), account.getPassword())) {
            throw new InvalidPasswordException();
        }

        return account;
    }
}
