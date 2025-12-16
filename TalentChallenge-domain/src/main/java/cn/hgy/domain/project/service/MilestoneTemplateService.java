package cn.hgy.domain.project.service;

import cn.hgy.domain.project.model.entity.MilestoneTemplateEntity;
import cn.hgy.domain.project.model.entity.ProjectMilestoneEntity;
import cn.hgy.domain.project.model.valobj.MilestoneApprovalStatusEnum;
import cn.hgy.domain.project.model.valobj.MilestoneStatusEnum;
import cn.hgy.domain.project.model.valobj.MilestoneTypeEnum;
import cn.hgy.domain.project.repository.IMilestoneRepository;
import cn.hgy.domain.project.repository.IMilestoneTemplateRepository;
import cn.hgy.types.enums.ResponseCode;
import cn.hgy.types.exception.AppException;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-11-25
 * @Description: 里程碑模板服务实现
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MilestoneTemplateService implements IMilestoneTemplateService {
    
    private final IMilestoneTemplateRepository templateRepository;
    private final IMilestoneRepository milestoneRepository;
    private final IMilestoneService milestoneService;
    private final Snowflake snowflake;
    
    @Override
    public boolean initializeDefaultTemplates() {
        log.info("开始初始化默认里程碑模板");
        
        // 检查是否已有模板
        List<MilestoneTemplateEntity> existing = templateRepository.queryAllEnabledTemplates();
        if (!existing.isEmpty()) {
            log.info("里程碑模板已存在，跳过初始化");
            return true;
        }
        
        List<MilestoneTemplateEntity> templates = new ArrayList<>();
        
        // 必须里程碑
        templates.add(createTemplate("proposal_submission", "方案提交", 
                "企业/团队提交完整方案、附录与预计交付时间", 
                MilestoneTypeEnum.REQUIRED.getTypeCode(), 1,
                "{\"approverRoles\":[\"enterprise\"],\"autoApprove\":true}", 72));
        
        templates.add(createTemplate("deliverable_submission", "成果提交",
                "上传交付成果包并同步进度与实际时间",
                MilestoneTypeEnum.REQUIRED.getTypeCode(), 3,
                "{\"approverRoles\":[\"enterprise\",\"team\"],\"autoApprove\":false}", 48));
        
        templates.add(createTemplate("project_settlement", "项目结算",
                "产出结算凭证并推进财务付款流程",
                MilestoneTypeEnum.REQUIRED.getTypeCode(), 5,
                "{\"approverRoles\":[\"finance\"],\"requiresReceipt\":true}", 24));
        
        // 可选里程碑
        templates.add(createTemplate("mid_term_defense", "中期答辩",
                "安排中期答辩、准备材料并确认实际时间",
                MilestoneTypeEnum.OPTIONAL.getTypeCode(), 2,
                "{\"approverRoles\":[\"academic\"],\"reviewTy pe\":\"defense\"}", 48));
        
        templates.add(createTemplate("result_review", "成果评审",
                "专家组评审成果并填写审批意见",
                MilestoneTypeEnum.OPTIONAL.getTypeCode(), 4,
                "{\"approverRoles\":[\"review_committee\"],\"auditLevel\":\"committee\"}", 24));
        
        templates.add(createTemplate("closure_announcement", "结项公示",
                "发布结项公示、归档成果与通知相关角色",
                MilestoneTypeEnum.OPTIONAL.getTypeCode(), 6,
                "{\"approverRoles\":[\"platform\"],\"notifyChannels\":[\"homepage\"]}", 24));
        
        try {
            templateRepository.batchCreateTemplates(templates);
            log.info("默认里程碑模板初始化成功，共创建{}个模板", templates.size());
            return true;
        } catch (Exception e) {
            log.error("初始化默认里程碑模板失败", e);
            throw new AppException(ResponseCode.SYSTEM_ERROR.getCode(), "初始化默认里程碑模板失败", e);
        }
    }
    
    private MilestoneTemplateEntity createTemplate(String code, String name, String description,
                                                   String type, int order, String approvalRule, int remindHours) {
        return MilestoneTemplateEntity.builder()
                .templateId(snowflake.nextIdStr())
                .milestoneCode(code)
                .name(name)
                .description(description)
                .milestoneType(type)
                .defaultSequenceOrder(order)
                .defaultApprovalRule(approvalRule)
                .defaultRemindBeforeHours(remindHours)
                .isEnabled(true)
                .build();
    }
    
    @Override
    public ProjectMilestoneEntity createMilestoneFromTemplate(String projectId, String templateId) {
        MilestoneTemplateEntity template = templateRepository.queryTemplateById(templateId)
                .orElseThrow(() -> new AppException(ResponseCode.MILESTONE_TEMPLATE_NOT_FOUND.getCode(), 
                        ResponseCode.MILESTONE_TEMPLATE_NOT_FOUND.getInfo()));
        return createMilestoneFromTemplateEntity(projectId, template);
    }
    
    @Override
    public ProjectMilestoneEntity createMilestoneFromTemplateCode(String projectId, String milestoneCode) {
        MilestoneTemplateEntity template = templateRepository.queryTemplateByCode(milestoneCode)
                .orElseThrow(() -> new AppException(ResponseCode.MILESTONE_TEMPLATE_NOT_FOUND.getCode(), 
                        ResponseCode.MILESTONE_TEMPLATE_NOT_FOUND.getInfo()));
        return createMilestoneFromTemplateEntity(projectId, template);
    }
    
    private ProjectMilestoneEntity createMilestoneFromTemplateEntity(String projectId, MilestoneTemplateEntity template) {
        // 检查是否已存在
        if (milestoneRepository.queryMilestoneByProjectIdAndCode(projectId, template.getMilestoneCode()) != null) {
            throw new AppException(ResponseCode.MILESTONE_ALREADY_EXISTS.getCode(), 
                    ResponseCode.MILESTONE_ALREADY_EXISTS.getInfo());
        }
        
        // 计算顺序
        List<ProjectMilestoneEntity> existed = milestoneRepository.queryMilestonesByProjectId(projectId);
        int sequenceOrder = template.getDefaultSequenceOrder();
        if (existed != null && !existed.isEmpty()) {
            // 如果指定顺序已存在，则插入到该位置，后续里程碑顺序+1
            boolean needAdjust = existed.stream()
                    .anyMatch(m -> m.getSequenceOrder() != null && m.getSequenceOrder() >= sequenceOrder);
            if (needAdjust) {
                // 调整后续里程碑顺序
                existed.stream()
                        .filter(m -> m.getSequenceOrder() != null && m.getSequenceOrder() >= sequenceOrder)
                        .forEach(m -> {
                            m.setSequenceOrder(m.getSequenceOrder() + 1);
                            milestoneRepository.updateMilestone(m);
                        });
            }
        }
        
        ProjectMilestoneEntity milestone = ProjectMilestoneEntity.builder()
                .milestoneId(snowflake.nextIdStr())
                .projectId(projectId)
                .milestoneCode(template.getMilestoneCode())
                .name(template.getName())
                .description(template.getDescription())
                .sequenceOrder(sequenceOrder)
                .status(MilestoneStatusEnum.PLANNED.getStatusCode())
                .progressPercent(0)
                .approvalRule(StrUtil.isBlank(template.getDefaultApprovalRule()) ? "{}" : template.getDefaultApprovalRule())
                .approvalStatus(MilestoneApprovalStatusEnum.PENDING.getStatusCode())
                .remindBeforeHours(template.getDefaultRemindBeforeHours() != null ? template.getDefaultRemindBeforeHours() : 24)
                .autoExtendedCount(0)
                .isSkipped(false)
                .version(0L)
                .build();
        
        milestoneService.createMilestone(milestone);
        return milestone;
    }
    
    @Override
    public List<ProjectMilestoneEntity> batchCreateMilestonesFromTemplates(String projectId, List<String> templateIds) {
        List<ProjectMilestoneEntity> milestones = new ArrayList<>();
        for (String templateId : templateIds) {
            try {
                ProjectMilestoneEntity milestone = createMilestoneFromTemplate(projectId, templateId);
                milestones.add(milestone);
            } catch (Exception e) {
                log.warn("从模板创建里程碑失败 templateId={}, projectId={}", templateId, projectId, e);
            }
        }
        return milestones;
    }
    
    @Override
    public boolean initializeRequiredMilestones(String projectId) {
        if (StrUtil.isBlank(projectId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        
        log.info("为项目初始化必须里程碑 projectId={}", projectId);
        
        // 检查是否已存在里程碑
        if (milestoneRepository.existsMilestonesByProjectId(projectId)) {
            log.info("项目里程碑已存在，跳过初始化 projectId={}", projectId);
            return true;
        }
        
        // 查询所有必须里程碑模板
        List<MilestoneTemplateEntity> requiredTemplates = templateRepository.queryTemplatesByType(
                MilestoneTypeEnum.REQUIRED.getTypeCode());
        
        if (requiredTemplates.isEmpty()) {
            log.warn("未找到必须里程碑模板，请先初始化默认模板 projectId={}", projectId);
            return false;
        }
        
        // 批量创建必须里程碑
        for (MilestoneTemplateEntity template : requiredTemplates) {
            try {
                createMilestoneFromTemplateEntity(projectId, template);
                log.info("创建必须里程碑成功 projectId={}, milestoneCode={}", projectId, template.getMilestoneCode());
            } catch (Exception e) {
                log.error("创建必须里程碑失败 projectId={}, milestoneCode={}", projectId, template.getMilestoneCode(), e);
            }
        }
        
        log.info("项目必须里程碑初始化完成 projectId={}", projectId);
        return true;
    }
    
    @Override
    public List<MilestoneTemplateEntity> getAllAvailableTemplates() {
        return templateRepository.queryAllEnabledTemplates();
    }
    
    @Override
    public List<MilestoneTemplateEntity> getOptionalTemplates() {
        return templateRepository.queryTemplatesByType(MilestoneTypeEnum.OPTIONAL.getTypeCode());
    }
    
    @Override
    public MilestoneTemplateEntity getTemplateByCode(String milestoneCode) {
        return templateRepository.queryTemplateByCode(milestoneCode)
                .orElseThrow(() -> new AppException(ResponseCode.MILESTONE_TEMPLATE_NOT_FOUND.getCode(),
                        ResponseCode.MILESTONE_TEMPLATE_NOT_FOUND.getInfo()));
    }
}

