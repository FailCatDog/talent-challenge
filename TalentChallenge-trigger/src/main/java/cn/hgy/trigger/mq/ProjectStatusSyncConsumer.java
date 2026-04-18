package cn.hgy.trigger.mq;

import cn.hgy.domain.bid.model.valobj.BidStatusEnum;
import cn.hgy.domain.bid.repository.IBidRepository;
import cn.hgy.domain.project.model.valobj.ProjectStatusEnum;
import cn.hgy.domain.project.repository.IProjectRepository;
import cn.hgy.domain.project.service.IMilestoneService;
import cn.hgy.domain.project.service.IMilestoneTemplateService;
import cn.hgy.types.event.BidConfirmedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectStatusSyncConsumer {

    private static final String PROJECT_SYNC_QUEUE = "bid.confirmed.project.sync";

    private final IProjectRepository projectRepository;
    private final IBidRepository bidRepository;
    private final IMilestoneService milestoneService;
    private final IMilestoneTemplateService templateService;

    @RabbitListener(queues = PROJECT_SYNC_QUEUE)
    public void handleBidConfirmed(BidConfirmedEvent event) {
        try {
            log.info("同步中标项目状态 bidId={}, projectId={}, teamId={}",
                    event.getBidId(), event.getProjectId(), event.getTeamId());

            projectRepository.updateAcceptedTeam(event.getProjectId(), event.getTeamId());
            projectRepository.updateProjectStatus(
                    event.getProjectId(),
                    ProjectStatusEnum.IN_PROGRESS.getStatusCode()
            );

            // 项目进入进行中后先初始化必选里程碑，再自动启动首个可执行节点。
            templateService.initializeRequiredMilestones(event.getProjectId());
            milestoneService.startInitialMilestone(event.getProjectId());

            List<String> otherBidIds = bidRepository.findOtherActiveBidIds(
                    event.getProjectId(), event.getBidId());
            if (!otherBidIds.isEmpty()) {
                bidRepository.batchUpdateStatus(otherBidIds, BidStatusEnum.REJECTED);
                log.info("批量拒绝其他活跃揭榜 projectId={}, count={}",
                        event.getProjectId(), otherBidIds.size());
            }

            log.info("项目同步完成 projectId={}, acceptedTeamId={}",
                    event.getProjectId(), event.getTeamId());
        } catch (Exception e) {
            log.error("项目同步失败 bidId={}, projectId={}",
                    event.getBidId(), event.getProjectId(), e);
            throw e;
        }
    }
}
