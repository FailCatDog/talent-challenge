package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-11-25
 * @Description: 里程碑模板响应DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MilestoneTemplateResponseDTO {
    
    private String templateId;
    private String milestoneCode;
    private String name;
    private String description;
    private String milestoneType;
    private Integer defaultSequenceOrder;
    private String defaultApprovalRule;
    private Integer defaultRemindBeforeHours;
    private Boolean isEnabled;
}

