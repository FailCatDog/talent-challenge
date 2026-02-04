package cn.hgy.domain.project.service;

import cn.hgy.domain.project.model.entity.ProjectMilestoneEntity;

import java.util.List;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-11-13
 * @Description: 里程碑服务接口
 */
public interface IMilestoneService {
    
    /**
     * 为项目初始化里程碑（创建6个固定里程碑）
     * @param projectId 项目ID
     * @return 是否成功
     */
    boolean initializeMilestonesForProject(String projectId);
    
    /**
     * 创建单个里程碑
     * @param milestoneEntity 里程碑实体
     * @return 是否成功
     */
    boolean createMilestone(ProjectMilestoneEntity milestoneEntity);
    
    /**
     * 更新里程碑
     * @param milestoneEntity 里程碑实体
     * @return 是否成功
     */
    boolean updateMilestone(ProjectMilestoneEntity milestoneEntity);
    
    /**
     * 根据里程碑ID查询里程碑
     * @param milestoneId 里程碑ID
     * @return 里程碑实体
     */
    ProjectMilestoneEntity getMilestoneById(String milestoneId);
    
    /**
     * 根据项目ID查询所有里程碑
     * @param projectId 项目ID
     * @return 里程碑列表
     */
    List<ProjectMilestoneEntity> getMilestonesByProjectId(String projectId);
    
    /**
     * 根据项目ID和里程碑编码查询里程碑
     * @param projectId 项目ID
     * @param milestoneCode 里程碑编码
     * @return 里程碑实体
     */
    ProjectMilestoneEntity getMilestoneByProjectIdAndCode(String projectId, String milestoneCode);
    
    /**
     * 更新里程碑状态
     * @param milestoneId 里程碑ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateMilestoneStatus(String milestoneId, String status);
    
    /**
     * 更新里程碑审批状态
     * @param milestoneId 里程碑ID
     * @param approvalStatus 审批状态
     * @return 是否成功
     */
    boolean updateMilestoneApprovalStatus(String milestoneId, String approvalStatus);
    
    /**
     * 更新里程碑期限（计划开始/结束时间、提醒提前小时数）
     * @param milestoneId 里程碑ID
     * @param plannedStartTime 计划开始时间
     * @param plannedEndTime 计划结束时间
     * @param remindBeforeHours 提前提醒小时数
     * @return 是否成功
     */
    boolean updateMilestoneDeadline(String milestoneId,
                                    java.time.LocalDateTime plannedStartTime,
                                    java.time.LocalDateTime plannedEndTime,
                                    Integer remindBeforeHours);
    
    /**
     * 提交里程碑成果
     * @param milestoneId 里程碑ID
     * @param deliverableUrl 成果附件URL
     * @return 是否成功
     */
    boolean submitMilestoneDeliverable(String milestoneId, String deliverableUrl);
    
    /**
     * 审批里程碑
     * @param milestoneId 里程碑ID
     * @param operatorId 操作人ID
     * @param approved 是否通过
     * @param remark 审批意见
     * @return 是否成功
     */
    boolean approveMilestone(String milestoneId, String operatorId, boolean approved, String remark);
}

