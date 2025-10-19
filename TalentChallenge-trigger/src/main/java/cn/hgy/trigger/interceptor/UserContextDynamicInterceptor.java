package cn.hgy.trigger.interceptor;

import cn.hgy.infrastructure.persistent.jwt.JWTServiceImpl;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-09-28
 * @Description: 用户上下文拦截器 用于从authentication认证令牌中获取用户上下文
 */
@Component("UserContextDynamicInterceptor")
@RequiredArgsConstructor
public class UserContextDynamicInterceptor implements HandlerInterceptor {

    private final JWTServiceImpl jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String AUTHORIZATION_HEADER = "Authorization";
        String token = request.getHeader(AUTHORIZATION_HEADER);

        if (StrUtil.isNotBlank(token)) {
            String userId = jwtService.parseToken(token);
            UserContextDynamic.setUserContextDynamic(userId);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContextDynamic.removeUserContextDynamic();
    }
}