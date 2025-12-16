package cn.hgy.infrastructure.persistent.repository;

import cn.hgy.domain.project.model.entity.MilestoneTemplateEntity;
import cn.hgy.domain.project.repository.IMilestoneTemplateRepository;
import cn.hgy.infrastructure.persistent.dao.MilestoneTemplateDao;
import cn.hgy.infrastructure.persistent.po.MilestoneTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-11-25
 * @Description: 里程碑模板仓储实现
 */
@Repository
@RequiredArgsConstructor
public class MilestoneTemplateRepository implements IMilestoneTemplateRepository {
    
    private final MilestoneTemplateDao milestoneTemplateDao;
    
    @Override
    public void createTemplate(MilestoneTemplateEntity templateEntity) {
        MilestoneTemplate po = convertToPO(templateEntity);
        milestoneTemplateDao.insert(po);
    }
    
    @Override
    public Optional<MilestoneTemplateEntity> queryTemplateById(String templateId) {
        MilestoneTemplate po = milestoneTemplateDao.selectByTemplateId(templateId);
        return po != null ? Optional.of(convertToEntity(po)) : Optional.empty();
    }
    
    @Override
    public Optional<MilestoneTemplateEntity> queryTemplateByCode(String milestoneCode) {
        MilestoneTemplate po = milestoneTemplateDao.selectByMilestoneCode(milestoneCode);
        return po != null ? Optional.of(convertToEntity(po)) : Optional.empty();
    }
    
    @Override
    public List<MilestoneTemplateEntity> queryAllEnabledTemplates() {
        List<MilestoneTemplate> pos = milestoneTemplateDao.selectAllEnabled();
        return pos.stream().map(this::convertToEntity).collect(Collectors.toList());
    }
    
    @Override
    public List<MilestoneTemplateEntity> queryTemplatesByType(String milestoneType) {
        List<MilestoneTemplate> pos = milestoneTemplateDao.selectByType(milestoneType);
        return pos.stream().map(this::convertToEntity).collect(Collectors.toList());
    }
    
    @Override
    public void batchCreateTemplates(List<MilestoneTemplateEntity> templateEntities) {
        List<MilestoneTemplate> pos = templateEntities.stream()
                .map(this::convertToPO)
                .collect(Collectors.toList());
        milestoneTemplateDao.batchInsert(pos);
    }
    
    private MilestoneTemplate convertToPO(MilestoneTemplateEntity entity) {
        MilestoneTemplate po = new MilestoneTemplate();
        po.setTemplateId(entity.getTemplateId());
        po.setMilestoneCode(entity.getMilestoneCode());
        po.setName(entity.getName());
        po.setDescription(entity.getDescription());
        po.setMilestoneType(entity.getMilestoneType());
        po.setDefaultSequenceOrder(entity.getDefaultSequenceOrder());
        po.setDefaultApprovalRule(entity.getDefaultApprovalRule());
        po.setDefaultRemindBeforeHours(entity.getDefaultRemindBeforeHours());
        po.setIsEnabled(entity.getIsEnabled());
        return po;
    }
    
    private MilestoneTemplateEntity convertToEntity(MilestoneTemplate po) {
        return MilestoneTemplateEntity.builder()
                .templateId(po.getTemplateId())
                .milestoneCode(po.getMilestoneCode())
                .name(po.getName())
                .description(po.getDescription())
                .milestoneType(po.getMilestoneType())
                .defaultSequenceOrder(po.getDefaultSequenceOrder())
                .defaultApprovalRule(po.getDefaultApprovalRule())
                .defaultRemindBeforeHours(po.getDefaultRemindBeforeHours())
                .isEnabled(po.getIsEnabled())
                .build();
    }
}

