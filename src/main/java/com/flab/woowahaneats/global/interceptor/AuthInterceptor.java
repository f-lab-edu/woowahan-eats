package com.flab.woowahaneats.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final List<AuthHandler> authHandlers;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("accountId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        AuthHandler authHandler = authHandlers.stream()
                .filter(h -> h.supports(request))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No AuthHandler found for path: " + request.getRequestURI()));

        return authHandler.handleAuth(request, response);
    }

}
