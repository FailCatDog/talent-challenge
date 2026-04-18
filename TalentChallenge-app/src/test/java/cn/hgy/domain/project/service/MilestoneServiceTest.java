package cn.hgy.domain.project.service;

import cn.hgy.domain.project.model.entity.ProjectMilestoneEntity;
import cn.hgy.domain.project.model.valobj.MilestoneApprovalStatusEnum;
import cn.hgy.domain.project.model.valobj.MilestoneStatusEnum;
import cn.hgy.domain.project.repository.IMilestoneRepository;
import cn.hutool.core.lang.Snowflake;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MilestoneServiceTest {

    @Mock
    private IMilestoneRepository milestoneRepository;

    @Mock
    private Snowflake snowflake;

    @InjectMocks
    private MilestoneService milestoneService;

    @Test
    void shouldStartInitialPlannedMilestoneWhenProjectEntersInProgress() {
        ProjectMilestoneEntity firstMilestone = ProjectMilestoneEntity.builder()
                .milestoneId("m1")
                .projectId("p1")
                .sequenceOrder(1)
                .status(MilestoneStatusEnum.PLANNED.getStatusCode())
                .build();
        ProjectMilestoneEntity secondMilestone = ProjectMilestoneEntity.builder()
                .milestoneId("m2")
                .projectId("p1")
                .sequenceOrder(2)
                .status(MilestoneStatusEnum.PLANNED.getStatusCode())
                .build();

        when(milestoneRepository.queryMilestonesByProjectId("p1"))
                .thenReturn(List.of(firstMilestone, secondMilestone));

        boolean result = milestoneService.startInitialMilestone("p1");

        assertThat(result).isTrue();

        ArgumentCaptor<ProjectMilestoneEntity> captor = ArgumentCaptor.forClass(ProjectMilestoneEntity.class);
        verify(milestoneRepository).updateMilestone(captor.capture());

        ProjectMilestoneEntity updated = captor.getValue();
        assertThat(updated.getMilestoneId()).isEqualTo("m1");
        assertThat(updated.getStatus()).isEqualTo(MilestoneStatusEnum.IN_PROGRESS.getStatusCode());
        assertThat(updated.getApprovalStatus()).isEqualTo(MilestoneApprovalStatusEnum.PENDING.getStatusCode());
        assertThat(updated.getProgressPercent()).isEqualTo(0);
        assertThat(updated.getPlannedStartTime()).isNotNull();
        assertThat(updated.getActualStartTime()).isNotNull();
        assertThat(updated.getActualEndTime()).isNull();
    }

    @Test
    void shouldCompleteCurrentMilestoneAndStartNextWhenApprovalPasses() {
        ProjectMilestoneEntity currentMilestone = ProjectMilestoneEntity.builder()
                .milestoneId("m1")
                .projectId("p1")
                .sequenceOrder(1)
                .status(MilestoneStatusEnum.DELIVERED.getStatusCode())
                .approvalStatus(MilestoneApprovalStatusEnum.PENDING.getStatusCode())
                .build();
        ProjectMilestoneEntity nextMilestone = ProjectMilestoneEntity.builder()
                .milestoneId("m2")
                .projectId("p1")
                .sequenceOrder(2)
                .status(MilestoneStatusEnum.PLANNED.getStatusCode())
                .build();

        when(milestoneRepository.queryMilestoneById("m1")).thenReturn(currentMilestone);
        when(milestoneRepository.queryMilestonesByProjectId("p1"))
                .thenReturn(List.of(currentMilestone, nextMilestone));

        boolean result = milestoneService.approveMilestone("m1", "enterprise-1", true, "ok");

        assertThat(result).isTrue();

        ArgumentCaptor<ProjectMilestoneEntity> captor = ArgumentCaptor.forClass(ProjectMilestoneEntity.class);
        verify(milestoneRepository, times(2)).updateMilestone(captor.capture());

        List<ProjectMilestoneEntity> updatedEntities = captor.getAllValues();
        ProjectMilestoneEntity updatedCurrent = updatedEntities.get(0);
        ProjectMilestoneEntity updatedNext = updatedEntities.get(1);

        assertThat(updatedCurrent.getMilestoneId()).isEqualTo("m1");
        assertThat(updatedCurrent.getStatus()).isEqualTo(MilestoneStatusEnum.COMPLETED.getStatusCode());
        assertThat(updatedCurrent.getApprovalStatus()).isEqualTo(MilestoneApprovalStatusEnum.APPROVED.getStatusCode());
        assertThat(updatedCurrent.getProgressPercent()).isEqualTo(100);
        assertThat(updatedCurrent.getActualEndTime()).isNotNull();

        assertThat(updatedNext.getMilestoneId()).isEqualTo("m2");
        assertThat(updatedNext.getStatus()).isEqualTo(MilestoneStatusEnum.IN_PROGRESS.getStatusCode());
        assertThat(updatedNext.getApprovalStatus()).isEqualTo(MilestoneApprovalStatusEnum.PENDING.getStatusCode());
        assertThat(updatedNext.getProgressPercent()).isEqualTo(0);
        assertThat(updatedNext.getPlannedStartTime()).isNotNull();
        assertThat(updatedNext.getActualStartTime()).isNotNull();
        assertThat(updatedNext.getActualEndTime()).isNull();
    }
}
