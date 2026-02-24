package com.flab.woowahaneats.global.interceptor;

import com.flab.woowahaneats.domain.auth.OwnerAuthContext;
import com.flab.woowahaneats.domain.member.application.exception.OwnerNotFoundException;
import com.flab.woowahaneats.domain.member.domain.Owner;
import com.flab.woowahaneats.domain.member.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OwnerAuthInterceptor extends AuthInterceptor {

    private final OwnerRepository ownerRepository;

    @Override
    protected boolean checkPermission(Long accountId) {
        Owner owner = ownerRepository.findByAccountId(accountId);

        if (owner == null) {
            throw new OwnerNotFoundException();
        }

        OwnerAuthContext.setOwner(owner);
        return true;
    }
}
