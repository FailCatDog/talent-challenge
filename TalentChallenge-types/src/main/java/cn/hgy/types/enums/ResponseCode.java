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
    USER_LOGIN_FAIL("ERR_BIZ_006", "用户登录失败")
    ;

    private String code;
    private String info;

}
