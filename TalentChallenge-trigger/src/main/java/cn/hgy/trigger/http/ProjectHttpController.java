package cn.hgy.trigger.http;

import cn.hgy.api.IProjectHttpService;
import cn.hgy.api.dto.ProjectCreateRequestDTO;
import cn.hgy.api.dto.ProjectDetailResponseDTO;
import cn.hgy.api.dto.ProjectListItemResponseDTO;
import cn.hgy.api.dto.ProjectListResponseDTO;
import cn.hgy.api.dto.ProjectRejectRequestDTO;
import cn.hgy.api.dto.ProjectSearchRequestDTO;
import cn.hgy.api.dto.ProjectUpdateRequestDTO;
import cn.hgy.api.response.Response;
import cn.hgy.domain.project.model.entity.ProjectEntity;
import cn.hgy.domain.project.service.IProjectService;
import cn.hgy.trigger.interceptor.UserContextDynamic;
import cn.hgy.types.common.ProjectComment;
import cn.hgy.types.enums.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-10-07
 * @Description: 项目业务控制器
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/project/")
public class ProjectHttpController implements IProjectHttpService {

    private final IProjectService projectService;

    @PostMapping("create_project")
    @Override
    public Response<Boolean> createProject(@RequestBody ProjectCreateRequestDTO dto) {
        String userId = UserContextDynamic.getUserContextDynamic();

        ProjectComment projectComment = buildProjectComment(dto, userId);
        boolean result = projectService.createProject(projectComment);

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    @PostMapping("save_draft")
    @Override
    public Response<Boolean> saveDraft(@RequestBody ProjectCreateRequestDTO dto) {
        String userId = UserContextDynamic.getUserContextDynamic();

        ProjectComment projectComment = buildProjectComment(dto, userId);
        boolean result = projectService.saveDraft(projectComment);

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    @PostMapping("update_project")
    @Override
    public Response<Boolean> updateProject(@RequestBody ProjectUpdateRequestDTO dto) {
        String userId = UserContextDynamic.getUserContextDynamic();

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
    public Response<ProjectDetailResponseDTO> getProjectDetail(@PathVariable("projectId") String projectId) {
        ProjectEntity projectEntity = projectService.getProjectDetail(projectId);
        projectService.incrementViewCount(projectId);
        ProjectDetailResponseDTO dto = convertToDetailDTO(projectEntity);

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), dto);
    }

    @GetMapping("my_projects")
    @Override
    public Response<ProjectListResponseDTO> getMyProjects(@RequestParam(value = "status", required = false) String status, 
                                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, 
                                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        String userId = UserContextDynamic.getUserContextDynamic();

        List<ProjectEntity> projects = projectService.getProjectsByPublisher(userId, status, pageNum, pageSize);
        Long total = projectService.countProjectsByPublisher(userId, status);

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

    @PostMapping("submit_review/{projectId}")
    @Override
    public Response<Boolean> submitForReview(@PathVariable("projectId") String projectId) {
        String userId = UserContextDynamic.getUserContextDynamic();

        boolean result = projectService.submitForReview(projectId, userId);

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    @GetMapping("published")
    @Override
    public Response<ProjectListResponseDTO> getPublishedProjects(@RequestParam(value = "categoryId", required = false) String categoryId,
                                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
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
    public Response<ProjectListResponseDTO> searchProjects(@RequestBody ProjectSearchRequestDTO dto) {
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
    public Response<ProjectListResponseDTO> getPopularProjects(@RequestParam(value = "limit", defaultValue = "10") Integer limit) {
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
    public Response<ProjectListResponseDTO> getLatestProjects(@RequestParam(value = "limit", defaultValue = "10") Integer limit) {
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
    public Response<ProjectListResponseDTO> getPendingReviewProjects(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        List<ProjectEntity> projects = projectService.getProjectsByStatus("pending_review", pageNum, pageSize);
        Long total = projectService.countProjectsByStatus("pending_review");

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

    @PostMapping("admin/approve/{projectId}")
    @Override
    public Response<Boolean> approveProject(@PathVariable("projectId") String projectId) {
        boolean result = projectService.approveProject(projectId);
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    @PostMapping("admin/reject")
    @Override
    public Response<Boolean> rejectProject(@RequestBody ProjectRejectRequestDTO dto) {
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
                .build();
    }

    private ProjectListItemResponseDTO convertToListItemDTO(ProjectEntity entity) {
        return ProjectListItemResponseDTO.builder()
                .projectId(entity.getProjectId())
                .serialNumber(entity.getSerialNumber())
                .title(entity.getTitle())
                .applicationCount(entity.getApplicationCount())
                .acceptedTeamId(entity.getAcceptedTeamId())
                .status(entity.getStatus())
                .budgetAmount(entity.getBudgetAmount())
                .currency(entity.getCurrency())
                .build();
    }
}
