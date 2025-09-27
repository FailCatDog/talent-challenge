package cn.hgy.infrastructure.persistent.po;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户认证凭证PO对象
 * 
 * @author hgy
 */
@Data
public class UserAuthCredential {
    
    private Long id;
    private String userAuthCredentialId;
    private String userId;
    private String identityType;
    private String identifier;
    private String credential;
    private Integer isVerified;
    private LocalDateTime verifyTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
