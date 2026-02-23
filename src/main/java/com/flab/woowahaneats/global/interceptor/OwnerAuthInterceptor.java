package com.flab.woowahaneats.global.interceptor;

import com.flab.woowahaneats.domain.member.application.exception.OwnerNotFoundException;
import com.flab.woowahaneats.domain.member.domain.Owner;
import com.flab.woowahaneats.domain.member.repository.OwnerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OwnerAuthInterceptor extends AuthInterceptor {

    private final OwnerRepository ownerRepository;

    @Override
    protected boolean checkPermission(Long accountId, HttpServletRequest request, HttpServletResponse response) {
        Owner owner = ownerRepository.findByAccountId(accountId);

        if (owner == null) {
            throw new OwnerNotFoundException();
        }

        request.setAttribute("owner", owner);
        return true;
    }
}
