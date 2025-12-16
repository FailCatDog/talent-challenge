package cn.hgy.domain.project.repository;

import cn.hgy.domain.project.model.entity.ProjectMilestoneEntity;

import java.util.List;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-11-13
 * @Description: 里程碑仓储接口
 */
public interface IMilestoneRepository {
    
    /**
     * 创建里程碑
     * @param milestoneEntity 里程碑实体
     */
    void createMilestone(ProjectMilestoneEntity milestoneEntity);
    
    /**
     * 批量创建里程碑
     * @param milestoneEntities 里程碑实体列表
     */
    void batchCreateMilestones(List<ProjectMilestoneEntity> milestoneEntities);
    
    /**
     * 更新里程碑
     * @param milestoneEntity 里程碑实体
     */
    void updateMilestone(ProjectMilestoneEntity milestoneEntity);
    
    /**
     * 根据里程碑ID查询里程碑
     * @param milestoneId 里程碑ID
     * @return 里程碑实体
     */
    ProjectMilestoneEntity queryMilestoneById(String milestoneId);
    
    /**
     * 根据项目ID查询所有里程碑
     * @param projectId 项目ID
     * @return 里程碑列表
     */
    List<ProjectMilestoneEntity> queryMilestonesByProjectId(String projectId);
    
    /**
     * 根据项目ID和里程碑编码查询里程碑
     * @param projectId 项目ID
     * @param milestoneCode 里程碑编码
     * @return 里程碑实体
     */
    ProjectMilestoneEntity queryMilestoneByProjectIdAndCode(String projectId, String milestoneCode);
    
    /**
     * 根据项目ID和状态查询里程碑列表
     * @param projectId 项目ID
     * @param status 状态
     * @return 里程碑列表
     */
    List<ProjectMilestoneEntity> queryMilestonesByProjectIdAndStatus(String projectId, String status);
    
    /**
     * 更新里程碑状态
     * @param milestoneId 里程碑ID
     * @param status 状态
     */
    void updateMilestoneStatus(String milestoneId, String status);
    
    /**
     * 更新里程碑审批状态
     * @param milestoneId 里程碑ID
     * @param approvalStatus 审批状态
     */
    void updateMilestoneApprovalStatus(String milestoneId, String approvalStatus);

    /**
     * 更新里程碑期限（计划开始/计划结束/提醒提前小时数）
     * @param milestoneId 里程碑ID
     * @param plannedStartTime 计划开始时间
     * @param plannedEndTime 计划结束时间
     * @param remindBeforeHours 提前提醒小时数
     */
    void updateMilestoneDeadline(String milestoneId,
                                 java.time.LocalDateTime plannedStartTime,
                                 java.time.LocalDateTime plannedEndTime,
                                 Integer remindBeforeHours);
    
    /**
     * 检查项目是否已存在里程碑
     * @param projectId 项目ID
     * @return 是否存在
     */
    boolean existsMilestonesByProjectId(String projectId);
}

