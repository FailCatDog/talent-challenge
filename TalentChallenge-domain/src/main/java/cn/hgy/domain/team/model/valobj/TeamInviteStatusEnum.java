package cn.hgy.domain.team.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TeamInviteStatusEnum {

    PENDING("pending", "待处理"),
    ACCEPTED("accepted", "已接受"),
    REJECTED("rejected", "已拒绝"),
    CANCELLED("cancelled", "已取消");

    private final String code;
    private final String name;

    public static TeamInviteStatusEnum getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (TeamInviteStatusEnum value : values()) {
            if (value.code.equalsIgnoreCase(code)) {
                return value;
            }
        }
        return null;
    }
}
