package com.flab.woowahaneats.domain.member.application;

import com.flab.woowahaneats.domain.member.controller.dto.OwnerSignUpRequest;
import com.flab.woowahaneats.domain.member.domain.Account;
import com.flab.woowahaneats.domain.member.domain.Owner;
import com.flab.woowahaneats.domain.member.repository.AccountRepository;
import com.flab.woowahaneats.domain.member.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final AccountRepository accountRepository;

    public void signUpOwner(OwnerSignUpRequest ownerSignUpRequest) {

        Account account = Account.builder()
                .id(ownerSignUpRequest.id())
                .name(ownerSignUpRequest.name())
                .password(ownerSignUpRequest.password())
                .email(ownerSignUpRequest.email())
                .phoneNumber(ownerSignUpRequest.phoneNumber())
                .build();

        accountRepository.save(account);

        Owner owner = Owner.builder()
                .id(ownerSignUpRequest.id())
                .accountId(account.getId())
                .address(ownerSignUpRequest.address())
                .businessNotificationCertUrl(ownerSignUpRequest.businessNotificationCertUrl())
                .businessRegistrationCertUrl(ownerSignUpRequest.businessRegistrationCertUrl())
                .bankAccount(ownerSignUpRequest.bankAccount())
                .build();

        ownerRepository.save(owner);
    }

}
