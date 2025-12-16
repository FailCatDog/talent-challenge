package cn.hgy.domain.team.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TeamStatusEnum {

    ACTIVE("active", "活跃"),
    INACTIVE("inactive", "不活跃"),
    DISBANDED("disbanded", "已解散");

    private final String code;
    private final String name;

    public static TeamStatusEnum getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (TeamStatusEnum statusEnum : values()) {
            if (statusEnum.getCode().equalsIgnoreCase(code)) {
                return statusEnum;
            }
        }
        return null;
    }
}

