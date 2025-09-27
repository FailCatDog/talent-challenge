package cn.hgy.domain.user.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-09-27
 * @Description: 用户类型
 */
@Getter
@AllArgsConstructor
public enum UserTypeEnum {

    STUDENT("学生", 1),
    COMPANY("企业", 2),
    ADMIN("管理员", 3)
    ;


    private final String type;
    private final int code;

    public static Integer getCodeByType(String type) {
        for (UserTypeEnum userTypeEnum : UserTypeEnum.values()) {
            if (userTypeEnum.getType().equals(type)) {
                return userTypeEnum.getCode();
            }
        }

        return STUDENT.code;
    }

    public static String getTypeByCode(Integer type) {
        for (UserTypeEnum userTypeEnum : UserTypeEnum.values()) {
            if (userTypeEnum.getCode() == type) {
                return userTypeEnum.getType();
            }
        }

        return STUDENT.type;
    }
}
