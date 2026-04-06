package com.flab.woowahaneats.domain.rider.application;

import com.flab.woowahaneats.domain.auth.domain.Account;
import com.flab.woowahaneats.domain.rider.exception.DuplicateEmailException;
import com.flab.woowahaneats.domain.rider.exception.RiderNotFoundException;
import com.flab.woowahaneats.domain.rider.controller.dto.RiderSignUpRequest;
import com.flab.woowahaneats.domain.rider.domain.Rider;
import com.flab.woowahaneats.domain.auth.repository.AccountRepository;
import com.flab.woowahaneats.domain.rider.repository.RiderRepository;
import com.flab.woowahaneats.global.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RiderService {
    private final RiderRepository riderRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUpRider(RiderSignUpRequest riderSignUpRequest) {
        Account existingAccount = accountRepository.findByEmail(riderSignUpRequest.email());

        if (existingAccount != null) {
            throw new DuplicateEmailException();
        }

        String encodedPassword = passwordEncoder.encode(riderSignUpRequest.password());

        Account account = accountRepository.save(Account.create(
                riderSignUpRequest.email(),
                encodedPassword
        ));

        Rider rider = Rider.create(
                account,
                riderSignUpRequest.name(),
                riderSignUpRequest.phoneNumber(),
                riderSignUpRequest.location(),
                riderSignUpRequest.bankAccount(),
                riderSignUpRequest.vehicleType()
        );

        riderRepository.save(rider);
    }

    @Transactional
    public void startWork(Long riderId) {
        Rider rider = getRider(riderId);
        rider.startWork();
        riderRepository.save(rider);
    }

    @Transactional
    public void endWork(Long riderId) {
        Rider rider = getRider(riderId);
        rider.endWork();
        riderRepository.save(rider);
    }

    @Transactional
    public void startDelivering(Long riderId) {
        Rider rider = getRider(riderId);
        rider.startDelivering();
        riderRepository.save(rider);
    }

    @Transactional
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
