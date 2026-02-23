package com.flab.woowahaneats.domain.auth.application;

import com.flab.woowahaneats.domain.auth.controller.dto.AuthLoginRequest;
import com.flab.woowahaneats.domain.member.domain.Account;
import com.flab.woowahaneats.domain.member.domain.Owner;
import com.flab.woowahaneats.domain.member.repository.AccountRepository;
import com.flab.woowahaneats.domain.member.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository accountRepository;

    public Account login(AuthLoginRequest authLoginRequest) {

        Account account = accountRepository.findByEmail(authLoginRequest.email());

        if (account == null) {
            throw new IllegalArgumentException("해당 email의 계정이 없습니다.");

        }

        if (!account.getPassword().equals(authLoginRequest.password())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

      return account;
    }
}
