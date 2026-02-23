package com.flab.woowahaneats.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class AuthInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("accountId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        Long accountId = (Long) session.getAttribute("accountId");

        return checkPermission(accountId, request, response);
    }

    protected abstract boolean checkPermission(Long accountId, HttpServletRequest request, HttpServletResponse response);

}
