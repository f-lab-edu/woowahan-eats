package com.flab.woowahaneats.global.interceptor;

import com.flab.woowahaneats.domain.auth.AuthContext;
import com.flab.woowahaneats.domain.auth.AuthContextHolder;
import com.flab.woowahaneats.domain.member.domain.Owner;
import com.flab.woowahaneats.domain.member.repository.OwnerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OwnerAuthHandler implements AuthHandler {

    private final OwnerRepository ownerRepository;

    @Override
    public boolean supports(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/owner/") || path.startsWith("/restaurant/");
    }

    @Override
    public boolean handleAuth(HttpServletRequest request, HttpServletResponse response) {
        Long accountId = (Long) request.getSession().getAttribute("accountId");

        Owner owner = ownerRepository.findByAccountId(accountId);

        if (owner == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        AuthContextHolder.setContext(new AuthContext(owner, "OWNER"));
        return true;
    }
}