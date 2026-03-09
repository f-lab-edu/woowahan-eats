package com.flab.woowahaneats.global.interceptor;

import com.flab.woowahaneats.domain.auth.AuthContext;
import com.flab.woowahaneats.domain.auth.AuthContextHolder;
import com.flab.woowahaneats.domain.member.domain.Rider;
import com.flab.woowahaneats.domain.member.repository.RiderRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RiderAuthHandler implements AuthHandler {

    private final RiderRepository riderRepository;

    @Override
    public boolean supports(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/rider/");
    }

    @Override
    public boolean handleAuth(HttpServletRequest request, HttpServletResponse response) {
        Long accountId = (Long) request.getSession().getAttribute("accountId");

        Rider rider = riderRepository.findByAccountId(accountId);

        if (rider == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        AuthContextHolder.setContext(new AuthContext(rider, "RIDER"));
        return true;
    }
}