package com.flab.woowahaneats.domain.owner.application;

import com.flab.woowahaneats.domain.owner.exception.DuplicateEmailException;
import com.flab.woowahaneats.domain.owner.controller.dto.OwnerSignUpRequest;
import com.flab.woowahaneats.domain.auth.domain.Account;
import com.flab.woowahaneats.domain.owner.domain.Owner;
import com.flab.woowahaneats.domain.auth.repository.AccountRepository;
import com.flab.woowahaneats.domain.owner.repository.OwnerRepository;
import com.flab.woowahaneats.global.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUpOwner(OwnerSignUpRequest ownerSignUpRequest) {

        Account existingAccount = accountRepository.findByEmail(ownerSignUpRequest.email());
        if (existingAccount != null) {
            throw new DuplicateEmailException();
        }

        String encodedPassword = passwordEncoder.encode(ownerSignUpRequest.password());

        Account account = Account.builder()
                .id(ownerSignUpRequest.id())
                .password(encodedPassword)
                .email(ownerSignUpRequest.email())
                .build();

        accountRepository.save(account);

        Owner owner = Owner.builder()
                .id(ownerSignUpRequest.id())
                .accountId(account.getId())
                .address(ownerSignUpRequest.address())
                .location(ownerSignUpRequest.location())
                .businessNotificationCertUrl(ownerSignUpRequest.businessNotificationCertUrl())
                .businessRegistrationCertUrl(ownerSignUpRequest.businessRegistrationCertUrl())
                .bankAccount(ownerSignUpRequest.bankAccount())
                .name(ownerSignUpRequest.name())
                .phoneNumber(ownerSignUpRequest.phoneNumber())
                .build();

        ownerRepository.save(owner);
    }

}