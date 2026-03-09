package com.flab.woowahaneats.global.interceptor;

import com.flab.woowahaneats.domain.auth.AdminAuthContext;
import com.flab.woowahaneats.domain.member.domain.Admin;
import com.flab.woowahaneats.domain.member.repository.AdminRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminAuthHandler implements AuthHandler {

    private final AdminRepository adminRepository;

    @Override
    public boolean supports(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/admin/");
    }

    @Override
    public boolean handleAuth(HttpServletRequest request, HttpServletResponse response) {
        Long accountId = (Long) request.getSession().getAttribute("accountId");

        Admin admin = adminRepository.findByAccountId(accountId);

        if (admin == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        AdminAuthContext.setAdmin(admin);
        return true;
    }
}