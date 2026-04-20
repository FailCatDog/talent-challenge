package cn.hgy.trigger.http;

import cn.hgy.api.IProjectHttpService;
import cn.hgy.api.dto.*;
import cn.hgy.api.response.Response;
import cn.hgy.domain.project.model.entity.ProjectCategoryEntity;
import cn.hgy.domain.project.model.entity.ProjectEntity;
import cn.hgy.domain.project.model.entity.ProjectMilestoneEntity;
import cn.hgy.domain.project.service.IProjectService;
import cn.hgy.domain.project.service.IMilestoneService;
import cn.hgy.infrastructure.persistent.jwt.JWTServiceImpl;
import cn.hgy.trigger.interceptor.UserContextDynamic;
import cn.hgy.types.common.ProjectComment;
import cn.hgy.domain.user.model.entity.CompanyProfileEntity;
import cn.hgy.domain.user.repository.IUserManagementRepository;
import cn.hgy.types.enums.CertificationStatusEnum;
import cn.hgy.types.enums.ResponseCode;
import cn.hgy.types.exception.AppException;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-10-07
 * @Description: 项目业务控制器
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/project")
public class ProjectHttpController implements IProjectHttpService {

    private final IProjectService projectService;
    private final IMilestoneService milestoneService;
    private final JWTServiceImpl jwtService;
    private final cn.hgy.domain.team.repository.ITeamRepository teamRepository;
    private final IUserManagementRepository userManagementRepository;

    @PostMapping("create_project")
    @Override
    public Response<Boolean> createProject(@RequestBody ProjectCreateRequestDTO dto, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        logRequest("企业创建项目", userId, request);

        ProjectComment projectComment = buildProjectComment(dto, userId);
        boolean result = projectService.createProject(projectComment);

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    @PostMapping("save_draft")
    @Override
    public Response<Boolean> saveDraft(@RequestBody ProjectCreateRequestDTO dto, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        logRequest("企业保存项目草稿", userId, request);

        // 校验企业认证状态
        checkCompanyCertified(userId);

        ProjectComment projectComment = buildProjectComment(dto, userId);
        boolean result = projectService.saveDraft(projectComment);

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    @PostMapping("update_project")
    @Override
    public Response<Boolean> updateProject(@RequestBody ProjectUpdateRequestDTO dto, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        logRequest("企业更新项目", userId, request);

        ProjectComment projectComment = ProjectComment.builder()
                .projectId(dto.getProjectId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .categoryId(dto.getCategoryId())
                .tags(dto.getTags())
                .requirements(dto.getRequirements())
                .skillLevel(dto.getSkillLevel())
                .teamSizeMin(dto.getTeamSizeMin())
                .teamSizeMax(dto.getTeamSizeMax())
                .durationDays(dto.getDurationDays())
                .budgetType(dto.getBudgetType())
                .budgetAmount(dto.getBudgetAmount())
                .currency(dto.getCurrency())
                .publishedAt(dto.getPublishedAt())
                .applicationDeadline(dto.getApplicationDeadline())
                .expectedStartDate(dto.getExpectedStartDate())
                .expectedEndDate(dto.getExpectedEndDate())
                .build();

        boolean result = projectService.updateProject(projectComment, userId);

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    @GetMapping("detail/{projectId}")
    @Override
    public Response<ProjectDetailResponseDTO> getProjectDetail(@PathVariable("projectId") String projectId, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        logRequest("查询项目详情", userId, request);

        ProjectEntity projectEntity = projectService.getProjectDetail(projectId);
        projectService.incrementViewCount(projectId);
        ProjectDetailResponseDTO dto = convertToDetailDTO(projectEntity);

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), dto);
    }

    @GetMapping("my_projects")
    @Override
    public Response<ProjectListResponseDTO> getMyProjects(@RequestParam(value = "status", required = false) String status, 
                                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, 
                                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                           HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        logRequest("企业查询自己的项目列表", userId, request);

        List<ProjectEntity> projects = projectService.getProjectsByPublisher(userId, status, pageNum, pageSize);
        Long total = projectService.countProjectsByPublisher(userId, status);

        List<ProjectListItemResponseDTO> projectDTOs = new ArrayList<>();
        for (ProjectEntity project : projects) {
            projectDTOs.add(convertToListItemDTO(project, true));
        }

        ProjectListResponseDTO listDTO = ProjectListResponseDTO.builder()
                .projects(projectDTOs)
                .total(total)
                .pageNum(pageNum)
                .pageSize(pageSize)
                .build();

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), listDTO);
    }

