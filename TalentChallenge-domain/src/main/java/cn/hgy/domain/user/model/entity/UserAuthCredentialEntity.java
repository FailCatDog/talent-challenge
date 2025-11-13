package cn.hgy.domain.user.model.entity;

import cn.hgy.domain.user.model.valobj.UserStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-09-22
 * @Description: 用户认证信息实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthCredentialEntity {
    
    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    
    /**
     * 用户认证唯一ID
     */
    private String userAuthCredentialId;

    /**
     * 关连用户id
     */
    private String userId;

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
     * 是否验证
     */
    private Integer isVerified;

    /**
     * 验证是时间
     */
    private LocalDateTime verifyTime;

    /**
     * 加密密码
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public static String encryptPassword(String rawPassword) {
        return PASSWORD_ENCODER.encode(rawPassword);
    }

    /**
     * 验证密码是否正确
     * @param rawPassword 原始密码
     * @return 是否匹配
     */
    public Boolean verifyPassword(String rawPassword) {
        return PASSWORD_ENCODER.matches(rawPassword, this.credential);
    }
}
