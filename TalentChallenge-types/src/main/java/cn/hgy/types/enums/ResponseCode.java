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
    USER_TYPE_NOT_ALLOWED("ERR_BIZ_021", "不允许注册该类型账号"),

    PROJECT_CREATE_FAIL("ERR_BIZ_007", "项目创建失败"),
    PROJECT_TITLE_INVALID("ERR_BIZ_008", "项目标题长度不符合要求"),
    PROJECT_DESCRIPTION_INVALID("ERR_BIZ_009", "项目描述长度不符合要求"),
    PROJECT_BUDGET_INVALID("ERR_BIZ_010", "项目预算金额必须大于0"),
    PROJECT_TEAM_SIZE_INVALID("ERR_BIZ_011", "团队人数设置不合理"),
    PROJECT_DURATION_INVALID("ERR_BIZ_012", "项目持续天数必须大于0"),
    PROJECT_NOT_FOUND("ERR_BIZ_013", "项目不存在"),
    PROJECT_UPDATE_FAIL("ERR_BIZ_014", "项目更新失败"),
    PROJECT_PERMISSION_DENIED("ERR_BIZ_015", "无权限操作该项目"),
    PROJECT_STATUS_INVALID("ERR_BIZ_016", "项目状态不允许此操作"),
    MILESTONE_INIT_FAIL("ERR_BIZ_050", "里程碑初始化失败"),
    MILESTONE_ALREADY_EXISTS("ERR_BIZ_051", "该里程碑已存在"),
    MILESTONE_CREATE_FAIL("ERR_BIZ_052", "里程碑创建失败"),
    MILESTONE_TEMPLATE_NOT_FOUND("ERR_BIZ_053", "里程碑模板不存在"),

    CATEGORY_NAME_EMPTY("ERR_BIZ_017", "分类名称不能为空"),
    CATEGORY_NAME_EXISTS("ERR_BIZ_018", "分类名称已存在"),
    PARENT_CATEGORY_NOT_FOUND("ERR_BIZ_019", "父分类不存在"),
    CATEGORY_NOT_FOUND("ERR_BIZ_020", "分类不存在"),

    BID_APPLY_FAIL("ERR_BIZ_030", "竞榜申请失败"),
    BID_DUPLICATE("ERR_BIZ_031", "已竞榜该项目"),
    BID_PROJECT_INVALID("ERR_BIZ_032", "项目不支持竞榜"),
    BID_NOT_FOUND("ERR_BIZ_033", "竞榜记录不存在"),
    BID_STATUS_INVALID("ERR_BIZ_034", "竞榜状态不允许此操作"),
    BID_PERMISSION_DENIED("ERR_BIZ_035", "无权限操作该竞榜记录"),
    BID_TEAM_REQUIRED("ERR_BIZ_036", "请先建立队伍后再进行揭榜"),
    BID_TEAM_NOT_FOUND("ERR_BIZ_037", "团队不存在或已解散"),

    TEAM_CREATE_FAIL("ERR_BIZ_040", "团队创建失败"),
    TEAM_UPDATE_FAIL("ERR_BIZ_041", "团队更新失败"),
    TEAM_DISBAND_FAIL("ERR_BIZ_042", "团队解散失败"),
    TEAM_NOT_FOUND("ERR_BIZ_043", "团队不存在"),
    TEAM_ALREADY_EXISTS("ERR_BIZ_044", "您已拥有活跃的团队"),
    TEAM_PERMISSION_DENIED("ERR_BIZ_045", "无权限操作该团队"),
    TEAM_STATUS_INVALID("ERR_BIZ_046", "团队状态不允许此操作"),

    ADMIN_PERMISSION_DENIED("ERR_AUTH_001", "需要管理员权限"),

    SYSTEM_ERROR("ERR_SYSTEM_001", "系统错误")
    ;

    private String code;
    private String info;

}
