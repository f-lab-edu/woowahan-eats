package com.flab.woowahaneats.global.interceptor;

import com.flab.woowahaneats.domain.auth.AdminAuthContext;
import com.flab.woowahaneats.domain.member.application.exception.AdminNotFoundException;
import com.flab.woowahaneats.domain.member.domain.Admin;
import com.flab.woowahaneats.domain.member.repository.AdminRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminAuthInterceptor extends AuthInterceptor {

    private final AdminRepository adminRepository;

    @Override
    protected boolean checkPermission(Long accountId) {
        Admin admin = adminRepository.findByAccountId(accountId);

        if (admin == null) {
            throw new AdminNotFoundException();
        }

        AdminAuthContext.setAdmin(admin);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex){
        AdminAuthContext.clear();
    }
}