package com.flab.woowahaneats.domain.auth.application;

import com.flab.woowahaneats.domain.auth.controller.dto.AuthLoginRequest;
import com.flab.woowahaneats.domain.member.domain.Owner;
import com.flab.woowahaneats.domain.member.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final OwnerRepository ownerRepository;

    public Owner login(AuthLoginRequest authLoginRequest) {

        Owner owner = ownerRepository.findByEmail(authLoginRequest.email());
        if (owner == null) {
            throw new IllegalArgumentException("해당 email의 Owner가 없습니다.");
        }

        if (!owner.getPassword().equals(authLoginRequest.password())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return owner;
    }
}
