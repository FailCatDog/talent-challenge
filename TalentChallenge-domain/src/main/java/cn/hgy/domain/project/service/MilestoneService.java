package cn.hgy.domain.project.service;

import cn.hgy.domain.project.model.entity.ProjectMilestoneEntity;
import cn.hgy.domain.project.model.valobj.MilestoneApprovalStatusEnum;
import cn.hgy.domain.project.model.valobj.MilestoneStatusEnum;
import cn.hgy.domain.project.model.valobj.ProjectStatusEnum;
import cn.hgy.domain.project.repository.IMilestoneRepository;
import cn.hgy.domain.project.repository.IProjectRepository;
import cn.hgy.types.enums.ResponseCode;
import cn.hgy.types.exception.AppException;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-11-13
 * @Description: 里程碑服务实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MilestoneService implements IMilestoneService {
    

    private final IMilestoneRepository milestoneRepository;
    private final IProjectRepository projectRepository;
    private final Snowflake snowflake;
    
    @Override
    public boolean initializeMilestonesForProject(String projectId) {
        if (StrUtil.isBlank(projectId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        log.info("初始化项目里程碑 projectId={}", projectId);
        if (milestoneRepository.existsMilestonesByProjectId(projectId)) {
            log.info("项目里程碑已存在 projectId={}", projectId);
            return true;
        }

        log.info("项目里程碑初始化完成 projectId={}", projectId);
        return true;
    }
    
    @Override
    public boolean createMilestone(ProjectMilestoneEntity milestoneEntity) {
        String projectId = milestoneEntity == null ? null : milestoneEntity.getProjectId();
        String milestoneCode = milestoneEntity == null ? null : milestoneEntity.getMilestoneCode();
        boolean missingRequired = milestoneEntity == null || StrUtil.isBlank(projectId) || StrUtil.isBlank(milestoneCode);
        if (missingRequired) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        // 灵活节点设计：移除对固定枚举的校验，允许自定义里程碑编码
        // 仅校验编码格式：不能为空，建议使用下划线命名（如：proposal_submission）
        if (milestoneCode.length() > 50) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "里程碑编码长度不能超过50个字符");
        }

        // 检查同一项目下是否已存在相同编码的里程碑（可选，根据业务需求决定是否允许重复）
        // 如果业务允许同一项目有多个相同编码的里程碑，可以注释掉此检查
        if (milestoneRepository.queryMilestoneByProjectIdAndCode(projectId, milestoneCode) != null) {
            throw new AppException(ResponseCode.MILESTONE_ALREADY_EXISTS.getCode(), ResponseCode.MILESTONE_ALREADY_EXISTS.getInfo());
        }

        if (StrUtil.isBlank(milestoneEntity.getMilestoneId())) {
            milestoneEntity.setMilestoneId(snowflake.nextIdStr());
        }

        // 灵活节点设计：名称必须由调用方提供，不再从枚举获取默认值
        if (StrUtil.isBlank(milestoneEntity.getName())) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "里程碑名称不能为空");
        }

        // 灵活节点设计：描述、审批规则、提醒时间等由调用方提供，不再使用默认值
        // 如果调用方未提供，使用空值或默认值
        if (StrUtil.isBlank(milestoneEntity.getDescription())) {
            milestoneEntity.setDescription("");
        }

        if (milestoneEntity.getSequenceOrder() == null || milestoneEntity.getSequenceOrder() <= 0) {
            List<ProjectMilestoneEntity> existed = milestoneRepository.queryMilestonesByProjectId(projectId);
            milestoneEntity.setSequenceOrder(existed == null ? 1 : existed.size() + 1);
        }

        if (StrUtil.isBlank(milestoneEntity.getStatus())) {
            milestoneEntity.setStatus(MilestoneStatusEnum.PLANNED.getStatusCode());
        }

        if (milestoneEntity.getProgressPercent() == null) {
            milestoneEntity.setProgressPercent(0);
        }

        if (StrUtil.isBlank(milestoneEntity.getApprovalStatus())) {
            milestoneEntity.setApprovalStatus(MilestoneApprovalStatusEnum.PENDING.getStatusCode());
        }

        // 灵活节点设计：审批规则由调用方提供，如果未提供则使用空JSON
        if (StrUtil.isBlank(milestoneEntity.getApprovalRule())) {
            milestoneEntity.setApprovalRule("{}");
        }

        // 灵活节点设计：提醒时间由调用方提供，如果未提供则使用默认值24小时
        if (milestoneEntity.getRemindBeforeHours() == null) {
            milestoneEntity.setRemindBeforeHours(24);
        }

        if (milestoneEntity.getAutoExtendedCount() == null) {
            milestoneEntity.setAutoExtendedCount(0);
        }

        if (milestoneEntity.getIsSkipped() == null) {
            milestoneEntity.setIsSkipped(false);
        }

        if (milestoneEntity.getVersion() == null) {
            milestoneEntity.setVersion(0L);
        }

        log.info("创建里程碑 milestoneId={}, projectId={}, milestoneCode={}", milestoneEntity.getMilestoneId(), projectId, milestoneCode);
        try {
            milestoneRepository.createMilestone(milestoneEntity);
        } catch (Exception e) {
            log.error("创建里程碑失败 milestoneId={}, projectId={}, milestoneCode={}", milestoneEntity.getMilestoneId(), projectId, milestoneCode, e);
            throw new AppException(ResponseCode.MILESTONE_CREATE_FAIL.getCode(), ResponseCode.MILESTONE_CREATE_FAIL.getInfo(), e);
        }
        return true;
    }
    
    @Override
    public boolean updateMilestone(ProjectMilestoneEntity milestoneEntity) {
        if (milestoneEntity == null || StrUtil.isBlank(milestoneEntity.getMilestoneId())) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        
        ProjectMilestoneEntity existed = milestoneRepository.queryMilestoneById(milestoneEntity.getMilestoneId());
        if (existed == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "里程碑不存在");
        }
        
        log.info("更新里程碑 milestoneId={}", milestoneEntity.getMilestoneId());
        milestoneRepository.updateMilestone(milestoneEntity);
        return true;
    }
    
    @Override
    public ProjectMilestoneEntity getMilestoneById(String milestoneId) {
        if (StrUtil.isBlank(milestoneId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        ProjectMilestoneEntity entity = milestoneRepository.queryMilestoneById(milestoneId);
        if (entity == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "里程碑不存在");
        }
        return entity;
    }
    
    @Override
    public List<ProjectMilestoneEntity> getMilestonesByProjectId(String projectId) {
        if (StrUtil.isBlank(projectId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        List<ProjectMilestoneEntity> milestones = milestoneRepository.queryMilestonesByProjectId(projectId);
        return milestones != null ? milestones : new ArrayList<>();
    }

    @Override
    public ProjectMilestoneEntity getCurrentMilestoneByProjectId(String projectId) {
        List<ProjectMilestoneEntity> milestones = getMilestonesByProjectId(projectId);
        if (milestones.isEmpty()) {
            return null;
        }

        return milestones.stream()
                .filter(milestone -> milestone.getSequenceOrder() != null)
                .min(Comparator.comparingInt(this::resolveMilestonePriority)
                        .thenComparingInt(ProjectMilestoneEntity::getSequenceOrder))
                .orElse(null);
    }

    @Override
    public boolean startInitialMilestone(String projectId) {
        if (StrUtil.isBlank(projectId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        List<ProjectMilestoneEntity> allMilestones = milestoneRepository.queryMilestonesByProjectId(projectId);
        if (allMilestones == null || allMilestones.isEmpty()) {
            return false;
        }

        boolean alreadyStarted = allMilestones.stream().anyMatch(this::hasStartedExecution);
        if (alreadyStarted) {
            log.info("project milestones already started, skip initial activation projectId={}", projectId);
            return false;
        }

        ProjectMilestoneEntity initialMilestone = allMilestones.stream()
                .filter(milestone -> milestone.getSequenceOrder() != null)
                .filter(milestone -> MilestoneStatusEnum.PLANNED.getStatusCode().equals(milestone.getStatus()))
                .min(Comparator.comparingInt(ProjectMilestoneEntity::getSequenceOrder))
                .orElse(null);
        if (initialMilestone != null) {
            activateMilestone(initialMilestone, LocalDateTime.now());
            log.info("auto activate initial milestone milestoneId={}, projectId={}, sequenceOrder={}",
                    initialMilestone.getMilestoneId(), projectId, initialMilestone.getSequenceOrder());
            return true;
        }

        for (ProjectMilestoneEntity milestone : java.util.Collections.<ProjectMilestoneEntity>emptyList()) {
            if (milestone.getSequenceOrder() == null) {
                continue;
            }
            if (!MilestoneStatusEnum.PLANNED.getStatusCode().equals(milestone.getStatus())) {
                continue;
            }

            activateMilestone(milestone, LocalDateTime.now());
            log.info("自动启动首个里程碑 milestoneId={}, projectId={}, sequenceOrder={}",
                    milestone.getMilestoneId(), projectId, milestone.getSequenceOrder());
            return true;
        }

        return false;
    }
    
    @Override
    public ProjectMilestoneEntity getMilestoneByProjectIdAndCode(String projectId, String milestoneCode) {
        if (StrUtil.isBlank(projectId) || StrUtil.isBlank(milestoneCode)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        ProjectMilestoneEntity entity = milestoneRepository.queryMilestoneByProjectIdAndCode(projectId, milestoneCode);
        if (entity == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "里程碑不存在");
        }
        return entity;
    }
    
    @Override
    public boolean updateMilestoneStatus(String milestoneId, String status) {
        if (StrUtil.isBlank(milestoneId) || StrUtil.isBlank(status)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        if (!MilestoneStatusEnum.isValidStatus(status)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "无效的状态值");
        }
        ProjectMilestoneEntity entity = milestoneRepository.queryMilestoneById(milestoneId);
        if (entity == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "里程碑不存在");
        }
        
        // 状态流转校验
        if (!isValidStatusTransition(entity.getStatus(), status)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), 
                    String.format("状态流转不合法：%s -> %s", entity.getStatus(), status));
        }
        
        entity.setStatus(status);
        // 若转为进行中，记录实际开始时间
        if (MilestoneStatusEnum.IN_PROGRESS.getStatusCode().equals(status) && entity.getActualStartTime() == null) {
            entity.setActualStartTime(java.time.LocalDateTime.now());
        }
        // 若转为已完成，记录实际结束时间
        if (MilestoneStatusEnum.COMPLETED.getStatusCode().equals(status) && entity.getActualEndTime() == null) {
            entity.setActualEndTime(java.time.LocalDateTime.now());
        }
        milestoneRepository.updateMilestone(entity);
        log.info("更新里程碑状态成功 milestoneId={}, oldStatus={}, newStatus={}", milestoneId, entity.getStatus(), status);
        return true;
    }
    
    @Override
    public boolean updateMilestoneApprovalStatus(String milestoneId, String approvalStatus) {
        if (StrUtil.isBlank(milestoneId) || StrUtil.isBlank(approvalStatus)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        if (!MilestoneApprovalStatusEnum.isValidStatus(approvalStatus)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "无效的审批状态值");
        }
        ProjectMilestoneEntity entity = milestoneRepository.queryMilestoneById(milestoneId);
        if (entity == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "里程碑不存在");
        }
        milestoneRepository.updateMilestoneApprovalStatus(milestoneId, approvalStatus);
        log.info("更新里程碑审批状态成功 milestoneId={}, approvalStatus={}", milestoneId, approvalStatus);
        return true;
    }
    
    @Override
    public boolean submitMilestoneDeliverable(String milestoneId, String fileName, String deliverableUrl, Long fileSize) {
        if (StrUtil.isBlank(milestoneId) || StrUtil.isBlank(deliverableUrl)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        ProjectMilestoneEntity entity = milestoneRepository.queryMilestoneById(milestoneId);
        if (entity == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "里程碑不存在");
        }

        // 状态校验：允许进行中和已交付状态提交
        String currentStatus = entity.getStatus();
        if (!MilestoneStatusEnum.IN_PROGRESS.getStatusCode().equals(currentStatus)
                && !MilestoneStatusEnum.DELIVERED.getStatusCode().equals(currentStatus)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    String.format("当前状态 %s 不允许提交成果", currentStatus));
        }

        // 将新的交付物URL追加到deliverableFiles数组（去重）
        JSONArray fileList;
        String existingFiles = entity.getDeliverableFiles();
        if (StrUtil.isBlank(existingFiles)) {
            fileList = new JSONArray();
        } else {
            fileList = JSON.parseArray(existingFiles);
        }

        // 检查是否已存在相同的fileUrl，避免重复追加
        boolean fileExists = false;
        for (int i = 0; i < fileList.size(); i++) {
            JSONObject existingFile = fileList.getJSONObject(i);
            if (deliverableUrl.equals(existingFile.getString("fileUrl"))) {
                fileExists = true;
                break;
            }
        }

        // 只有文件不存在时才追加
        if (!fileExists) {
            JSONObject fileInfo = new JSONObject();
            fileInfo.put("fileName", fileName);
            fileInfo.put("fileUrl", deliverableUrl);
            fileInfo.put("fileSize", fileSize);
            fileInfo.put("uploadTime", java.time.LocalDateTime.now().toString());
            fileList.add(fileInfo);
            entity.setDeliverableFiles(fileList.toJSONString());
        }

        // 更新里程碑：设置交付物URL、状态转为delivered、审批状态为pending、进度30%
        entity.setDeliverableUrl(deliverableUrl);
        entity.setStatus(MilestoneStatusEnum.DELIVERED.getStatusCode());
        entity.setApprovalStatus(MilestoneApprovalStatusEnum.PENDING.getStatusCode());
        entity.setProgressPercent(30);
        
        milestoneRepository.updateMilestone(entity);
        log.info("提交里程碑成果成功 milestoneId={}, deliverableUrl={}", milestoneId, deliverableUrl);
        return true;
    }
    
    @Override
    public boolean approveMilestone(String milestoneId, String operatorId, boolean approved, String remark) {
        if (StrUtil.isBlank(milestoneId) || StrUtil.isBlank(operatorId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        
        ProjectMilestoneEntity entity = milestoneRepository.queryMilestoneById(milestoneId);
        if (entity == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "里程碑不存在");
        }
        
        // 状态校验：必须是 delivered 或 under_review 状态才能审批
        String currentStatus = entity.getStatus();
        if (!MilestoneStatusEnum.DELIVERED.getStatusCode().equals(currentStatus) 
                && !MilestoneStatusEnum.UNDER_REVIEW.getStatusCode().equals(currentStatus)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), 
                    String.format("当前状态 %s 不允许审批", currentStatus));
        }
        
        if (approved) {
            // 审批通过：状态转为completed，审批状态为approved，进度100%
            entity.setStatus(MilestoneStatusEnum.COMPLETED.getStatusCode());
            entity.setApprovalStatus(MilestoneApprovalStatusEnum.APPROVED.getStatusCode());
            entity.setProgressPercent(100);
            entity.setActualEndTime(LocalDateTime.now());
        } else {
            // 审批拒绝：状态转为rejected，审批状态为rejected
            entity.setStatus(MilestoneStatusEnum.REJECTED.getStatusCode());
            entity.setApprovalStatus(MilestoneApprovalStatusEnum.REJECTED.getStatusCode());
        }

        if (StrUtil.isNotBlank(remark)) {
            entity.setRemark(remark);
        }

        milestoneRepository.updateMilestone(entity);
        log.info("审批里程碑成功 milestoneId={}, operatorId={}, approved={}", milestoneId, operatorId, approved);

        // 审批通过后，自动启动下一个里程碑
        if (approved) {
            boolean nextStarted = startNextMilestone(entity.getProjectId(), entity.getSequenceOrder());
            if (!nextStarted && isProjectReadyToComplete(entity.getProjectId())) {
                projectRepository.updateProjectStatus(entity.getProjectId(), ProjectStatusEnum.COMPLETED.getStatusCode());
                log.info("最后一个里程碑审批通过后自动完成项目 projectId={}", entity.getProjectId());
            }
        }

        return true;
    }

    /**
     * 自动启动下一个里程碑
     * @param projectId 项目ID
     * @param currentSequenceOrder 当前里程碑的顺序号
     */
    private boolean startNextMilestone(String projectId, Integer currentSequenceOrder) {
        if (StrUtil.isBlank(projectId) || currentSequenceOrder == null) {
            return false;
        }

        List<ProjectMilestoneEntity> allMilestones = milestoneRepository.queryMilestonesByProjectId(projectId);
        if (allMilestones == null || allMilestones.isEmpty()) {
            return false;
        }

        ProjectMilestoneEntity nextMilestone = null;
        for (ProjectMilestoneEntity milestone : allMilestones) {
            if (milestone.getSequenceOrder() == null) {
                continue;
            }
            if (milestone.getSequenceOrder() <= currentSequenceOrder) {
                continue;
            }
            if (!MilestoneStatusEnum.PLANNED.getStatusCode().equals(milestone.getStatus())) {
                continue;
            }

            nextMilestone = milestone;
            break;
        }

        if (nextMilestone != null) {
            activateMilestone(nextMilestone, LocalDateTime.now());
            log.info("自动启动下一个里程碑 milestoneId={}, projectId={}, sequenceOrder={}",
                    nextMilestone.getMilestoneId(), projectId, nextMilestone.getSequenceOrder());
            return true;
        }
        return false;
    }

    private void activateMilestone(ProjectMilestoneEntity milestone, LocalDateTime startTime) {
        milestone.setStatus(MilestoneStatusEnum.IN_PROGRESS.getStatusCode());

        if (milestone.getProgressPercent() == null) {
            milestone.setProgressPercent(0);
        }
        if (StrUtil.isBlank(milestone.getApprovalStatus())) {
            milestone.setApprovalStatus(MilestoneApprovalStatusEnum.PENDING.getStatusCode());
        }
        if (milestone.getPlannedStartTime() == null) {
            milestone.setPlannedStartTime(startTime);
        }
        if (milestone.getActualStartTime() == null) {
            milestone.setActualStartTime(startTime);
        }
        milestone.setActualEndTime(null);

        milestoneRepository.updateMilestone(milestone);
    }

    private boolean hasStartedExecution(ProjectMilestoneEntity milestone) {
        if (milestone == null) {
            return false;
        }
        if (milestone.getActualStartTime() != null) {
            return true;
        }

        String status = milestone.getStatus();
        if (StrUtil.isBlank(status)) {
            return false;
        }

        return !MilestoneStatusEnum.PLANNED.getStatusCode().equals(status)
                && !MilestoneStatusEnum.SKIPPED.getStatusCode().equals(status);
    }

    private int resolveMilestonePriority(ProjectMilestoneEntity milestone) {
        if (milestone == null || StrUtil.isBlank(milestone.getStatus())) {
            return Integer.MAX_VALUE;
        }

        String status = milestone.getStatus();
        if (MilestoneStatusEnum.IN_PROGRESS.getStatusCode().equals(status)
                || MilestoneStatusEnum.DELIVERED.getStatusCode().equals(status)
                || MilestoneStatusEnum.UNDER_REVIEW.getStatusCode().equals(status)
                || MilestoneStatusEnum.REJECTED.getStatusCode().equals(status)) {
            return 0;
        }
        if (MilestoneStatusEnum.PLANNED.getStatusCode().equals(status)) {
            return 1;
        }
        if (MilestoneStatusEnum.COMPLETED.getStatusCode().equals(status)) {
            return 2;
        }
        if (MilestoneStatusEnum.EXPIRED.getStatusCode().equals(status)
                || MilestoneStatusEnum.SKIPPED.getStatusCode().equals(status)) {
            return 3;
        }
        return Integer.MAX_VALUE;
    }

    private boolean isProjectReadyToComplete(String projectId) {
        List<ProjectMilestoneEntity> milestones = milestoneRepository.queryMilestonesByProjectId(projectId);
        if (milestones == null || milestones.isEmpty()) {
            return false;
        }

        for (ProjectMilestoneEntity milestone : milestones) {
            String status = milestone.getStatus();
            if (!MilestoneStatusEnum.COMPLETED.getStatusCode().equals(status)
                    && !MilestoneStatusEnum.SKIPPED.getStatusCode().equals(status)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 校验状态流转是否合法
     * 规则：planned → in_progress → delivered → under_review → completed/rejected
     *      rejected → in_progress（重新提交）
     */
    private boolean isValidStatusTransition(String fromStatus, String toStatus) {
        if (StrUtil.isBlank(fromStatus) || StrUtil.isBlank(toStatus)) {
            return false;
        }
        
        // 相同状态不允许
        if (fromStatus.equals(toStatus)) {
            return false;
        }
        
        // planned → in_progress
        if (MilestoneStatusEnum.PLANNED.getStatusCode().equals(fromStatus) 
                && MilestoneStatusEnum.IN_PROGRESS.getStatusCode().equals(toStatus)) {
            return true;
        }
        
        // in_progress → delivered
        if (MilestoneStatusEnum.IN_PROGRESS.getStatusCode().equals(fromStatus) 
                && MilestoneStatusEnum.DELIVERED.getStatusCode().equals(toStatus)) {
            return true;
        }
        
        // delivered → under_review
        if (MilestoneStatusEnum.DELIVERED.getStatusCode().equals(fromStatus) 
                && MilestoneStatusEnum.UNDER_REVIEW.getStatusCode().equals(toStatus)) {
            return true;
        }
        
        // under_review → completed
        if (MilestoneStatusEnum.UNDER_REVIEW.getStatusCode().equals(fromStatus) 
                && MilestoneStatusEnum.COMPLETED.getStatusCode().equals(toStatus)) {
            return true;
        }
        
        // under_review → rejected
        if (MilestoneStatusEnum.UNDER_REVIEW.getStatusCode().equals(fromStatus) 
                && MilestoneStatusEnum.REJECTED.getStatusCode().equals(toStatus)) {
            return true;
        }
        
        // rejected → in_progress（重新提交）
        if (MilestoneStatusEnum.REJECTED.getStatusCode().equals(fromStatus) 
                && MilestoneStatusEnum.IN_PROGRESS.getStatusCode().equals(toStatus)) {
            return true;
        }
        
        return false;
    }

    @Override
    public boolean updateMilestoneDeadline(String milestoneId, java.time.LocalDateTime plannedStartTime, java.time.LocalDateTime plannedEndTime, Integer remindBeforeHours) {
        if (StrUtil.isBlank(milestoneId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        if (plannedStartTime != null && plannedEndTime != null && plannedEndTime.isBefore(plannedStartTime)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "计划结束时间不能早于计划开始时间");
        }
        ProjectMilestoneEntity existed = milestoneRepository.queryMilestoneById(milestoneId);
        if (existed == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "里程碑不存在");
        }
        milestoneRepository.updateMilestoneDeadline(milestoneId, plannedStartTime, plannedEndTime, remindBeforeHours);
        log.info("更新里程碑期限 milestoneId={}, plannedStart={}, plannedEnd={}, remindBeforeHours={}", milestoneId, plannedStartTime, plannedEndTime, remindBeforeHours);
        return true;
    }

}

