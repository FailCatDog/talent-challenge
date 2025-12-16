package cn.hgy.domain.project.repository;

import cn.hgy.domain.project.model.entity.MilestoneTemplateEntity;

import java.util.List;
import java.util.Optional;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-11-25
 * @Description: 里程碑模板仓储接口
 */
public interface IMilestoneTemplateRepository {
    
    /**
     * 创建模板
     * @param templateEntity 模板实体
     */
    void createTemplate(MilestoneTemplateEntity templateEntity);
    
    /**
     * 根据模板ID查询模板
     * @param templateId 模板ID
     * @return 模板实体
     */
    Optional<MilestoneTemplateEntity> queryTemplateById(String templateId);
    
    /**
     * 根据里程碑编码查询模板
     * @param milestoneCode 里程碑编码
     * @return 模板实体
     */
    Optional<MilestoneTemplateEntity> queryTemplateByCode(String milestoneCode);
    
    /**
     * 查询所有启用的模板
     * @return 模板列表
     */
    List<MilestoneTemplateEntity> queryAllEnabledTemplates();
    
    /**
     * 根据类型查询启用的模板
     * @param milestoneType 里程碑类型：required(必须), optional(可选)
     * @return 模板列表
     */
    List<MilestoneTemplateEntity> queryTemplatesByType(String milestoneType);
    
    /**
     * 批量创建模板
     * @param templateEntities 模板列表
     */
    void batchCreateTemplates(List<MilestoneTemplateEntity> templateEntities);
}

