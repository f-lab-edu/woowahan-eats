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

        Account account = accountRepository.save(Account.create(
                ownerSignUpRequest.email(),
                encodedPassword
        ));

        Owner owner = Owner.create(
                account.getId(),
                ownerSignUpRequest.name(),
                ownerSignUpRequest.phoneNumber(),
                ownerSignUpRequest.address(),
                ownerSignUpRequest.location(),
                ownerSignUpRequest.businessRegistrationCertUrl(),
                ownerSignUpRequest.businessNotificationCertUrl(),
                ownerSignUpRequest.bankAccount()
        );

        ownerRepository.save(owner);
    }

}
