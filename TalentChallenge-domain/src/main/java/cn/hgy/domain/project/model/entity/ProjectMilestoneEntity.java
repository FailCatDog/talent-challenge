package cn.hgy.domain.project.model.entity;

import lombok.*;

import java.time.LocalDateTime;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-11-13
 * @Description: 项目里程碑实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMilestoneEntity {
    
    /**
     * 里程碑全局唯一标识
     */
    private String milestoneId;
    
    /**
     * 关联项目ID
     */
    private String projectId;
    
    /**
     * 里程碑编码（自定义，用于标识里程碑类型，建议使用下划线命名，如：proposal_submission、mid_term_defense等）
     */
    private String milestoneCode;
    
    /**
     * 里程碑名称（可自定义展示文案）
     */
    private String name;
    
    /**
     * 里程碑描述
     */
    private String description;
    
    /**
     * 阶段顺序，从1开始
     */
    private Integer sequenceOrder;
    
    /**
     * 阶段状态：planned, in_progress, delivered, under_review, completed, rejected, expired, skipped
     */
    private String status;
    
    /**
     * 阶段进度0-100
     */
    private Integer progressPercent;
    
    /**
     * 成果附件/链接
     */
    private String deliverableUrl;
    
    /**
     * 审批规则配置（JSON格式）
     */
    private String approvalRule;
    
    /**
     * 审批状态：pending, partial, approved, rejected
     */
    private String approvalStatus;
    
    /**
     * 当前负责人（如企业或获奖团队用户ID）
     */
    private String currentOwnerId;
    
    /**
     * 计划开始时间
     */
    private LocalDateTime plannedStartTime;
    
    /**
     * 计划结束时间
     */
    private LocalDateTime plannedEndTime;
    
    /**
     * 实际开始时间
     */
    private LocalDateTime actualStartTime;
    
    /**
     * 实际完成时间
     */
    private LocalDateTime actualEndTime;
    
    /**
     * 自动延期次数
     */
    private Integer autoExtendedCount;
    
    /**
     * 提醒提前小时数
     */
    private Integer remindBeforeHours;
    
    /**
     * 是否跳过该阶段
     */
    private Boolean isSkipped;
    
    /**
     * 跳过原因说明
     */
    private String skipReason;
    
    /**
     * 阶段备注
     */
    private String remark;
    
    /**
     * 乐观锁版本号
     */
    private Long version;
}

