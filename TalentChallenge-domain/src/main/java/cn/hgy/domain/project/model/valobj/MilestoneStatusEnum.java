package cn.hgy.domain.project.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-11-13
 * @Description: 里程碑状态枚举
 */
@Getter
@AllArgsConstructor
public enum MilestoneStatusEnum {
    
    PLANNED("计划中", "planned"),
    IN_PROGRESS("进行中", "in_progress"),
    DELIVERED("已交付", "delivered"),
    UNDER_REVIEW("评审中", "under_review"),
    COMPLETED("已完成", "completed"),
    REJECTED("已拒绝", "rejected"),
    EXPIRED("已过期", "expired"),
    SKIPPED("已跳过", "skipped");
    
    private final String statusName;
    private final String statusCode;
    
    public static boolean isValidStatus(String status) {
        for (MilestoneStatusEnum statusEnum : values()) {
            if (statusEnum.getStatusCode().equals(status)) {
                return true;
            }
        }
        return false;
    }
    
    public static MilestoneStatusEnum getByCode(String code) {
        for (MilestoneStatusEnum statusEnum : values()) {
            if (statusEnum.getStatusCode().equals(code)) {
                return statusEnum;
            }
        }
        return null;
    }
}

