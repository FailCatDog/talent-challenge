package cn.hgy.infrastructure.persistent.repository;

import cn.hgy.domain.project.model.entity.ProjectCategoryEntity;
import cn.hgy.domain.project.model.entity.ProjectEntity;
import cn.hgy.domain.project.repository.IProjectRepository;
import cn.hgy.infrastructure.persistent.dao.ProjectCategoryDao;
import cn.hgy.infrastructure.persistent.dao.ProjectDao;
import cn.hgy.infrastructure.persistent.po.Project;
import cn.hgy.infrastructure.persistent.po.ProjectCategory;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-10-07
 * @Description: 项目仓储实现
 */
@Repository
@Slf4j
@RequiredArgsConstructor
public class ProjectRepository implements IProjectRepository {

    private final ProjectDao projectDao;
    private final ProjectCategoryDao projectCategoryDao;

    @Override
    public void createProject(ProjectEntity projectEntity) {
        Project project = convertToProject(projectEntity);
        project.setCreateTime(LocalDateTime.now());
        project.setUpdateTime(LocalDateTime.now());
        projectDao.insert(project);
    }

    @Override
    public void updateProject(ProjectEntity projectEntity) {
        Project project = convertToProject(projectEntity);
        project.setUpdateTime(LocalDateTime.now());
        projectDao.updateByProjectId(project);
    }

    @Override
    public ProjectEntity queryProjectById(String projectId) {
        Project project = projectDao.selectByProjectId(projectId);
        return project == null ? null : convertToEntity(project);
    }

    @Override
    public List<ProjectEntity> queryProjectsByPublisherId(String publisherId, String status, Integer offset, Integer limit) {
        List<Project> projects;
        if (StrUtil.isNotBlank(status)) {
            projects = projectDao.selectByPublisherIdAndStatus(publisherId, status, offset, limit);
        } else {
            projects = projectDao.selectByPublisherIdAndPage(publisherId, offset, limit);
        }
        
        List<ProjectEntity> entities = new ArrayList<>();
        for (Project project : projects) {
            entities.add(convertToEntity(project));
        }
        return entities;
    }

    @Override
    public Long countProjectsByPublisherId(String publisherId, String status) {
        if (StrUtil.isNotBlank(status)) {
            return projectDao.countByPublisherIdAndStatus(publisherId, status);
        } else {
            return projectDao.countByPublisherId(publisherId);
        }
    }

    @Override
    public void updateProjectStatus(String projectId, String status) {
        projectDao.updateStatus(projectId, status);
    }

    @Override
    public List<ProjectEntity> queryPublishedProjects(String categoryId, Integer offset, Integer limit) {
        List<Project> projects;
        if (StrUtil.isNotBlank(categoryId)) {
            projects = projectDao.selectPublishedByCategoryId(categoryId, offset, limit);
        } else {
            projects = projectDao.selectPublishedByPage(offset, limit);
        }
        
        List<ProjectEntity> entities = new ArrayList<>();
        for (Project project : projects) {
            entities.add(convertToEntity(project));
        }
        return entities;
    }

    @Override
    public Long countPublishedProjects(String categoryId) {
        if (StrUtil.isNotBlank(categoryId)) {
            return projectDao.countPublishedByCategoryId(categoryId);
        } else {
            return projectDao.countPublished();
        }
    }

    @Override
    public List<ProjectEntity> searchProjects(String keyword, String categoryId, String skillLevel,
                                             java.math.BigDecimal budgetMin, java.math.BigDecimal budgetMax,
                                             Integer offset, Integer limit) {
        List<Project> projects = projectDao.searchProjects(keyword, categoryId, skillLevel, budgetMin, budgetMax, offset, limit);
        
        List<ProjectEntity> entities = new ArrayList<>();
        for (Project project : projects) {
            entities.add(convertToEntity(project));
        }
        return entities;
    }

    @Override
    public Long countSearchResults(String keyword, String categoryId, String skillLevel,
                                   java.math.BigDecimal budgetMin, java.math.BigDecimal budgetMax) {
        return projectDao.countSearchResults(keyword, categoryId, skillLevel, budgetMin, budgetMax);
    }

