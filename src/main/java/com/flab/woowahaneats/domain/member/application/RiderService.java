package com.flab.woowahaneats.domain.member.application;

import com.flab.woowahaneats.domain.auth.domain.Account;
import com.flab.woowahaneats.domain.member.application.exception.DuplicateEmailException;
import com.flab.woowahaneats.domain.member.controller.dto.RiderSignUpRequest;
import com.flab.woowahaneats.domain.member.domain.Rider;
import com.flab.woowahaneats.domain.member.domain.RiderStatus;
import com.flab.woowahaneats.domain.member.repository.AccountRepository;
import com.flab.woowahaneats.domain.member.repository.RiderRepository;
import com.flab.woowahaneats.global.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiderService {
    private final RiderRepository riderRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUpRider(RiderSignUpRequest riderSignUpRequest) {
        Account existingAccount = accountRepository.findByEmail(riderSignUpRequest.email());

        if (existingAccount != null) {
            throw new DuplicateEmailException();
        }

        String encodedPassword = passwordEncoder.encode(riderSignUpRequest.password());

        Account account = Account.builder()
                .id(riderSignUpRequest.id())
                .password(encodedPassword)
                .email(riderSignUpRequest.email())
                .build();

        accountRepository.save(account);

        Rider rider = Rider.builder()
                .id(riderSignUpRequest.id())
                .accountId(account.getId())
                .name(riderSignUpRequest.name())
                .phoneNumber(riderSignUpRequest.phoneNumber())
                .location(riderSignUpRequest.location())
                .bankAccount(riderSignUpRequest.bankAccount())
                .vehicleType(riderSignUpRequest.vehicleType())
                .riderStatus(RiderStatus.WAITING)
                .build();

        riderRepository.save(rider);
    }
}
