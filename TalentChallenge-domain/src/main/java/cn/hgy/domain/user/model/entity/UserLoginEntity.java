package cn.hgy.domain.user.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-09-26
 * @Description: 用户登录实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginEntity {
    /**
     * 用户唯一标识
     */
    private String userId;

    /**
     * 用户类型
     */
    private String type;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像图片URL
     */
    private String avatarUrl;

    /**
     * 用户认证唯一ID
     */
    private String userAuthCredentialId;

    /**
     * 认证类型
     */
    private String identityType;

    /**
     * 唯一标识符
     */
    private String identifier;

    /**
     * 凭证
     */
    private String credential;

    /**
     * 账号是否激活（1-激活，2-禁用）
     */
    private Integer isActive;


    /**
     * 是否验证
     */
    private Integer isVerified;

    /**
     * 验证时间
     */
    private LocalDateTime verifyTime;

    /**
     * 上次使用时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 令牌
     */
    private String authentication;
}
