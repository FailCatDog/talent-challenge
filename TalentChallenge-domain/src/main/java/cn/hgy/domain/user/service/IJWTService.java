package cn.hgy.domain.user.service;

import io.jsonwebtoken.Claims;

import java.util.Map;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-09-27
 * @Description: jwt服务
 */
public interface IJWTService {

    Map<String, Object> buildUserClaims(String userId, Map<String, Object> additionalClaims);

    String extractUserId(Claims claims);

    String generateToken(String userId, Map<String, Object> additionalClaims);
}
