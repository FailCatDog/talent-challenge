package cn.hgy.domain.project.service;

import cn.hgy.domain.audit.repository.IAuditReviewRepository;
import cn.hgy.domain.project.model.entity.ProjectEntity;
import cn.hgy.domain.project.repository.IProjectRepository;
import cn.hgy.types.common.ProjectComment;
import cn.hgy.types.enums.ResponseCode;
import cn.hgy.types.exception.AppException;
import cn.hutool.core.lang.Snowflake;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private Snowflake snowflake;

    @Mock
    private IProjectRepository repository;

    @Mock
    private IMilestoneTemplateService milestoneTemplateService;

    @Mock
    private IMilestoneService milestoneService;

    @Mock
    private IAuditReviewRepository auditReviewRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void test_CreateProjectSuccessfullyWithValidData() {
        log.info("开始测试：使用有效数据成功创建项目");

        String projectId = "1234567890123456789";
        ProjectComment projectComment = buildValidProjectComment();
        log.info("测试数据：title={}, categoryId={}, publisherId={}", 
                projectComment.getTitle(), projectComment.getCategoryId(), projectComment.getPublisherId());

        when(snowflake.nextIdStr()).thenReturn(projectId);
        when(milestoneTemplateService.initializeRequiredMilestones(projectId)).thenReturn(true);
        log.info("已设置Mock：雪花算法生成项目ID={}，里程碑初始化返回true", projectId);

        Boolean result = projectService.createProject(projectComment);

        assertThat(result).isTrue();
        log.info("验证项目创建结果为true");

        verify(snowflake, times(1)).nextIdStr();
        verify(repository, times(1)).createProject(argThat(projectEntity -> {
            log.info("验证传递给repository的项目实体属性");
            assertThat(projectEntity.getProjectId()).isEqualTo(projectId);
            assertThat(projectEntity.getSerialNumber()).startsWith("PRJ");
            assertThat(projectEntity.getTitle()).isEqualTo(projectComment.getTitle());
            assertThat(projectEntity.getDescription()).isEqualTo(projectComment.getDescription());
            assertThat(projectEntity.getCategoryId()).isEqualTo(projectComment.getCategoryId());
            assertThat(projectEntity.getTags()).isEqualTo(projectComment.getTags());
            assertThat(projectEntity.getRequirements()).isEqualTo(projectComment.getRequirements());
            assertThat(projectEntity.getSkillLevel()).isEqualTo(projectComment.getSkillLevel());
            assertThat(projectEntity.getTeamSizeMin()).isEqualTo(projectComment.getTeamSizeMin());
            assertThat(projectEntity.getTeamSizeMax()).isEqualTo(projectComment.getTeamSizeMax());
            assertThat(projectEntity.getDurationDays()).isEqualTo(projectComment.getDurationDays());
            assertThat(projectEntity.getBudgetType()).isEqualTo(projectComment.getBudgetType());
            assertThat(projectEntity.getBudgetAmount()).isEqualTo(projectComment.getBudgetAmount());
            assertThat(projectEntity.getCurrency()).isEqualTo(projectComment.getCurrency());
            assertThat(projectEntity.getPublishedAt()).isEqualTo(projectComment.getPublishedAt());
            assertThat(projectEntity.getApplicationDeadline()).isEqualTo(projectComment.getApplicationDeadline());
            assertThat(projectEntity.getExpectedStartDate()).isEqualTo(projectComment.getExpectedStartDate());
            assertThat(projectEntity.getExpectedEndDate()).isEqualTo(projectComment.getExpectedEndDate());
            assertThat(projectEntity.getStatus()).isEqualTo("draft");
            assertThat(projectEntity.getPublisherId()).isEqualTo(projectComment.getPublisherId());
            return true;
        }));
        verify(milestoneTemplateService, times(1)).initializeRequiredMilestones(projectId);
        log.info("验证里程碑初始化方法被调用一次");

        log.info("测试完成：项目创建成功测试通过");
    }

    @Test
    void test_ThrowProjectCreateFailExceptionWhenRepositoryThrowsException() {
        log.info("开始测试：当仓库层抛出异常时应抛出项目创建失败异常");

        String projectId = "1234567890123456789";
        ProjectComment projectComment = buildValidProjectComment();
        log.info("测试数据：title={}, publisherId={}", projectComment.getTitle(), projectComment.getPublisherId());

        when(snowflake.nextIdStr()).thenReturn(projectId);
        doThrow(new RuntimeException("Database connection failed"))
                .when(repository).createProject(any(ProjectEntity.class));
        log.info("已设置Mock：雪花算法生成ID成功，但仓库插入操作会抛出运行时异常");

        AppException exception = assertThrows(AppException.class, () -> {
            projectService.createProject(projectComment);
        });

        assertThat(exception.getCode()).isEqualTo(ResponseCode.PROJECT_CREATE_FAIL.getCode());
        assertThat(exception.getInfo()).isEqualTo(ResponseCode.PROJECT_CREATE_FAIL.getInfo());
        log.info("验证异常代码和信息符合预期");

        verify(snowflake, times(1)).nextIdStr();
        verify(repository, times(1)).createProject(any(ProjectEntity.class));
        verify(milestoneTemplateService, never()).initializeRequiredMilestones(anyString());
        log.info("验证里程碑初始化方法未被调用");

        log.info("测试完成：项目创建失败异常测试通过");
    }

    @Test
    void test_CreateProjectWithMinimalRequiredData() {
        log.info("开始测试：使用最少必需数据创建项目");

        String projectId = "9876543210987654321";
        ProjectComment projectComment = buildMinimalProjectComment();
        log.info("测试数据：title={}, publisherId={}", projectComment.getTitle(), projectComment.getPublisherId());

        when(snowflake.nextIdStr()).thenReturn(projectId);
        when(milestoneTemplateService.initializeRequiredMilestones(projectId)).thenReturn(true);

        Boolean result = projectService.createProject(projectComment);

        assertThat(result).isTrue();
        log.info("验证项目创建结果为true");

        verify(repository, times(1)).createProject(argThat(projectEntity -> {
            log.info("验证最少数据项目实体属性");
            assertThat(projectEntity.getProjectId()).isEqualTo(projectId);
            assertThat(projectEntity.getSerialNumber()).startsWith("PRJ");
            assertThat(projectEntity.getTitle()).isEqualTo(projectComment.getTitle());
            assertThat(projectEntity.getPublisherId()).isEqualTo(projectComment.getPublisherId());
            return true;
        }));
        verify(milestoneTemplateService, times(1)).initializeRequiredMilestones(projectId);

        log.info("测试完成：最少数据项目创建测试通过");
    }

    @Test
    void test_CreateProjectWithDifferentBudgetTypes() {
        log.info("开始测试：使用不同预算类型创建项目");

        String[] budgetTypes = {"固定总价", "按时计费", "按里程碑支付"};
        BigDecimal[] budgetAmounts = {new BigDecimal("10000.00"), new BigDecimal("500.00"), new BigDecimal("25000.00")};
        String[] currencies = {"CNY", "USD", "EUR"};

        for (int i = 0; i < budgetTypes.length; i++) {
            final String expectedBudgetType = budgetTypes[i];
            final BigDecimal expectedBudgetAmount = budgetAmounts[i];
            final String expectedCurrency = currencies[i];

            String projectId = "budget_test_" + i;
            ProjectComment projectComment = buildValidProjectComment();
            projectComment.setBudgetType(expectedBudgetType);
            projectComment.setBudgetAmount(expectedBudgetAmount);
            projectComment.setCurrency(expectedCurrency);

            when(snowflake.nextIdStr()).thenReturn(projectId);
            when(milestoneTemplateService.initializeRequiredMilestones(projectId)).thenReturn(true);

            Boolean result = projectService.createProject(projectComment);

            assertThat(result).isTrue();
            log.info("验证预算类型 {} 项目创建成功", expectedBudgetType);

            verify(repository).createProject(argThat(projectEntity -> {
                assertThat(projectEntity.getBudgetType()).isEqualTo(expectedBudgetType);
                assertThat(projectEntity.getBudgetAmount()).isEqualTo(expectedBudgetAmount);
                assertThat(projectEntity.getCurrency()).isEqualTo(expectedCurrency);
                return true;
            }));
            verify(milestoneTemplateService).initializeRequiredMilestones(projectId);

            reset(snowflake, repository, milestoneTemplateService);
        }

        log.info("测试完成：不同预算类型项目创建测试通过");
    }

    @Test
    void test_CreateProjectWithDifferentTeamSizes() {
        log.info("开始测试：使用不同团队规模创建项目");

        Integer[][] teamSizes = {{1, 3}, {5, 10}, {10, 20}};
        String[] skillLevels = {"初级", "中级", "高级"};

        for (int i = 0; i < teamSizes.length; i++) {
            final Integer expectedTeamSizeMin = teamSizes[i][0];
            final Integer expectedTeamSizeMax = teamSizes[i][1];
            final String expectedSkillLevel = skillLevels[i];

            String projectId = "team_test_" + i;
            ProjectComment projectComment = buildValidProjectComment();
            projectComment.setTeamSizeMin(expectedTeamSizeMin);
            projectComment.setTeamSizeMax(expectedTeamSizeMax);
            projectComment.setSkillLevel(expectedSkillLevel);

            when(snowflake.nextIdStr()).thenReturn(projectId);
            when(milestoneTemplateService.initializeRequiredMilestones(projectId)).thenReturn(true);

            Boolean result = projectService.createProject(projectComment);

            assertThat(result).isTrue();
            log.info("验证团队规模 {}-{} 人，技能等级 {} 项目创建成功",
                    expectedTeamSizeMin, expectedTeamSizeMax, expectedSkillLevel);

            verify(repository).createProject(argThat(projectEntity -> {
                assertThat(projectEntity.getTeamSizeMin()).isEqualTo(expectedTeamSizeMin);
                assertThat(projectEntity.getTeamSizeMax()).isEqualTo(expectedTeamSizeMax);
                assertThat(projectEntity.getSkillLevel()).isEqualTo(expectedSkillLevel);
                return true;
            }));
            verify(milestoneTemplateService).initializeRequiredMilestones(projectId);

            reset(snowflake, repository, milestoneTemplateService);
        }

        log.info("测试完成：不同团队规模项目创建测试通过");
    }

    @Test
    void test_VerifyProjectIdAndSerialNumberAreSetCorrectly() {
        log.info("开始测试：验证项目ID和序列号设置正确");

        String expectedProjectId = "test_project_id_123456";
        ProjectComment projectComment = buildValidProjectComment();

        when(snowflake.nextIdStr()).thenReturn(expectedProjectId);
        when(milestoneTemplateService.initializeRequiredMilestones(expectedProjectId)).thenReturn(true);

        projectService.createProject(projectComment);

        verify(repository).createProject(argThat(projectEntity -> {
            log.info("验证项目ID和序列号设置");
            assertThat(projectEntity.getProjectId()).isEqualTo(expectedProjectId);
            assertThat(projectEntity.getSerialNumber()).startsWith("PRJ");
            log.info("项目ID和序列号均设置为：{}", expectedProjectId);
            return true;
        }));
        verify(milestoneTemplateService).initializeRequiredMilestones(expectedProjectId);

        log.info("测试完成：项目ID和序列号设置验证通过");
    }

    @Test
    void test_ThrowTitleInvalidExceptionWhenTitleTooShort() {
        log.info("开始测试：当项目标题过短时应抛出标题无效异常");

        ProjectComment projectComment = buildValidProjectComment();
        projectComment.setTitle("短");

        AppException exception = assertThrows(AppException.class, () -> {
            projectService.createProject(projectComment);
        });

        assertThat(exception.getCode()).isEqualTo(ResponseCode.PROJECT_TITLE_INVALID.getCode());
        assertThat(exception.getInfo()).isEqualTo(ResponseCode.PROJECT_TITLE_INVALID.getInfo());

        verify(repository, never()).createProject(any());

        log.info("测试完成：标题过短异常测试通过");
    }

    @Test
    void test_ThrowBudgetInvalidExceptionWhenBudgetIsZero() {
        log.info("开始测试：当预算金额为0时应抛出预算无效异常");

        ProjectComment projectComment = buildValidProjectComment();
        projectComment.setBudgetAmount(BigDecimal.ZERO);

        AppException exception = assertThrows(AppException.class, () -> {
            projectService.createProject(projectComment);
        });

        assertThat(exception.getCode()).isEqualTo(ResponseCode.PROJECT_BUDGET_INVALID.getCode());
        assertThat(exception.getInfo()).isEqualTo(ResponseCode.PROJECT_BUDGET_INVALID.getInfo());

        verify(repository, never()).createProject(any());

        log.info("测试完成：预算为0异常测试通过");
    }

    @Test
    void test_ThrowTeamSizeInvalidExceptionWhenMinGreaterThanMax() {
        log.info("开始测试：当最小团队人数大于最大人数时应抛出团队规模无效异常");

        ProjectComment projectComment = buildValidProjectComment();
        projectComment.setTeamSizeMin(10);
        projectComment.setTeamSizeMax(5);

        AppException exception = assertThrows(AppException.class, () -> {
            projectService.createProject(projectComment);
        });

        assertThat(exception.getCode()).isEqualTo(ResponseCode.PROJECT_TEAM_SIZE_INVALID.getCode());
        assertThat(exception.getInfo()).isEqualTo(ResponseCode.PROJECT_TEAM_SIZE_INVALID.getInfo());

        verify(repository, never()).createProject(any());

        log.info("测试完成：团队规模无效异常测试通过");
    }

    @Test
    void test_ThrowProjectCreateFailExceptionWhenMilestoneInitializationFails() {
        log.info("开始测试：当里程碑初始化失败时应抛出项目创建失败异常");

        String projectId = "1234567890123456789";
        ProjectComment projectComment = buildValidProjectComment();
        log.info("测试数据：title={}, publisherId={}", projectComment.getTitle(), projectComment.getPublisherId());

        when(snowflake.nextIdStr()).thenReturn(projectId);
        doThrow(new AppException(ResponseCode.MILESTONE_INIT_FAIL.getCode(), ResponseCode.MILESTONE_INIT_FAIL.getInfo()))
                .when(milestoneTemplateService).initializeRequiredMilestones(projectId);
        log.info("已设置Mock：项目创建成功，但里程碑初始化会抛出异常");

        AppException exception = assertThrows(AppException.class, () -> {
            projectService.createProject(projectComment);
        });

        assertThat(exception.getCode()).isEqualTo(ResponseCode.PROJECT_CREATE_FAIL.getCode());
        assertThat(exception.getInfo()).isEqualTo(ResponseCode.PROJECT_CREATE_FAIL.getInfo());
        log.info("验证异常代码和信息符合预期");

        verify(snowflake, times(1)).nextIdStr();
        verify(repository, times(1)).createProject(any(ProjectEntity.class));
        verify(milestoneTemplateService, times(1)).initializeRequiredMilestones(projectId);
        log.info("验证项目创建和里程碑初始化方法都被调用");

        log.info("测试完成：里程碑初始化失败异常测试通过");
    }

    @Test
    void shouldCloseProjectWhenPublisherClosesCompletedProject() {
        ProjectEntity projectEntity = ProjectEntity.builder()
                .projectId("p1")
                .publisherId("publisher-1")
                .status("completed")
                .build();

        when(repository.queryProjectById("p1")).thenReturn(projectEntity);

        boolean result = projectService.closeProject("p1", "publisher-1");

        assertThat(result).isTrue();
        verify(repository).updateProjectStatus("p1", "closed");
    }

    @Test
    void shouldAutoCloseCompletedProjectsOlderThanTwoWeeks() {
        ProjectEntity first = ProjectEntity.builder().projectId("p1").status("completed").build();
        ProjectEntity second = ProjectEntity.builder().projectId("p2").status("completed").build();

        when(repository.queryProjectsByStatusUpdatedBefore(eq("completed"), any(LocalDateTime.class)))
                .thenReturn(java.util.List.of(first, second));

        int closedCount = projectService.autoCloseCompletedProjects();

        assertThat(closedCount).isEqualTo(2);
        verify(repository).updateProjectStatus("p1", "closed");
        verify(repository).updateProjectStatus("p2", "closed");
    }

    @Test
    void shouldMarkProjectCompletedWhenProjectIsInProgress() {
        ProjectEntity projectEntity = ProjectEntity.builder()
                .projectId("p1")
                .status("in_progress")
                .build();

        when(repository.queryProjectById("p1")).thenReturn(projectEntity);

        boolean result = projectService.completeProject("p1");

        assertThat(result).isTrue();
        verify(repository).updateProjectStatus("p1", "completed");
    }

    @Test
    void shouldRecordAuditHistoryWhenAdminApprovesProject() {
        ProjectEntity projectEntity = ProjectEntity.builder()
                .projectId("p1")
                .status("pending_review")
                .build();

        when(repository.queryProjectById("p1")).thenReturn(projectEntity);

        boolean result = projectService.approveProject("p1", "admin-1");

        assertThat(result).isTrue();
        verify(repository).updateProjectStatusAndReason("p1", "published", null);
        verify(auditReviewRepository).save(argThat(record -> {
            assertThat(record.getAuditType()).isEqualTo("project");
            assertThat(record.getTargetId()).isEqualTo("p1");
            assertThat(record.getAction()).isEqualTo("approve");
            assertThat(record.getFromStatus()).isEqualTo("pending_review");
            assertThat(record.getToStatus()).isEqualTo("published");
            assertThat(record.getReviewerId()).isEqualTo("admin-1");
            return true;
        }));
    }

    private ProjectComment buildValidProjectComment() {
        return ProjectComment.builder()
                .title("测试项目标题")
                .description("这是一个测试项目的详细描述")
                .categoryId("CAT001")
                .tags("[\"Java\",\"Spring Boot\",\"MySQL\"]")
                .requirements("需要有3年以上开发经验")
                .skillLevel("中级")
                .teamSizeMin(3)
                .teamSizeMax(5)
                .durationDays(30)
                .budgetType("固定总价")
                .budgetAmount(new BigDecimal("15000.00"))
                .currency("CNY")
                .publishedAt(LocalDateTime.now())
                .applicationDeadline(LocalDateTime.now().plusDays(7))
                .expectedStartDate(LocalDate.now().plusDays(14))
                .expectedEndDate(LocalDate.now().plusDays(44))
                .publisherId("PUB001")
                .build();
    }

    private ProjectComment buildMinimalProjectComment() {
        return ProjectComment.builder()
                .title("最小测试项目")
                .publisherId("PUB002")
                .build();
    }
}
