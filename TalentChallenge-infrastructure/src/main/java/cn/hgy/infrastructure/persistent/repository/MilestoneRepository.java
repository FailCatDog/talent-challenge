package cn.hgy.infrastructure.persistent.repository;

import cn.hgy.domain.project.model.entity.ProjectMilestoneEntity;
import cn.hgy.domain.project.repository.IMilestoneRepository;
import cn.hgy.infrastructure.persistent.dao.ProjectMilestoneDao;
import cn.hgy.infrastructure.persistent.po.ProjectMilestone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MilestoneRepository implements IMilestoneRepository {

    private final ProjectMilestoneDao milestoneDao;

    @Override
    public void createMilestone(ProjectMilestoneEntity milestoneEntity) {
        ProjectMilestone milestone = convertToPo(milestoneEntity);
        LocalDateTime now = LocalDateTime.now();
        milestone.setCreateTime(now);
        milestone.setUpdateTime(now);
        milestoneDao.insert(milestone);
    }

    @Override
    public void batchCreateMilestones(List<ProjectMilestoneEntity> milestoneEntities) {
        if (CollectionUtils.isEmpty(milestoneEntities)) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        List<ProjectMilestone> list = milestoneEntities.stream()
                .map(this::convertToPo)
                .collect(Collectors.toList());
        list.forEach(m -> {
            m.setCreateTime(now);
            m.setUpdateTime(now);
        });
        milestoneDao.batchInsert(list);
    }

    @Override
    public void updateMilestone(ProjectMilestoneEntity milestoneEntity) {
        ProjectMilestone milestone = convertToPo(milestoneEntity);
        milestone.setUpdateTime(LocalDateTime.now());
        milestoneDao.update(milestone);
    }

    @Override
    public ProjectMilestoneEntity queryMilestoneById(String milestoneId) {
        ProjectMilestone milestone = milestoneDao.selectByMilestoneId(milestoneId);
        return convertToEntity(milestone);
    }

    @Override
    public List<ProjectMilestoneEntity> queryMilestonesByProjectId(String projectId) {
        List<ProjectMilestone> milestones = milestoneDao.selectByProjectId(projectId);
        return convertToEntities(milestones);
    }

    @Override
    public ProjectMilestoneEntity queryMilestoneByProjectIdAndCode(String projectId, String milestoneCode) {
        ProjectMilestone milestone = milestoneDao.selectByProjectIdAndCode(projectId, milestoneCode);
        return convertToEntity(milestone);
    }

    @Override
    public List<ProjectMilestoneEntity> queryMilestonesByProjectIdAndStatus(String projectId, String status) {
        List<ProjectMilestone> milestones = milestoneDao.selectByProjectIdAndStatus(projectId, status);
        return convertToEntities(milestones);
    }

    @Override
    public void updateMilestoneStatus(String milestoneId, String status) {
        milestoneDao.updateStatus(milestoneId, status);
    }

    @Override
    public void updateMilestoneApprovalStatus(String milestoneId, String approvalStatus) {
        milestoneDao.updateApprovalStatus(milestoneId, approvalStatus);
    }

    @Override
    public void updateMilestoneDeadline(String milestoneId, LocalDateTime plannedStartTime, LocalDateTime plannedEndTime, Integer remindBeforeHours) {
        milestoneDao.updateDeadline(milestoneId, plannedStartTime, plannedEndTime, remindBeforeHours);
    }

    @Override
    public boolean existsMilestonesByProjectId(String projectId) {
        return milestoneDao.countByProjectId(projectId) > 0;
    }

    private ProjectMilestone convertToPo(ProjectMilestoneEntity entity) {
        if (entity == null) {
            return null;
        }
        ProjectMilestone milestone = new ProjectMilestone();
        milestone.setMilestoneId(entity.getMilestoneId());
        milestone.setProjectId(entity.getProjectId());
        milestone.setMilestoneCode(entity.getMilestoneCode());
        milestone.setName(entity.getName());
        milestone.setDescription(entity.getDescription());
        milestone.setSequenceOrder(entity.getSequenceOrder());
        milestone.setStatus(entity.getStatus());
        milestone.setProgressPercent(entity.getProgressPercent());
        milestone.setDeliverableUrl(entity.getDeliverableUrl());
        milestone.setDeliverableFiles(entity.getDeliverableFiles());
        milestone.setReferenceFiles(entity.getReferenceFiles());
        milestone.setApprovalRule(entity.getApprovalRule());
        milestone.setApprovalStatus(entity.getApprovalStatus());
        milestone.setCurrentOwnerId(entity.getCurrentOwnerId());
        milestone.setPlannedStartTime(entity.getPlannedStartTime());
        milestone.setPlannedEndTime(entity.getPlannedEndTime());
        milestone.setActualStartTime(entity.getActualStartTime());
        milestone.setActualEndTime(entity.getActualEndTime());
        milestone.setAutoExtendedCount(entity.getAutoExtendedCount());
        milestone.setRemindBeforeHours(entity.getRemindBeforeHours());
        milestone.setIsSkipped(entity.getIsSkipped());
        milestone.setSkipReason(entity.getSkipReason());
        milestone.setRemark(entity.getRemark());
        milestone.setVersion(entity.getVersion());
        return milestone;
    }

    private ProjectMilestoneEntity convertToEntity(ProjectMilestone milestone) {
        if (milestone == null) {
            return null;
        }
        return ProjectMilestoneEntity.builder()
                .milestoneId(milestone.getMilestoneId())
                .projectId(milestone.getProjectId())
                .milestoneCode(milestone.getMilestoneCode())
                .name(milestone.getName())
                .description(milestone.getDescription())
                .sequenceOrder(milestone.getSequenceOrder())
                .status(milestone.getStatus())
                .progressPercent(milestone.getProgressPercent())
                .deliverableUrl(milestone.getDeliverableUrl())
                .approvalRule(milestone.getApprovalRule())
                .approvalStatus(milestone.getApprovalStatus())
                .currentOwnerId(milestone.getCurrentOwnerId())
                .plannedStartTime(milestone.getPlannedStartTime())
                .plannedEndTime(milestone.getPlannedEndTime())
                .actualStartTime(milestone.getActualStartTime())
                .actualEndTime(milestone.getActualEndTime())
                .autoExtendedCount(milestone.getAutoExtendedCount())
                .remindBeforeHours(milestone.getRemindBeforeHours())
                .isSkipped(milestone.getIsSkipped())
                .skipReason(milestone.getSkipReason())
                .remark(milestone.getRemark())
                .version(milestone.getVersion())
                .build();
    }

    private List<ProjectMilestoneEntity> convertToEntities(List<ProjectMilestone> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
    }
}

