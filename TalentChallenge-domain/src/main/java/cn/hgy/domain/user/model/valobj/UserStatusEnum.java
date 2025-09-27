package cn.hgy.domain.user.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-09-24
 * @Description: 用户注册账号状态
 */
@Getter
@AllArgsConstructor
public enum UserStatusEnum {
    // user表 -- isActive
    PENDING_VERIFICATION("待验证", 0),
    ACTIVE("已激活", 1),
    REJECTED("已拒绝", 2),

    // user-auth表 -- isValify
    UN_VALIFY("未验证", 0),
    IS_VALIFY("已验证", 1)
    ;

    private final String statusName;
    private final Integer statusCode;
}
