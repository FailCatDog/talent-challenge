package cn.hgy.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-09-27
 * @Description: 用户登录请求传输对象
 */
@Data
public class UserLoginRequestDTO {

    @NotBlank(message = "账号认证类型不能为空")
    private String identityType;
    @NotBlank(message = "账号唯一标识符不能为空")
    private String identifier;
    @NotBlank(message = "账号凭证不能为空")
    private String credential;
}
