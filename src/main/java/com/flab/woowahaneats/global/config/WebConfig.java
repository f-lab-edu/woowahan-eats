package com.flab.woowahaneats.global.config;

import com.flab.woowahaneats.global.interceptor.AdminAuthInterceptor;
import com.flab.woowahaneats.global.interceptor.OwnerAuthInterceptor;
import com.flab.woowahaneats.global.interceptor.RiderAuthInterceptor;
import com.flab.woowahaneats.global.interceptor.UserAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AdminAuthInterceptor adminAuthInterceptor;
    private final OwnerAuthInterceptor ownerAuthInterceptor;
    private final UserAuthInterceptor userAuthInterceptor;
    private final RiderAuthInterceptor riderAuthInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminAuthInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/sign-up");

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
                "/rider/sign-up",
                "/admin/sign-up",
                "/restaurant/register",
                "/restaurant/open/{restaurantId}"
        );

        registry.addInterceptor(riderAuthInterceptor)
                .addPathPatterns("/rider/**")
                .excludePathPatterns("/rider/sign-up");

    }
}
