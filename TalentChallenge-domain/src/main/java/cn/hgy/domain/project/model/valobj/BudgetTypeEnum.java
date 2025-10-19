package cn.hgy.domain.project.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BudgetTypeEnum {
    
    FIXED("固定总价", "fixed"),
    HOURLY("按时计费", "hourly"),
    NEGOTIABLE("面议", "negotiable");
    
    private final String typeName;
    private final String typeCode;
    
    public static boolean isValidType(String type) {
        for (BudgetTypeEnum typeEnum : values()) {
            if (typeEnum.getTypeCode().equals(type) || typeEnum.getTypeName().equals(type)) {
                return true;
            }
        }
        return false;
    }
    
    public static BudgetTypeEnum getByCode(String code) {
        for (BudgetTypeEnum typeEnum : values()) {
            if (typeEnum.getTypeCode().equals(code)) {
                return typeEnum;
            }
        }
        return null;
    }
}
