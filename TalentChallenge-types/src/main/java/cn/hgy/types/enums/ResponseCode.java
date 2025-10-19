package cn.hgy.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS("0000", "成功"),
    UN_ERROR("0001", "未知失败"),
    ILLEGAL_PARAMETER("0002", "非法参数"),

    USER_REGISTER_FAIL("ERR_BIZ_001", "用户注册失败"),
    USER_IS_EXIST("ERR_BIZ_002", "该用户已存在"),
    USER_IS_NOT_EXIST("ERR_BIZ_003", "该用户不存在"),
    USER_CREDENTIAL_NOT_TRUE("ERR_BIZ_004", "登录凭证不正确"),
    USER_IS_NOT_ACTIVE("ERR_BIZ_005", "用户账户为禁用状态"),
    USER_LOGIN_FAIL("ERR_BIZ_006", "用户登录失败"),

    PROJECT_CREATE_FAIL("ERR_BIZ_007", "项目创建失败"),
    PROJECT_TITLE_INVALID("ERR_BIZ_008", "项目标题长度不符合要求"),
    PROJECT_DESCRIPTION_INVALID("ERR_BIZ_009", "项目描述长度不符合要求"),
    PROJECT_BUDGET_INVALID("ERR_BIZ_010", "项目预算金额必须大于0"),
    PROJECT_TEAM_SIZE_INVALID("ERR_BIZ_011", "团队人数设置不合理"),
    PROJECT_DURATION_INVALID("ERR_BIZ_012", "项目持续天数必须大于0"),
    PROJECT_NOT_FOUND("ERR_BIZ_013", "项目不存在"),
    PROJECT_UPDATE_FAIL("ERR_BIZ_014", "项目更新失败"),
    PROJECT_PERMISSION_DENIED("ERR_BIZ_015", "无权限操作该项目"),
    PROJECT_STATUS_INVALID("ERR_BIZ_016", "项目状态不允许此操作")
    ;

    private String code;
    private String info;

}
