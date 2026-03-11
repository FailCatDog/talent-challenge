package cn.hgy.types.enums;

/**
 * 企业认证状态枚举
 *
 * @author hgy
 */
public enum CertificationStatusEnum {

    /** 未认证（默认状态） */
    UNCERTIFIED("uncertified"),

    /** 待审核（已提交资质文件） */
    PENDING_REVIEW("pending_review"),

    /** 已认证 */
    CERTIFIED("certified"),

    /** 已拒绝 */
    REJECTED("rejected");

    private final String code;

    CertificationStatusEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
