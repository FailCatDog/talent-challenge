package cn.hgy.domain.project.model.entity;

import lombok.*;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-11-25
 * @Description: 里程碑模板实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MilestoneTemplateEntity {
    
    /**
     * 模板唯一标识
     */
    private String templateId;
    
    /**
     * 里程碑编码
     */
    private String milestoneCode;
    
    /**
     * 里程碑名称
     */
    private String name;
    
    /**
     * 里程碑描述
     */
    private String description;
    
    /**
     * 里程碑类型：required(必须), optional(可选)
     */
    private String milestoneType;
    
    /**
     * 默认顺序
     */
    private Integer defaultSequenceOrder;
    
    /**
     * 默认审批规则配置（JSON格式）
     */
    private String defaultApprovalRule;
    
    /**
     * 默认提醒提前小时数
     */
    private Integer defaultRemindBeforeHours;
    
    /**
     * 是否启用
     */
    private Boolean isEnabled;
}

