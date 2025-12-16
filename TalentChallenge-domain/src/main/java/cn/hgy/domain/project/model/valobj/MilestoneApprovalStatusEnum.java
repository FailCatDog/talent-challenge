package cn.hgy.domain.project.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-11-13
 * @Description: 里程碑审批状态枚举
 */
@Getter
@AllArgsConstructor
public enum MilestoneApprovalStatusEnum {
    
    PENDING("待审批", "pending"),
    PARTIAL("部分审批", "partial"),
    APPROVED("已审批", "approved"),
    REJECTED("已拒绝", "rejected");
    
    private final String statusName;
    private final String statusCode;
    
    public static boolean isValidStatus(String status) {
        for (MilestoneApprovalStatusEnum statusEnum : values()) {
            if (statusEnum.getStatusCode().equals(status)) {
                return true;
            }
        }
        return false;
    }
    
    public static MilestoneApprovalStatusEnum getByCode(String code) {
        for (MilestoneApprovalStatusEnum statusEnum : values()) {
            if (statusEnum.getStatusCode().equals(code)) {
                return statusEnum;
            }
        }
        return null;
    }
}

