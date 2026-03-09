package com.flab.woowahaneats.global.interceptor;

import com.flab.woowahaneats.domain.auth.UserAuthContext;
import com.flab.woowahaneats.domain.member.domain.User;
import com.flab.woowahaneats.domain.member.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAuthHandler implements AuthHandler {

    private final UserRepository userRepository;

    @Override
    public boolean supports(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/user/");
    }

    @Override
    public boolean handleAuth(HttpServletRequest request, HttpServletResponse response) {
        Long accountId = (Long) request.getSession().getAttribute("accountId");

        User user = userRepository.findByAccountId(accountId);

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        UserAuthContext.setUser(user);
        return true;
    }
}
