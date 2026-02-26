package com.flab.woowahaneats.global.interceptor;

import com.flab.woowahaneats.domain.auth.UserAuthContext;
import com.flab.woowahaneats.domain.member.application.exception.UserNotFoundException;
import com.flab.woowahaneats.domain.member.domain.User;
import com.flab.woowahaneats.domain.member.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAuthInterceptor extends AuthInterceptor {

    private final UserRepository userRepository;

    @Override
    protected boolean checkPermission(Long accountId) {
        User user = userRepository.findByAccountId(accountId);

        if (user == null) {
            throw new UserNotFoundException();
        }

        UserAuthContext.setUser(user);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex){
        UserAuthContext.clear();
    }

}