    @PostMapping("submit_review/{projectId}")
    @Override
    public Response<Boolean> submitForReview(@PathVariable("projectId") String projectId, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        logRequest("企业提交项目审核", userId, request);

        boolean result = projectService.submitForReview(projectId, userId);

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    @GetMapping("published")
    @Override
    public Response<ProjectListResponseDTO> getPublishedProjects(@RequestParam(value = "categoryId", required = false) String categoryId,
                                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                                  HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        logRequest("学生浏览已发布项目列表", userId, request);

        List<ProjectEntity> projects = projectService.getPublishedProjects(categoryId, pageNum, pageSize);
        Long total = projectService.countPublishedProjects(categoryId);

        List<ProjectListItemResponseDTO> projectDTOs = new ArrayList<>();
        for (ProjectEntity project : projects) {
            projectDTOs.add(convertToListItemDTO(project));
        }

        ProjectListResponseDTO listDTO = ProjectListResponseDTO.builder()
                .projects(projectDTOs)
                .total(total)
                .pageNum(pageNum)
                .pageSize(pageSize)
                .build();

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), listDTO);
    }

    @PostMapping("search")
    @Override
    public Response<ProjectListResponseDTO> searchProjects(@RequestBody ProjectSearchRequestDTO dto, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        logRequest("学生搜索项目", userId, request);

        Integer pageNum = dto.getPageNum() != null ? dto.getPageNum() : 1;
        Integer pageSize = dto.getPageSize() != null ? dto.getPageSize() : 10;

        List<ProjectEntity> projects = projectService.searchProjects(
                dto.getKeyword(),
                dto.getCategoryId(),
                dto.getSkillLevel(),
                dto.getBudgetMin(),
                dto.getBudgetMax(),
                pageNum,
                pageSize
        );

        Long total = projectService.countSearchResults(
                dto.getKeyword(),
                dto.getCategoryId(),
                dto.getSkillLevel(),
                dto.getBudgetMin(),
                dto.getBudgetMax()
        );

        List<ProjectListItemResponseDTO> projectDTOs = new ArrayList<>();
        for (ProjectEntity project : projects) {
            projectDTOs.add(convertToListItemDTO(project));
        }

        ProjectListResponseDTO listDTO = ProjectListResponseDTO.builder()
                .projects(projectDTOs)
                .total(total)
                .pageNum(pageNum)
                .pageSize(pageSize)
                .build();

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), listDTO);
    }

    @GetMapping("popular")
    @Override
    public Response<ProjectListResponseDTO> getPopularProjects(@RequestParam(value = "limit", defaultValue = "10") Integer limit, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        logRequest("获取热门项目列表", userId, request);

        List<ProjectEntity> projects = projectService.getPopularProjects(limit);

        List<ProjectListItemResponseDTO> projectDTOs = new ArrayList<>();
        for (ProjectEntity project : projects) {
            projectDTOs.add(convertToListItemDTO(project));
        }

        ProjectListResponseDTO listDTO = ProjectListResponseDTO.builder()
                .projects(projectDTOs)
                .total((long) projectDTOs.size())
                .pageNum(1)
                .pageSize(limit)
                .build();

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), listDTO);
    }

    @GetMapping("latest")
    @Override
    public Response<ProjectListResponseDTO> getLatestProjects(@RequestParam(value = "limit", defaultValue = "10") Integer limit, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        logRequest("获取最新项目列表", userId, request);

        List<ProjectEntity> projects = projectService.getLatestProjects(limit);

        List<ProjectListItemResponseDTO> projectDTOs = new ArrayList<>();
        for (ProjectEntity project : projects) {
            projectDTOs.add(convertToListItemDTO(project));
        }

        ProjectListResponseDTO listDTO = ProjectListResponseDTO.builder()
                .projects(projectDTOs)
                .total((long) projectDTOs.size())
                .pageNum(1)
                .pageSize(limit)
                .build();

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), listDTO);
    }

    @GetMapping("admin/pending_review")
    @Override
    public Response<ProjectListResponseDTO> getPendingReviewProjects(
            @RequestParam(value = "status", defaultValue = "pending_review") String status,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        // status 传 "all" 时查全部
        String queryStatus = "all".equals(status) ? null : status;
        logRequest("管理员获取项目列表 status=" + status, userId, request);

        List<ProjectEntity> projects = projectService.getProjectsByStatus(queryStatus, pageNum, pageSize);
        Long total = projectService.countProjectsByStatus(queryStatus);

        List<ProjectListItemResponseDTO> projectDTOs = new ArrayList<>();
        for (ProjectEntity project : projects) {
            projectDTOs.add(convertToListItemDTO(project));
        }

        ProjectListResponseDTO listDTO = ProjectListResponseDTO.builder()
                .projects(projectDTOs)
                .total(total)
                .pageNum(pageNum)
                .pageSize(pageSize)
                .build();

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), listDTO);
    }

    @GetMapping("admin/detail/{projectId}")
    @Override
    public Response<ProjectDetailResponseDTO> getProjectReviewDetail(
            @PathVariable("projectId") String projectId, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        logRequest("管理员查看项目审核详情", userId, request);

        ProjectEntity projectEntity = projectService.getProjectDetail(projectId);
        if (projectEntity == null) {
            throw new AppException(ResponseCode.UN_ERROR.getCode(), "项目不存在");
        }
        ProjectDetailResponseDTO dto = convertToDetailDTO(projectEntity);
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), dto);
    }

    @PostMapping("admin/approve/{projectId}")
    @Override
    public Response<Boolean> approveProject(@PathVariable("projectId") String projectId, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        logRequest("管理员审核通过项目", userId, request);

        boolean result = projectService.approveProject(projectId);
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    @PostMapping("admin/reject")
    @Override
    public Response<Boolean> rejectProject(@RequestBody ProjectRejectRequestDTO dto, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        logRequest("管理员拒绝项目", userId, request);

        boolean result = projectService.rejectProject(dto.getProjectId(), dto.getRejectReason());
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    private ProjectComment buildProjectComment(ProjectCreateRequestDTO dto, String userId) {
        return ProjectComment.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .categoryId(dto.getCategoryId())
                .tags(dto.getTags())
                .requirements(dto.getRequirements())
                .skillLevel(dto.getSkillLevel())
                .teamSizeMin(dto.getTeamSizeMin())
                .teamSizeMax(dto.getTeamSizeMax())
                .durationDays(dto.getDurationDays())
                .budgetType(dto.getBudgetType())
                .budgetAmount(dto.getBudgetAmount())
                .currency(dto.getCurrency())
                .publishedAt(dto.getPublishedAt())
                .applicationDeadline(dto.getApplicationDeadline())
                .expectedStartDate(dto.getExpectedStartDate())
                .expectedEndDate(dto.getExpectedEndDate())
                .publisherId(userId)
                .build();
    }

    private ProjectDetailResponseDTO convertToDetailDTO(ProjectEntity entity) {
        return ProjectDetailResponseDTO.builder()
                .projectId(entity.getProjectId())
                .serialNumber(entity.getSerialNumber())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .categoryId(entity.getCategoryId())
                .tags(entity.getTags())
                .requirements(entity.getRequirements())
                .skillLevel(entity.getSkillLevel())
                .teamSizeMin(entity.getTeamSizeMin())
                .teamSizeMax(entity.getTeamSizeMax())
                .durationDays(entity.getDurationDays())
                .budgetType(entity.getBudgetType())
                .budgetAmount(entity.getBudgetAmount())
                .currency(entity.getCurrency())
                .publishedAt(entity.getPublishedAt())
                .applicationDeadline(entity.getApplicationDeadline())
                .expectedStartDate(entity.getExpectedStartDate())
                .expectedEndDate(entity.getExpectedEndDate())
                .status(entity.getStatus())
                .rejectReason(entity.getRejectReason())
                .progress(entity.getProgress())
                .publisherId(entity.getPublisherId())
                .acceptedTeamId(entity.getAcceptedTeamId())
                .applicationCount(entity.getApplicationCount())
                .viewCount(entity.getViewCount())
                .favoriteCount(entity.getFavoriteCount())
                .publisherName(entity.getPublisherName())
                .acceptedTeamName(entity.getAcceptedTeamName())
                .build();
    }

    private ProjectListItemResponseDTO convertToListItemDTO(ProjectEntity entity) {
        return convertToListItemDTO(entity, false);
    }

    private ProjectListItemResponseDTO convertToListItemDTO(ProjectEntity entity, boolean includeCurrentMilestone) {
        ProjectListItemResponseDTO.ProjectListItemResponseDTOBuilder builder = ProjectListItemResponseDTO.builder()
                .projectId(entity.getProjectId())
                .serialNumber(entity.getSerialNumber())
                .title(entity.getTitle())
                .applicationCount(entity.getApplicationCount())
                .acceptedTeamId(entity.getAcceptedTeamId())
                .acceptedTeamName(resolveAcceptedTeamName(entity))
                .status(entity.getStatus())
                .budgetAmount(entity.getBudgetAmount())
                .currency(entity.getCurrency())
                .publisherName(entity.getPublisherName())
                .viewCount(entity.getViewCount())
                .expectedEndDate(entity.getExpectedEndDate())
                .submittedAt(entity.getSubmittedAt());

        if (includeCurrentMilestone && StrUtil.isNotBlank(entity.getProjectId())) {
            ProjectMilestoneEntity currentMilestone = milestoneService.getCurrentMilestoneByProjectId(entity.getProjectId());
            builder.currentMilestone(currentMilestone != null ? currentMilestone.getName() : null);
        }

        return builder.build();
    }

    private String resolveAcceptedTeamName(ProjectEntity entity) {
        if (entity == null || StrUtil.isBlank(entity.getAcceptedTeamId())) {
            return null;
        }
        if (StrUtil.isNotBlank(entity.getAcceptedTeamName())) {
            return entity.getAcceptedTeamName();
        }
        return teamRepository.queryByTeamId(entity.getAcceptedTeamId())
                .map(cn.hgy.domain.team.model.entity.TeamEntity::getTeamName)
                .orElse(null);
    }

    @PostMapping("admin/category/create")
    @Override
    public Response<Boolean> createProjectCategory(@RequestBody ProjectCategoryCreateRequestDTO dto, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        logRequest("管理员创建项目分类", userId, request);

        boolean result = projectService.createProjectCategory(
                dto.getName(),
                dto.getParentId(),
                dto.getDescription(),
                dto.getIcon(),
                dto.getSortOrder()
        );

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    @PostMapping("admin/category/update")
    @Override
    public Response<Boolean> updateProjectCategory(@RequestBody ProjectCategoryUpdateRequestDTO dto, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        logRequest("管理员更新项目分类", userId, request);

        boolean result = projectService.updateProjectCategory(
                dto.getProjectCategoryId(),
                dto.getName(),
                dto.getParentId(),
                dto.getDescription(),
                dto.getIcon(),
                dto.getSortOrder(),
                dto.getIsVisible(),
                dto.getIsActive()
        );

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    @DeleteMapping("admin/category/{categoryId}")
    @Override
    public Response<Boolean> deleteProjectCategory(@PathVariable("categoryId") String categoryId, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        logRequest("管理员删除项目分类", userId, request);

        boolean result = projectService.deleteProjectCategory(categoryId);

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    @GetMapping("category/all")
    @Override
    public Response<List<ProjectCategoryResponseDTO>> getAllCategories(HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        logRequest("查询所有项目分类", userId, request);

        List<ProjectCategoryEntity> categories = projectService.getAllCategories();
        List<ProjectCategoryResponseDTO> categoryDTOs = new ArrayList<>();
        for (ProjectCategoryEntity category : categories) {
            categoryDTOs.add(convertToCategoryDTO(category));
        }

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), categoryDTOs);
    }

    @GetMapping("category/visible")
    @Override
    public Response<List<ProjectCategoryResponseDTO>> getVisibleCategories(HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        logRequest("查询可见的项目分类", userId, request);

        List<ProjectCategoryEntity> categories = projectService.getVisibleCategories();
        List<ProjectCategoryResponseDTO> categoryDTOs = new ArrayList<>();
        for (ProjectCategoryEntity category : categories) {
            categoryDTOs.add(convertToCategoryDTO(category));
        }

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), categoryDTOs);
    }

    @GetMapping("category/{categoryId}")
    @Override
    public Response<ProjectCategoryResponseDTO> getCategoryById(@PathVariable("categoryId") String categoryId, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        logRequest("查询项目分类详情", userId, request);

        ProjectCategoryEntity category = projectService.getCategoryById(categoryId);
        ProjectCategoryResponseDTO categoryDTO = convertToCategoryDTO(category);

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), categoryDTO);
    }

    private ProjectCategoryResponseDTO convertToCategoryDTO(ProjectCategoryEntity entity) {
        return ProjectCategoryResponseDTO.builder()
                .projectCategoryId(entity.getProjectCategoryId())
                .name(entity.getName())
                .parentId(entity.getParentId())
                .description(entity.getDescription())
                .icon(entity.getIcon())
                .sortOrder(entity.getSortOrder())
                .isVisible(entity.getIsVisible())
                .isActive(entity.getIsActive())
                .build();
    }

    @PostMapping("publish_draft/{projectId}")
    @Override
    public Response<Boolean> publishDraftDirectly(@PathVariable("projectId") String projectId, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        logRequest("企业直接发布草稿项目", userId, request);

        // 校验企业认证状态
        checkCompanyCertified(userId);

        boolean result = projectService.publishDraftDirectly(projectId, userId);

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    @PostMapping("close/{projectId}")
    @Override
    public Response<Boolean> closeProject(@PathVariable("projectId") String projectId, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        logRequest("企业手动完结项目", userId, request);

        checkCompanyCertified(userId);

        boolean result = projectService.closeProject(projectId, userId);
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    @GetMapping("my_accepted_projects")
    @Override
    public Response<ProjectListResponseDTO> getMyAcceptedProjects(@RequestParam(value = "status", required = false) String status,
                                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                                   HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        logRequest("学生查询自己承接的项目列表", userId, request);

        String teamId = getTeamIdByUserId(userId);
        
        if (StrUtil.isBlank(teamId)) {
            ProjectListResponseDTO emptyList = ProjectListResponseDTO.builder()
                    .projects(new ArrayList<>())
                    .total(0L)
                    .pageNum(pageNum)
                    .pageSize(pageSize)
                    .build();
            return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), emptyList);
        }

        List<ProjectEntity> projects = projectService.getProjectsByAcceptedTeam(teamId, status, pageNum, pageSize);
        Long total = projectService.countProjectsByAcceptedTeam(teamId, status);

        List<ProjectListItemResponseDTO> projectDTOs = new ArrayList<>();
        for (ProjectEntity project : projects) {
            projectDTOs.add(convertToListItemDTO(project, true));
        }

        ProjectListResponseDTO listDTO = ProjectListResponseDTO.builder()
                .projects(projectDTOs)
                .total(total)
                .pageNum(pageNum)
                .pageSize(pageSize)
                .build();

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), listDTO);
    }

    private String getTeamIdByUserId(String userId) {
        try {
            java.util.Optional<cn.hgy.domain.team.model.entity.TeamEntity> teamOpt = teamRepository.queryActiveTeamByUserId(userId);
            if (teamOpt.isPresent()) {
                return teamOpt.get().getTeamId();
            }
        } catch (Exception e) {
            log.warn("查询用户团队失败 userId={}", userId, e);
        }
        return null;
    }

    private void logRequest(String action, String userId, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StrUtil.isNotBlank(token) && StrUtil.isNotBlank(userId)) {
            Integer userType = jwtService.parseUserType(token);
            log.info("{} userId={} userType={}", action, userId, userType);
        } else {
            log.info("{}", action);
        }
    }

    /**
     * 校验企业是否已通过认证，未认证则抛出异常
     */
    private void checkCompanyCertified(String userId) {
        CompanyProfileEntity profile = userManagementRepository.queryCompanyProfileByUserId(userId);
        if (profile == null || !CertificationStatusEnum.CERTIFIED.getCode().equals(profile.getCertificationStatus())) {
            throw new AppException(ResponseCode.UN_ERROR.getCode(), "企业未通过资质认证，无法操作项目");
        }
    }
}
