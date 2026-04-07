package com.flab.woowahaneats.domain.auth.application;

import com.flab.woowahaneats.domain.auth.controller.dto.AuthLoginRequest;
import com.flab.woowahaneats.domain.auth.domain.Account;
import com.flab.woowahaneats.domain.auth.exception.AccountNotFoundException;
import com.flab.woowahaneats.domain.auth.exception.InvalidPasswordException;
import com.flab.woowahaneats.domain.auth.repository.AccountRepository;
import com.flab.woowahaneats.global.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
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
