package cn.hgy.domain.project.service;

import cn.hgy.domain.audit.model.entity.AuditReviewRecordEntity;
import cn.hgy.domain.audit.repository.IAuditReviewRepository;
import cn.hgy.domain.project.model.entity.ProjectCategoryEntity;
import cn.hgy.domain.project.model.entity.ProjectEntity;
import cn.hgy.domain.project.model.entity.ProjectMilestoneEntity;
import cn.hgy.domain.project.model.valobj.ProjectStatusEnum;
import cn.hgy.domain.project.repository.IProjectRepository;
import cn.hgy.types.common.ProjectComment;
import cn.hgy.types.enums.ResponseCode;
import cn.hgy.types.exception.AppException;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.StrUtil;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-10-07
 * @Description: 项目服务实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectService implements IProjectService{

    private final Snowflake snowflake;
    private final IProjectRepository repository;
    private final IMilestoneTemplateService milestoneTemplateService;
    private final IMilestoneService milestoneService;
    private final IAuditReviewRepository auditReviewRepository;

    @Override
    public boolean createProject(ProjectComment projectComment) {
        String projectId = snowflake.nextIdStr();
        String serialNumber = generateSerialNumber();
        
        ProjectEntity projectEntity = ProjectEntity.builder()
                .projectId(projectId)
                .serialNumber(serialNumber)
                .title(projectComment.getTitle())
                .description(projectComment.getDescription())
                .categoryId(projectComment.getCategoryId())
                .tags(projectComment.getTags())
                .requirements(projectComment.getRequirements())
                .skillLevel(projectComment.getSkillLevel())
                .teamSizeMin(projectComment.getTeamSizeMin())
                .teamSizeMax(projectComment.getTeamSizeMax())
                .durationDays(projectComment.getDurationDays())
                .budgetType(projectComment.getBudgetType())
                .budgetAmount(projectComment.getBudgetAmount())
                .currency(projectComment.getCurrency())
                .publishedAt(projectComment.getPublishedAt())
                .applicationDeadline(projectComment.getApplicationDeadline())
                .expectedStartDate(projectComment.getExpectedStartDate())
                .expectedEndDate(projectComment.getExpectedEndDate())
                .status(ProjectStatusEnum.DRAFT.getStatusCode())
                .publisherId(projectComment.getPublisherId())
                .applicationCount(0)
                .viewCount(0)
                .favoriteCount(0)
                .progress(0)
                .version(0L)
                .build();

        projectEntity.validateForCreation();

        try {
            repository.createProject(projectEntity);
            log.info("项目创建成功 projectId={}", projectId);
            
            milestoneTemplateService.initializeRequiredMilestones(projectId);
            log.info("项目必须里程碑初始化成功 projectId={}", projectId);
        } catch (Exception e) {
            log.error("项目创建失败 projectId={} 原因:{}", projectId, e.getMessage());
            throw new AppException(ResponseCode.PROJECT_CREATE_FAIL.getCode(), ResponseCode.PROJECT_CREATE_FAIL.getInfo());
        }

        return true;
    }

    @Override
    public boolean saveDraft(ProjectComment projectComment) {
        String projectId = snowflake.nextIdStr();
        String serialNumber = generateSerialNumber();
        
        ProjectEntity projectEntity = ProjectEntity.builder()
                .projectId(projectId)
                .serialNumber(serialNumber)
                .title(projectComment.getTitle())
                .description(projectComment.getDescription())
                .categoryId(projectComment.getCategoryId())
                .tags(projectComment.getTags())
                .requirements(projectComment.getRequirements())
                .skillLevel(projectComment.getSkillLevel())
                .teamSizeMin(projectComment.getTeamSizeMin())
                .teamSizeMax(projectComment.getTeamSizeMax())
                .durationDays(projectComment.getDurationDays())
                .budgetType(projectComment.getBudgetType())
                .budgetAmount(projectComment.getBudgetAmount())
                .currency(projectComment.getCurrency())
                .applicationDeadline(projectComment.getApplicationDeadline())
                .expectedStartDate(projectComment.getExpectedStartDate())
                .expectedEndDate(projectComment.getExpectedEndDate())
                .status(ProjectStatusEnum.DRAFT.getStatusCode())
                .publisherId(projectComment.getPublisherId())
                .applicationCount(0)
                .viewCount(0)
                .favoriteCount(0)
                .progress(0)
                .version(0L)
                .build();

        projectEntity.validateForCreation();

        try {
            repository.createProject(projectEntity);
            log.info("项目草稿保存成功 projectId={}", projectId);
            
            milestoneTemplateService.initializeRequiredMilestones(projectId);
            log.info("项目必须里程碑初始化成功 projectId={}", projectId);
        } catch (Exception e) {
            log.error("保存项目草稿失败 原因:{}", e.getMessage());
            throw new AppException(ResponseCode.PROJECT_CREATE_FAIL.getCode(), ResponseCode.PROJECT_CREATE_FAIL.getInfo());
        }

        return true;
    }

    @Override
    public boolean updateProject(ProjectComment projectComment, String publisherId) {
        ProjectEntity existingProject = repository.queryProjectById(projectComment.getProjectId());
        
        if (existingProject == null) {
            throw new AppException(ResponseCode.PROJECT_NOT_FOUND.getCode(), ResponseCode.PROJECT_NOT_FOUND.getInfo());
        }
        
        if (!existingProject.getPublisherId().equals(publisherId)) {
            throw new AppException(ResponseCode.PROJECT_PERMISSION_DENIED.getCode(), ResponseCode.PROJECT_PERMISSION_DENIED.getInfo());
        }
        
        if (!ProjectStatusEnum.DRAFT.getStatusCode().equals(existingProject.getStatus()) && 
            !ProjectStatusEnum.REJECTED.getStatusCode().equals(existingProject.getStatus())) {
            throw new AppException(ResponseCode.PROJECT_STATUS_INVALID.getCode(), ResponseCode.PROJECT_STATUS_INVALID.getInfo());
        }

        ProjectEntity projectEntity = ProjectEntity.builder()
                .projectId(projectComment.getProjectId())
                .serialNumber(existingProject.getSerialNumber())
                .title(projectComment.getTitle())
                .description(projectComment.getDescription())
                .categoryId(projectComment.getCategoryId())
                .tags(projectComment.getTags())
                .requirements(projectComment.getRequirements())
                .skillLevel(projectComment.getSkillLevel())
                .teamSizeMin(projectComment.getTeamSizeMin())
                .teamSizeMax(projectComment.getTeamSizeMax())
                .durationDays(projectComment.getDurationDays())
                .budgetType(projectComment.getBudgetType())
                .budgetAmount(projectComment.getBudgetAmount())
                .currency(projectComment.getCurrency())
                .applicationDeadline(projectComment.getApplicationDeadline())
                .expectedStartDate(projectComment.getExpectedStartDate())
                .expectedEndDate(projectComment.getExpectedEndDate())
                .status(existingProject.getStatus())
                .publisherId(publisherId)
                .applicationCount(existingProject.getApplicationCount())
                .viewCount(existingProject.getViewCount())
                .favoriteCount(existingProject.getFavoriteCount())
                .progress(existingProject.getProgress())
                .version(existingProject.getVersion())
                .build();

        projectEntity.validateForCreation();

        try {
            repository.updateProject(projectEntity);
        } catch (Exception e) {
            log.error("项目更新失败 原因:{}", e.getMessage());
            throw new AppException(ResponseCode.PROJECT_UPDATE_FAIL.getCode(), ResponseCode.PROJECT_UPDATE_FAIL.getInfo());
        }

        return true;
    }

    @Override
    public ProjectEntity getProjectDetail(String projectId) {
        ProjectEntity projectEntity = repository.queryProjectByIdWithNames(projectId);
        if (projectEntity == null) {
            throw new AppException(ResponseCode.PROJECT_NOT_FOUND.getCode(), ResponseCode.PROJECT_NOT_FOUND.getInfo());
        }
        return projectEntity;
    }

    @Override
    public List<ProjectEntity> getProjectsByPublisher(String publisherId, String status, Integer pageNum, Integer pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return repository.queryProjectsByPublisherId(publisherId, status, offset, pageSize);
    }

    @Override
    public Long countProjectsByPublisher(String publisherId, String status) {
        return repository.countProjectsByPublisherId(publisherId, status);
    }

    @Override
    public boolean submitForReview(String projectId, String publisherId) {
        ProjectEntity existingProject = repository.queryProjectById(projectId);
        
        if (existingProject == null) {
            throw new AppException(ResponseCode.PROJECT_NOT_FOUND.getCode(), ResponseCode.PROJECT_NOT_FOUND.getInfo());
        }
        
        if (!existingProject.getPublisherId().equals(publisherId)) {
            throw new AppException(ResponseCode.PROJECT_PERMISSION_DENIED.getCode(), ResponseCode.PROJECT_PERMISSION_DENIED.getInfo());
        }
        
        if (!ProjectStatusEnum.DRAFT.getStatusCode().equals(existingProject.getStatus()) && 
            !ProjectStatusEnum.REJECTED.getStatusCode().equals(existingProject.getStatus())) {
            throw new AppException(ResponseCode.PROJECT_STATUS_INVALID.getCode(), ResponseCode.PROJECT_STATUS_INVALID.getInfo());
        }

        List<ProjectMilestoneEntity> milestones = milestoneService.getMilestonesByProjectId(projectId);
        if (milestones == null || milestones.isEmpty()) {
            throw new AppException(ResponseCode.MILESTONE_NOT_CONFIGURED.getCode(), ResponseCode.MILESTONE_NOT_CONFIGURED.getInfo());
        }

        for (ProjectMilestoneEntity milestone : milestones) {
            if (StrUtil.isBlank(milestone.getDescription())) {
                throw new AppException(ResponseCode.MILESTONE_INCOMPLETE.getCode(), 
                    "里程碑【" + milestone.getName() + "】缺少描述信息");
            }
            if (milestone.getPlannedStartTime() == null || milestone.getPlannedEndTime() == null) {
                throw new AppException(ResponseCode.MILESTONE_INCOMPLETE.getCode(), 
                    "里程碑【" + milestone.getName() + "】缺少计划时间");
            }
        }

        try {
            repository.updateProjectStatusAndSubmittedAt(projectId, ProjectStatusEnum.PENDING_REVIEW.getStatusCode(), LocalDateTime.now());
        } catch (Exception e) {
            log.error("提交项目审核失败 原因:{}", e.getMessage());
            throw new AppException(ResponseCode.PROJECT_UPDATE_FAIL.getCode(), ResponseCode.PROJECT_UPDATE_FAIL.getInfo());
        }

        return true;
    }

    @Override
    public List<ProjectEntity> getPublishedProjects(String categoryId, Integer pageNum, Integer pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return repository.queryPublishedProjects(categoryId, offset, pageSize);
    }

    @Override
    public Long countPublishedProjects(String categoryId) {
        return repository.countPublishedProjects(categoryId);
    }

    @Override
    public List<ProjectEntity> searchProjects(String keyword, String categoryId, String skillLevel,
                                             java.math.BigDecimal budgetMin, java.math.BigDecimal budgetMax,
                                             Integer pageNum, Integer pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return repository.searchProjects(keyword, categoryId, skillLevel, budgetMin, budgetMax, offset, pageSize);
    }

    @Override
    public Long countSearchResults(String keyword, String categoryId, String skillLevel,
                                   java.math.BigDecimal budgetMin, java.math.BigDecimal budgetMax) {
        return repository.countSearchResults(keyword, categoryId, skillLevel, budgetMin, budgetMax);
    }

    @Override
    public List<ProjectEntity> getPopularProjects(Integer limit) {
        return repository.queryPopularProjects(limit);
    }

    @Override
    public List<ProjectEntity> getLatestProjects(Integer limit) {
        return repository.queryLatestProjects(limit);
    }

    @Override
    public void incrementViewCount(String projectId) {
        repository.incrementViewCount(projectId);
    }

    @Override
    public boolean approveProject(String projectId, String reviewerId) {
        ProjectEntity projectEntity = repository.queryProjectById(projectId);
        if (projectEntity == null) {
            throw new AppException(ResponseCode.PROJECT_NOT_FOUND.getCode(), ResponseCode.PROJECT_NOT_FOUND.getInfo());
        }

        if (!"pending_review".equals(projectEntity.getStatus())) {
            throw new AppException(ResponseCode.PROJECT_STATUS_INVALID.getCode(), ResponseCode.PROJECT_STATUS_INVALID.getInfo());
        }

        repository.updateProjectStatusAndReason(projectId, "published", null);
        saveAuditRecord("project", projectId, "approve", projectEntity.getStatus(), "published", null, reviewerId);
        return true;
    }

    @Override
    public boolean rejectProject(String projectId, String rejectReason, String reviewerId) {
        ProjectEntity projectEntity = repository.queryProjectById(projectId);
        if (projectEntity == null) {
            throw new AppException(ResponseCode.PROJECT_NOT_FOUND.getCode(), ResponseCode.PROJECT_NOT_FOUND.getInfo());
        }

        if (!"pending_review".equals(projectEntity.getStatus())) {
            throw new AppException(ResponseCode.PROJECT_STATUS_INVALID.getCode(), ResponseCode.PROJECT_STATUS_INVALID.getInfo());
        }

        repository.updateProjectStatusAndReason(projectId, "rejected", rejectReason);
        saveAuditRecord("project", projectId, "reject", projectEntity.getStatus(), "rejected", rejectReason, reviewerId);
        return true;
    }

    @Override
    public List<ProjectEntity> getProjectsByStatus(String status, Integer pageNum, Integer pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return repository.queryProjectsByStatus(status, offset, pageSize);
    }

    @Override
    public Long countProjectsByStatus(String status) {
        return repository.countProjectsByStatus(status);
    }

    @Override
    public boolean createProjectCategory(String name, String parentId, String description, String icon, Integer sortOrder) {
        if (StrUtil.isBlank(name)) {
            throw new AppException(ResponseCode.CATEGORY_NAME_EMPTY.getCode(), ResponseCode.CATEGORY_NAME_EMPTY.getInfo());
        }

        if (repository.existsCategoryByName(name)) {
            throw new AppException(ResponseCode.CATEGORY_NAME_EXISTS.getCode(), ResponseCode.CATEGORY_NAME_EXISTS.getInfo());
        }

        if (StrUtil.isNotBlank(parentId) && !repository.existsCategoryById(parentId)) {
            throw new AppException(ResponseCode.PARENT_CATEGORY_NOT_FOUND.getCode(), ResponseCode.PARENT_CATEGORY_NOT_FOUND.getInfo());
        }

        String categoryId = "CAT_" + snowflake.nextIdStr();

        ProjectCategoryEntity projectCategoryEntity = ProjectCategoryEntity.builder()
                .projectCategoryId(categoryId)
                .name(name)
                .parentId(parentId)
                .description(description)
                .icon(icon)
                .sortOrder(sortOrder != null ? sortOrder : 0)
                .isVisible(true)
                .isActive(true)
                .build();

        repository.createProjectCategory(projectCategoryEntity);
        return true;
    }

    @Override
    public boolean updateProjectCategory(String categoryId, String name, String parentId, String description, String icon, Integer sortOrder, Boolean isVisible, Boolean isActive) {
        if (StrUtil.isBlank(categoryId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        if (!repository.existsCategoryById(categoryId)) {
            throw new AppException(ResponseCode.CATEGORY_NOT_FOUND.getCode(), ResponseCode.CATEGORY_NOT_FOUND.getInfo());
        }

        if (StrUtil.isBlank(name)) {
            throw new AppException(ResponseCode.CATEGORY_NAME_EMPTY.getCode(), ResponseCode.CATEGORY_NAME_EMPTY.getInfo());
        }

        if (StrUtil.isNotBlank(parentId) && !repository.existsCategoryById(parentId)) {
            throw new AppException(ResponseCode.PARENT_CATEGORY_NOT_FOUND.getCode(), ResponseCode.PARENT_CATEGORY_NOT_FOUND.getInfo());
        }

        ProjectCategoryEntity projectCategoryEntity = ProjectCategoryEntity.builder()
                .projectCategoryId(categoryId)
                .name(name)
                .parentId(parentId)
                .description(description)
                .icon(icon)
                .sortOrder(sortOrder)
                .isVisible(isVisible)
                .isActive(isActive)
                .build();

        repository.updateProjectCategory(projectCategoryEntity);
        return true;
    }

    @Override
    public boolean deleteProjectCategory(String categoryId) {
        if (!repository.existsCategoryById(categoryId)) {
            throw new AppException(ResponseCode.CATEGORY_NOT_FOUND.getCode(), ResponseCode.CATEGORY_NOT_FOUND.getInfo());
        }

        repository.deleteProjectCategory(categoryId);
        return true;
    }

    @Override
    public List<ProjectCategoryEntity> getAllCategories() {
        return repository.queryAllCategories();
    }

    @Override
    public List<ProjectCategoryEntity> getVisibleCategories() {
        return repository.queryVisibleCategories();
    }

    @Override
    public ProjectCategoryEntity getCategoryById(String categoryId) {
        ProjectCategoryEntity category = repository.queryCategoryById(categoryId);
        if (category == null) {
            throw new AppException(ResponseCode.CATEGORY_NOT_FOUND.getCode(), ResponseCode.CATEGORY_NOT_FOUND.getInfo());
        }
        return category;
    }

    @Override
    public boolean publishDraftDirectly(String projectId, String publisherId) {
        ProjectEntity existingProject = repository.queryProjectById(projectId);
        
        if (existingProject == null) {
            throw new AppException(ResponseCode.PROJECT_NOT_FOUND.getCode(), ResponseCode.PROJECT_NOT_FOUND.getInfo());
        }
        
        if (!existingProject.getPublisherId().equals(publisherId)) {
            throw new AppException(ResponseCode.PROJECT_PERMISSION_DENIED.getCode(), ResponseCode.PROJECT_PERMISSION_DENIED.getInfo());
        }
        
        if (!ProjectStatusEnum.DRAFT.getStatusCode().equals(existingProject.getStatus())) {
            throw new AppException(ResponseCode.PROJECT_STATUS_INVALID.getCode(), "只能发布草稿状态的项目");
        }

        List<ProjectMilestoneEntity> milestones = milestoneService.getMilestonesByProjectId(projectId);
        if (milestones == null || milestones.isEmpty()) {
            throw new AppException(ResponseCode.MILESTONE_NOT_CONFIGURED.getCode(), ResponseCode.MILESTONE_NOT_CONFIGURED.getInfo());
        }

        for (ProjectMilestoneEntity milestone : milestones) {
            if (StrUtil.isBlank(milestone.getDescription())) {
                throw new AppException(ResponseCode.MILESTONE_INCOMPLETE.getCode(), 
                    "里程碑【" + milestone.getName() + "】缺少描述信息");
            }
            if (milestone.getPlannedStartTime() == null || milestone.getPlannedEndTime() == null) {
                throw new AppException(ResponseCode.MILESTONE_INCOMPLETE.getCode(), 
                    "里程碑【" + milestone.getName() + "】缺少计划时间");
            }
        }

        repository.updateProjectStatusAndSubmittedAt(projectId, ProjectStatusEnum.PENDING_REVIEW.getStatusCode(), LocalDateTime.now());
        log.info("企业发布项目，进入待审核状态 projectId={} publisherId={}", projectId, publisherId);
        return true;
    }

    @Override
    public List<ProjectEntity> getProjectsByAcceptedTeam(String teamId, String status, Integer pageNum, Integer pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return repository.queryProjectsByAcceptedTeamId(teamId, status, offset, pageSize);
    }

    @Override
    public Long countProjectsByAcceptedTeam(String teamId, String status) {
        return repository.countProjectsByAcceptedTeamId(teamId, status);
    }

    @Override
    public boolean closeProject(String projectId, String publisherId) {
        ProjectEntity existingProject = repository.queryProjectById(projectId);
        if (existingProject == null) {
            throw new AppException(ResponseCode.PROJECT_NOT_FOUND.getCode(), ResponseCode.PROJECT_NOT_FOUND.getInfo());
        }

        if (!existingProject.getPublisherId().equals(publisherId)) {
            throw new AppException(ResponseCode.PROJECT_PERMISSION_DENIED.getCode(), ResponseCode.PROJECT_PERMISSION_DENIED.getInfo());
        }

        if (!ProjectStatusEnum.COMPLETED.getStatusCode().equals(existingProject.getStatus())) {
            throw new AppException(ResponseCode.PROJECT_STATUS_INVALID.getCode(), ResponseCode.PROJECT_STATUS_INVALID.getInfo());
        }

        repository.updateProjectStatus(projectId, ProjectStatusEnum.CLOSED.getStatusCode());
        log.info("企业手动完结项目 projectId={} publisherId={}", projectId, publisherId);
        return true;
    }

    @Override
    public int autoCloseCompletedProjects() {
        LocalDateTime cutoff = LocalDateTime.now().minusWeeks(2);
        List<ProjectEntity> completedProjects = repository.queryProjectsByStatusUpdatedBefore(
                ProjectStatusEnum.COMPLETED.getStatusCode(), cutoff);

        int closedCount = 0;
        for (ProjectEntity project : completedProjects) {
            repository.updateProjectStatus(project.getProjectId(), ProjectStatusEnum.CLOSED.getStatusCode());
            closedCount++;
        }

        if (closedCount > 0) {
            log.info("自动关闭已完成项目完成 count={} cutoff={}", closedCount, cutoff);
        }
        return closedCount;
    }

    @Override
    public boolean completeProject(String projectId) {
        ProjectEntity existingProject = repository.queryProjectById(projectId);
        if (existingProject == null) {
            throw new AppException(ResponseCode.PROJECT_NOT_FOUND.getCode(), ResponseCode.PROJECT_NOT_FOUND.getInfo());
        }

        if (!ProjectStatusEnum.IN_PROGRESS.getStatusCode().equals(existingProject.getStatus())) {
            return false;
        }

        repository.updateProjectStatus(projectId, ProjectStatusEnum.COMPLETED.getStatusCode());
        log.info("项目进入已完成状态 projectId={}", projectId);
        return true;
    }

    private String generateSerialNumber() {
        String datePart = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = String.format("%06d", new java.util.Random().nextInt(1000000));
        return "PRJ" + datePart + randomPart;
    }

    private void saveAuditRecord(String auditType, String targetId, String action,
                                 String fromStatus, String toStatus, String reason, String reviewerId) {
        auditReviewRepository.save(AuditReviewRecordEntity.builder()
                .auditType(auditType)
                .targetId(targetId)
                .action(action)
                .fromStatus(fromStatus)
                .toStatus(toStatus)
                .reason(reason)
                .reviewerId(reviewerId)
                .build());
    }
}
