package com.flab.woowahaneats.domain.member.application;

import com.flab.woowahaneats.domain.member.controller.dto.OwnerLoginRequest;
import com.flab.woowahaneats.domain.member.controller.dto.OwnerSignUpRequest;
import com.flab.woowahaneats.domain.member.domain.Owner;
import com.flab.woowahaneats.domain.member.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;

    public void signUpOwner(OwnerSignUpRequest ownerSignUpRequest) {

        if (ownerRepository.findById(ownerSignUpRequest.id()) != null) {
            throw new IllegalArgumentException("이미 있는 회원입니다.");
        }

        Owner owner = Owner.builder()
                .id(ownerSignUpRequest.id())
                .name(ownerSignUpRequest.name())
                .password(ownerSignUpRequest.password())
                .email(ownerSignUpRequest.email())
                .phoneNumber(ownerSignUpRequest.phoneNumber())
                .address(ownerSignUpRequest.address())
                .businessNumber(ownerSignUpRequest.businessNumber())
                .accountNumber(ownerSignUpRequest.accountNumber())
                .bankName(ownerSignUpRequest.bankName())
                .build();

        ownerRepository.save(owner);
    }

    public Owner loginOwner(OwnerLoginRequest ownerLoginRequest) {

        Owner owner = ownerRepository.findByEmail(ownerLoginRequest.email());
        if (owner == null) {
            throw new IllegalArgumentException("해당 email의 Owner가 없습니다.");
        }

        if (!owner.getPassword().equals(ownerLoginRequest.password())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return owner;
    }
}
