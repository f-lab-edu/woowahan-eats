package com.flab.woowahaneats.global.interceptor;

import com.flab.woowahaneats.domain.auth.OwnerAuthContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("accountId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        Long accountId = (Long) session.getAttribute("accountId");

        return checkPermission(accountId);
    }

    protected abstract boolean checkPermission(Long accountId);

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex){
        OwnerAuthContext.clear();
    }
}
