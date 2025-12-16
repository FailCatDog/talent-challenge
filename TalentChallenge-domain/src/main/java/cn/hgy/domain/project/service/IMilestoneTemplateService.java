package cn.hgy.domain.project.service;

import cn.hgy.domain.project.model.entity.MilestoneTemplateEntity;
import cn.hgy.domain.project.model.entity.ProjectMilestoneEntity;

import java.util.List;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-11-25
 * @Description: 里程碑模板服务接口
 */
public interface IMilestoneTemplateService {
    
    /**
     * 初始化默认模板（系统启动时调用）
     * @return 是否成功
     */
    boolean initializeDefaultTemplates();
    
    /**
     * 根据模板创建里程碑实例
     * @param projectId 项目ID
     * @param templateId 模板ID
     * @return 创建的里程碑实体
     */
    ProjectMilestoneEntity createMilestoneFromTemplate(String projectId, String templateId);
    
    /**
     * 根据模板编码创建里程碑实例
     * @param projectId 项目ID
     * @param milestoneCode 里程碑编码
     * @return 创建的里程碑实体
     */
    ProjectMilestoneEntity createMilestoneFromTemplateCode(String projectId, String milestoneCode);
    
    /**
     * 批量从模板创建里程碑
     * @param projectId 项目ID
     * @param templateIds 模板ID列表
     * @return 创建的里程碑列表
     */
    List<ProjectMilestoneEntity> batchCreateMilestonesFromTemplates(String projectId, List<String> templateIds);
    
    /**
     * 为项目初始化必须里程碑
     * @param projectId 项目ID
     * @return 是否成功
     */
    boolean initializeRequiredMilestones(String projectId);
    
    /**
     * 查询所有可用的模板
     * @return 模板列表
     */
    List<MilestoneTemplateEntity> getAllAvailableTemplates();
    
    /**
     * 查询可选里程碑模板
     * @return 可选模板列表
     */
    List<MilestoneTemplateEntity> getOptionalTemplates();
    
    /**
     * 根据编码查询模板
     * @param milestoneCode 里程碑编码
     * @return 模板实体
     */
    MilestoneTemplateEntity getTemplateByCode(String milestoneCode);
}

