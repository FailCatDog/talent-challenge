package cn.hgy.trigger.interceptor;

import cn.hgy.infrastructure.persistent.jwt.JWTServiceImpl;
import cn.hgy.types.enums.ResponseCode;
import cn.hgy.types.exception.AppException;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(UserContextDynamicInterceptor.class);
    private final JWTServiceImpl jwtService;
    private static final Integer ADMIN_TYPE = 3;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String AUTHORIZATION_HEADER = "Authorization";
        String token = request.getHeader(AUTHORIZATION_HEADER);
        String requestURI = request.getRequestURI();

        Integer userType = null;
        if (StrUtil.isNotBlank(token)) {
            userType = jwtService.parseUserType(token);
        }

        if (requestURI.contains("/admin/")) {
            if (StrUtil.isBlank(token) || userType == null || !ADMIN_TYPE.equals(userType)) {
                throw new AppException(ResponseCode.ADMIN_PERMISSION_DENIED.getCode(), ResponseCode.ADMIN_PERMISSION_DENIED.getInfo());
            }
        }

        if (StrUtil.isNotBlank(token)) {
            String userId = jwtService.parseToken(token);
            UserContextDynamic.setUserContext(userId, userType);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContextDynamic.removeUserContextDynamic();
    }
}