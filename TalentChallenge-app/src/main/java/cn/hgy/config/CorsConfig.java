package cn.hgy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置类
 * 允许所有域名、所有请求方法、所有请求头，并支持携带凭证
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 允许所有域名访问
                .allowedOriginPatterns("*")
                // 允许所有请求方法
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                // 允许所有请求头
                .allowedHeaders("*")
                // 允许携带凭证（如cookies）
                .allowCredentials(true)
                // 预检请求缓存时间（秒）
                .maxAge(3600);
    }
}