    @Override
    public List<ProjectEntity> queryPopularProjects(Integer limit) {
        List<Project> projects = projectDao.selectPopularProjects(limit);
        
        List<ProjectEntity> entities = new ArrayList<>();
        for (Project project : projects) {
            entities.add(convertToEntity(project));
        }
        return entities;
    }

    @Override
    public List<ProjectEntity> queryLatestProjects(Integer limit) {
        List<Project> projects = projectDao.selectLatestProjects(limit);
        
        List<ProjectEntity> entities = new ArrayList<>();
        for (Project project : projects) {
            entities.add(convertToEntity(project));
        }
        return entities;
    }

    @Override
    public void incrementViewCount(String projectId) {
        projectDao.incrementViewCount(projectId);
    }

    @Override
    public void updateProjectStatusAndReason(String projectId, String status, String rejectReason) {
        projectDao.updateStatusAndReason(projectId, status, rejectReason);
    }

    @Override
    public List<ProjectEntity> queryProjectsByStatus(String status, Integer offset, Integer limit) {
        List<Project> projects = projectDao.selectByStatusAndPage(status, offset, limit);
        
        List<ProjectEntity> entities = new ArrayList<>();
        for (Project project : projects) {
            entities.add(convertToEntity(project));
        }
        return entities;
    }

    @Override
    public Long countProjectsByStatus(String status) {
        return projectDao.countByStatus(status);
    }

    @Override
    public void incrementApplicationCount(String projectId) {
        projectDao.incrementApplicationCount(projectId);
    }

    private Project convertToProject(ProjectEntity projectEntity) {
        Project project = new Project();
        project.setProjectId(projectEntity.getProjectId());
        project.setSerialNumber(projectEntity.getSerialNumber());
        project.setTitle(projectEntity.getTitle());
        project.setDescription(projectEntity.getDescription());
        project.setCategoryId(projectEntity.getCategoryId());
        project.setTags(projectEntity.getTags());
        project.setRequirements(projectEntity.getRequirements());
        project.setSkillLevel(projectEntity.getSkillLevel());
        project.setTeamSizeMin(projectEntity.getTeamSizeMin());
        project.setTeamSizeMax(projectEntity.getTeamSizeMax());
        project.setDurationDays(projectEntity.getDurationDays());
        project.setBudgetType(projectEntity.getBudgetType());
        project.setBudgetAmount(projectEntity.getBudgetAmount());
        project.setCurrency(projectEntity.getCurrency());
        project.setPublishedAt(projectEntity.getPublishedAt());
        project.setApplicationDeadline(projectEntity.getApplicationDeadline());
        project.setExpectedStartDate(projectEntity.getExpectedStartDate());
        project.setExpectedEndDate(projectEntity.getExpectedEndDate());
        project.setStatus(projectEntity.getStatus());
        project.setRejectReason(projectEntity.getRejectReason());
        project.setProgress(projectEntity.getProgress());
        project.setPublisherId(projectEntity.getPublisherId());
        project.setAcceptedTeamId(projectEntity.getAcceptedTeamId());
        project.setApplicationCount(projectEntity.getApplicationCount());
        project.setViewCount(projectEntity.getViewCount());
        project.setFavoriteCount(projectEntity.getFavoriteCount());
        project.setVersion(projectEntity.getVersion());
        return project;
    }

    @Override
    public void createProjectCategory(ProjectCategoryEntity projectCategoryEntity) {
        ProjectCategory projectCategory = new ProjectCategory();
        projectCategory.setProjectCategoryId(projectCategoryEntity.getProjectCategoryId());
        projectCategory.setName(projectCategoryEntity.getName());
        projectCategory.setParentId(projectCategoryEntity.getParentId());
        projectCategory.setDescription(projectCategoryEntity.getDescription());
        projectCategory.setIcon(projectCategoryEntity.getIcon());
        projectCategory.setSortOrder(projectCategoryEntity.getSortOrder());
        projectCategory.setIsVisible(projectCategoryEntity.getIsVisible());
        projectCategory.setIsActive(projectCategoryEntity.getIsActive());
        projectCategory.setCreateTime(LocalDateTime.now());
        projectCategory.setUpdateTime(LocalDateTime.now());
        projectCategoryDao.insert(projectCategory);
    }

