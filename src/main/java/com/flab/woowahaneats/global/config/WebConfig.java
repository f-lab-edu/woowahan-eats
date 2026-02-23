package com.flab.woowahaneats.global.config;

import com.flab.woowahaneats.global.interceptor.AuthInterceptor;
import com.flab.woowahaneats.global.interceptor.OwnerAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final OwnerAuthInterceptor ownerAuthInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ownerAuthInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/auth/login",
                        "/owner/sign-up"
                        );

    }
}
