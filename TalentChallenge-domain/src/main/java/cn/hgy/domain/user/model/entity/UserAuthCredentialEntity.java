package cn.hgy.domain.user.model.entity;

import cn.hgy.domain.user.model.valobj.UserStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
     * 验证凭证是否正确
     * @param credential 凭证
     * @return 是否成功
     */
    public Boolean verifyPassword(String credential) {
        return credential.equals(this.credential);
    }
}
