package cn.hgy.trigger.mq;

import cn.hgy.domain.bid.model.valobj.BidStatusEnum;
import cn.hgy.domain.bid.repository.IBidRepository;
import cn.hgy.domain.project.model.valobj.ProjectStatusEnum;
import cn.hgy.domain.project.repository.IProjectRepository;
import cn.hgy.domain.project.service.IMilestoneService;
import cn.hgy.domain.project.service.IMilestoneTemplateService;
import cn.hgy.types.event.BidConfirmedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectStatusSyncConsumerTest {

    @Mock
    private IProjectRepository projectRepository;

    @Mock
    private IBidRepository bidRepository;

    @Mock
    private IMilestoneService milestoneService;

    @Mock
    private IMilestoneTemplateService templateService;

    @InjectMocks
    private ProjectStatusSyncConsumer consumer;

    @Test
    void shouldInitializeAndStartInitialMilestoneAfterBidConfirmed() {
        BidConfirmedEvent event = BidConfirmedEvent.builder()
                .bidId("bid-1")
                .projectId("project-1")
                .teamId("team-1")
                .operatorId("admin-1")
                .confirmedAt(LocalDateTime.now())
                .build();

        when(bidRepository.findOtherActiveBidIds("project-1", "bid-1"))
                .thenReturn(List.of("bid-2", "bid-3"));

        consumer.handleBidConfirmed(event);

        verify(projectRepository).updateAcceptedTeam("project-1", "team-1");
        verify(projectRepository).updateProjectStatus("project-1", ProjectStatusEnum.IN_PROGRESS.getStatusCode());
        verify(templateService).initializeRequiredMilestones("project-1");
        verify(milestoneService).startInitialMilestone("project-1");
        verify(bidRepository).batchUpdateStatus(List.of("bid-2", "bid-3"), BidStatusEnum.REJECTED);
    }
}
