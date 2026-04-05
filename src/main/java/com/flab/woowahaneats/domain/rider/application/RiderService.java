package com.flab.woowahaneats.domain.rider.application;

import com.flab.woowahaneats.domain.auth.domain.Account;
import com.flab.woowahaneats.domain.rider.application.exception.DuplicateEmailException;
import com.flab.woowahaneats.domain.rider.application.exception.RiderNotFoundException;
import com.flab.woowahaneats.domain.rider.controller.dto.RiderSignUpRequest;
import com.flab.woowahaneats.domain.rider.domain.Rider;
import com.flab.woowahaneats.domain.rider.domain.RiderStatus;
import com.flab.woowahaneats.domain.auth.repository.AccountRepository;
import com.flab.woowahaneats.domain.rider.repository.RiderRepository;
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
                .riderStatus(RiderStatus.RESTING)
                .build();

        riderRepository.save(rider);
    }

    public void startWork(Long riderId) {
        Rider rider = getRider(riderId);
        rider.startWork();
        riderRepository.save(rider);
    }

    public void endWork(Long riderId) {
        Rider rider = getRider(riderId);
        rider.endWork();
        riderRepository.save(rider);
    }

    public void startDelivering(Long riderId) {
        Rider rider = getRider(riderId);
        rider.startDelivering();
        riderRepository.save(rider);
    }

    public void finishDelivering(Long riderId) {
        Rider rider = getRider(riderId);
        rider.finishDelivering();
        riderRepository.save(rider);
    }

    private Rider getRider(Long riderId) {
        return riderRepository.findById(riderId)
                .orElseThrow(RiderNotFoundException::new);
    }
}