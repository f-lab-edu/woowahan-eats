package com.flab.woowahaneats.global.config;

import com.flab.woowahaneats.global.interceptor.OwnerAuthInterceptor;
import com.flab.woowahaneats.global.interceptor.UserAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final OwnerAuthInterceptor ownerAuthInterceptor;
    private final UserAuthInterceptor userAuthInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ownerAuthInterceptor)
                .addPathPatterns(
                        "/restaurant/register",
                        "/restaurant/open/{restaurantId}"
                        );

        registry.addInterceptor(userAuthInterceptor)
                .addPathPatterns("/**") // 수정 예정
                .excludePathPatterns(
                "/auth/login",
                "/user/sign-up",
                "/owner/sign-up",
                "/restaurant/register",
                "/restaurant/open/{restaurantId}"
        );

    }
}
