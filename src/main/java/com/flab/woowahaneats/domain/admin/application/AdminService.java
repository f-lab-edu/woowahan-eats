package com.flab.woowahaneats.domain.admin.application;

import com.flab.woowahaneats.domain.auth.domain.Account;
import com.flab.woowahaneats.domain.admin.exception.DuplicateEmailException;
import com.flab.woowahaneats.domain.admin.controller.dto.AdminSignUpRequest;
import com.flab.woowahaneats.domain.admin.domain.Admin;
import com.flab.woowahaneats.domain.auth.repository.AccountRepository;
import com.flab.woowahaneats.domain.admin.repository.AdminRepository;
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

        Account account = accountRepository.save(Account.create(
                adminSignUpRequest.email(),
                encodedPassword
        ));

        Admin admin = Admin.builder()
                .id(adminSignUpRequest.id())
                .accountId(account.getId())
                .name(adminSignUpRequest.name())
                .phoneNumber(adminSignUpRequest.phoneNumber())
                .build();

        adminRepository.save(admin);
    }
}
