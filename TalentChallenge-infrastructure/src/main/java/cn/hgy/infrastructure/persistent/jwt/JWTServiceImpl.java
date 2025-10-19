package cn.hgy.infrastructure.persistent.jwt;

import cn.hgy.domain.user.service.IJWTService;
import cn.hutool.core.lang.UUID;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-09-15
 * @Description: JWT工具类基类，用于封装公共逻辑
 */
@Component
@Slf4j
public class JWTServiceImpl implements IJWTService {

    @Value("${app.jwt.secret}")
    private String secret;
    @Value("${app.jwt.expiration}")
    private Long expiration;
    @Value("${app.jwt.refresh-expiration}")
    private Long refreshExpiration;

    private SecretKey secretKey;

    @PostConstruct
    public void initSecretKey() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 构建用户相关Claims
     * 统一使用 userId 作为主题(subject)，并将用户类型(type)作为一个标准声明(claim)
     */
    @Override
    public Map<String, Object> buildUserClaims(String userId, Map<String, Object> additionalClaims) {
        Map<String, Object> claims = new HashMap<>();
        // 标准声明
        claims.put("sub", userId); // JWT标准主题

        // 业务相关声明，可根据需要添加
        // claims.put("type", userType);

        // 合并调用方可能传入的额外声明
        if (additionalClaims != null) {
            claims.putAll(additionalClaims);
        }

        return claims;
    }

    /**
     * 从Claims提取用户标识
     * 直接从JWT的标准主题(subject)中提取用户ID
     */
    @Override
    public String extractUserId(Claims claims) {
        return claims.getSubject();
    }

    /**
     * 生成jwt令牌
     * @param userId 用户id
     * @param additionalClaims 额外后缀
     * @return 令牌
     */
    @Override
    public String generateToken(String userId, Map<String, Object> additionalClaims) {
        // 1. 构建公共Claims（jti、过期时间配置）
        Map<String, Object> commonClaims = new HashMap<>();
        commonClaims.put("jti", UUID.randomUUID().toString()); // 唯一标识
        commonClaims.put("expiration", expiration); // 过期时间配置
        commonClaims.put("refreshExpiration", refreshExpiration); // 刷新过期时间配置（可选）

        // 2. 合并用户Claims和公共Claims
        Map<String, Object> allClaims = new HashMap<>(commonClaims);
        allClaims.putAll(buildUserClaims(userId, additionalClaims));

        // 3. 调用公共方法创建Token
        return createToken(allClaims, expiration);
    }

    /**
     * 创建Token
     */
    private String createToken(Map<String, Object> claims, long expirationMs) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey)
                .compact();
    }

    public String parseToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return extractUserId(claims);
    }
}