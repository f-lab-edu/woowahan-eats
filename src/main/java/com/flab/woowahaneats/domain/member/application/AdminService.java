package com.flab.woowahaneats.domain.member.application;

import com.flab.woowahaneats.domain.auth.domain.Account;
import com.flab.woowahaneats.domain.member.application.exception.DuplicateEmailException;
import com.flab.woowahaneats.domain.member.controller.dto.AdminSignUpRequest;
import com.flab.woowahaneats.domain.member.domain.Admin;
import com.flab.woowahaneats.domain.member.repository.AccountRepository;
import com.flab.woowahaneats.domain.member.repository.AdminRepository;
import com.flab.woowahaneats.global.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUpAdmin(AdminSignUpRequest adminSignUpRequest) {
        Account existingAccount = accountRepository.findByEmail(adminSignUpRequest.email());

        if (existingAccount != null) {
            throw new DuplicateEmailException();
        }

        String encodedPassword = passwordEncoder.encode(adminSignUpRequest.password());

        Account account = Account.builder()
                .id(adminSignUpRequest.id())
                .password(encodedPassword)
                .email(adminSignUpRequest.email())
                .build();

        accountRepository.save(account);

        Admin admin = Admin.builder()
                .id(adminSignUpRequest.id())
                .accountId(account.getId())
                .name(adminSignUpRequest.name())
                .phoneNumber(adminSignUpRequest.phoneNumber())
                .build();

        adminRepository.save(admin);
    }
}