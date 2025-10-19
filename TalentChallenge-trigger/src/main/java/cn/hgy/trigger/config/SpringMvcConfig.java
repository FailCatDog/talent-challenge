package cn.hgy.trigger.config;

import cn.hgy.trigger.interceptor.UserContextDynamicInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-09-28
 * @Description: Mvc配置类
 */
@Configuration
@RequiredArgsConstructor
public class SpringMvcConfig implements WebMvcConfigurer {
    private final UserContextDynamicInterceptor userContextDynamicInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userContextDynamicInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/public");
    }
}
