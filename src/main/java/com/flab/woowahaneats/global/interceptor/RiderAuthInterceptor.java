package com.flab.woowahaneats.global.interceptor;

import com.flab.woowahaneats.domain.auth.RiderAuthContext;
import com.flab.woowahaneats.domain.member.application.exception.RiderNotFoundException;
import com.flab.woowahaneats.domain.member.domain.Rider;
import com.flab.woowahaneats.domain.member.repository.RiderRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RiderAuthInterceptor extends AuthInterceptor {

    private final RiderRepository riderRepository;

    @Override
    protected boolean checkPermission(Long accountId) {
        Rider rider = riderRepository.findByAccountId(accountId);

        if (rider == null) {
            throw new RiderNotFoundException();
        }

        RiderAuthContext.setRider(rider);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex){
        RiderAuthContext.clear();
    }
}