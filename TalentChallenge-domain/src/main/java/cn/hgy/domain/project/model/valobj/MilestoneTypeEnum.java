package cn.hgy.domain.project.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-11-25
 * @Description: 里程碑类型枚举
 */
@Getter
@AllArgsConstructor
public enum MilestoneTypeEnum {
    
    REQUIRED("必须", "required"),
    OPTIONAL("可选", "optional");
    
    private final String typeName;
    private final String typeCode;
    
    public static boolean isValidType(String type) {
        for (MilestoneTypeEnum typeEnum : values()) {
            if (typeEnum.getTypeCode().equals(type)) {
                return true;
            }
        }
        return false;
    }
    
    public static MilestoneTypeEnum getByCode(String code) {
        for (MilestoneTypeEnum typeEnum : values()) {
            if (typeEnum.getTypeCode().equals(code)) {
                return typeEnum;
            }
        }
        return null;
    }
}

