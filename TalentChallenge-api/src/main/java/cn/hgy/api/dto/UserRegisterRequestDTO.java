package cn.hgy.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-09-22
 * @Description: 用户注册数据传输 DTO
 */
@Data
public class UserRegisterRequestDTO {
    /**
     * 用户昵称
     */
    @NotBlank(message = "用户昵称不能为空")
    private String nickname;

    /**
     * 用户类型
     */
    @NotBlank(message = "用户类型不能为空")
    private String type;

    /**
     * 账号认证类型
     */
    @NotBlank(message = "账号认证类型不能为空")
    private String identityType;

    /**
     * 账号认证唯一标识符
     */
    @NotBlank(message = "账号唯一标识符不能为空")
    private String identifier;

    /**
     * 凭证
     */
    @NotBlank(message = "账号凭证不能为空")
    private String credential;
}