    @Override
    public boolean existsCategoryByName(String name) {
        return projectCategoryDao.existsByName(name) > 0;
    }

    @Override
    public boolean existsCategoryById(String categoryId) {
        return projectCategoryDao.existsByCategoryId(categoryId) > 0;
    }

    @Override
    public void updateProjectCategory(ProjectCategoryEntity projectCategoryEntity) {
        ProjectCategory projectCategory = new ProjectCategory();
        projectCategory.setProjectCategoryId(projectCategoryEntity.getProjectCategoryId());
        projectCategory.setName(projectCategoryEntity.getName());
        projectCategory.setParentId(projectCategoryEntity.getParentId());
        projectCategory.setDescription(projectCategoryEntity.getDescription());
        projectCategory.setIcon(projectCategoryEntity.getIcon());
        projectCategory.setSortOrder(projectCategoryEntity.getSortOrder());
        projectCategory.setIsVisible(projectCategoryEntity.getIsVisible());
        projectCategory.setIsActive(projectCategoryEntity.getIsActive());
        projectCategory.setUpdateTime(LocalDateTime.now());
        projectCategoryDao.updateByCategoryId(projectCategory);
    }

    @Override
    public void deleteProjectCategory(String categoryId) {
        projectCategoryDao.deleteByCategoryId(categoryId);
    }

    @Override
    public List<ProjectCategoryEntity> queryAllCategories() {
        List<ProjectCategory> categories = projectCategoryDao.selectAll();
        List<ProjectCategoryEntity> entities = new ArrayList<>();
        for (ProjectCategory category : categories) {
            entities.add(convertToCategoryEntity(category));
        }
        return entities;
    }

    @Override
    public List<ProjectCategoryEntity> queryVisibleCategories() {
        List<ProjectCategory> categories = projectCategoryDao.selectAllVisible();
        List<ProjectCategoryEntity> entities = new ArrayList<>();
        for (ProjectCategory category : categories) {
            entities.add(convertToCategoryEntity(category));
        }
        return entities;
    }

    @Override
    public ProjectCategoryEntity queryCategoryById(String categoryId) {
        ProjectCategory category = projectCategoryDao.selectByCategoryId(categoryId);
        return category == null ? null : convertToCategoryEntity(category);
    }

    private ProjectCategoryEntity convertToCategoryEntity(ProjectCategory category) {
        return ProjectCategoryEntity.builder()
                .projectCategoryId(category.getProjectCategoryId())
                .name(category.getName())
                .parentId(category.getParentId())
                .description(category.getDescription())
                .icon(category.getIcon())
                .sortOrder(category.getSortOrder())
                .isVisible(category.getIsVisible())
                .isActive(category.getIsActive())
                .build();
    }

    private ProjectEntity convertToEntity(Project project) {
        return ProjectEntity.builder()
                .projectId(project.getProjectId())
                .serialNumber(project.getSerialNumber())
                .title(project.getTitle())
                .description(project.getDescription())
                .categoryId(project.getCategoryId())
                .tags(project.getTags())
                .requirements(project.getRequirements())
                .skillLevel(project.getSkillLevel())
                .teamSizeMin(project.getTeamSizeMin())
                .teamSizeMax(project.getTeamSizeMax())
                .durationDays(project.getDurationDays())
                .budgetType(project.getBudgetType())
                .budgetAmount(project.getBudgetAmount())
                .currency(project.getCurrency())
                .publishedAt(project.getPublishedAt())
                .applicationDeadline(project.getApplicationDeadline())
                .expectedStartDate(project.getExpectedStartDate())
                .expectedEndDate(project.getExpectedEndDate())
                .status(project.getStatus())
                .rejectReason(project.getRejectReason())
                .progress(project.getProgress())
                .publisherId(project.getPublisherId())
                .acceptedTeamId(project.getAcceptedTeamId())
                .applicationCount(project.getApplicationCount())
                .viewCount(project.getViewCount())
                .favoriteCount(project.getFavoriteCount())
                .version(project.getVersion())
                .build();
    }
}
