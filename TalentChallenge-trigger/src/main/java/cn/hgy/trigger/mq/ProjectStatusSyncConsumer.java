package cn.hgy.trigger.mq;

import cn.hgy.domain.bid.repository.IBidRepository;
import cn.hgy.domain.bid.model.valobj.BidStatusEnum;
import cn.hgy.domain.project.model.valobj.ProjectStatusEnum;
import cn.hgy.domain.project.repository.IProjectRepository;
import cn.hgy.domain.project.service.IMilestoneService;
import cn.hgy.domain.project.service.IMilestoneTemplateService;
import cn.hgy.types.event.BidConfirmedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import cn.hgy.domain.project.model.valobj.MilestoneStatusEnum;
import cn.hgy.domain.project.model.entity.ProjectMilestoneEntity;

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
            log.info("开始处理项目状态同步 bidId={}, projectId={}, teamId={}",
                    event.getBidId(), event.getProjectId(), event.getTeamId());
            
            projectRepository.updateAcceptedTeam(event.getProjectId(), event.getTeamId());
            
            projectRepository.updateProjectStatus(
                    event.getProjectId(),
                    ProjectStatusEnum.IN_PROGRESS.getStatusCode()
            );
            
            // 项目进入进行中状态后，通过模板服务初始化必须里程碑
            templateService.initializeRequiredMilestones(event.getProjectId());
            
            List<String> otherBidIds = bidRepository.findOtherActiveBidIds(
                    event.getProjectId(), event.getBidId());
            if (!otherBidIds.isEmpty()) {
                bidRepository.batchUpdateStatus(otherBidIds, BidStatusEnum.REJECTED);
                log.info("已拒绝其他竞榜申请 projectId={}, count={}",
                        event.getProjectId(), otherBidIds.size());
            }
            
            log.info("项目状态同步完成 projectId={}, acceptedTeamId={}",
                    event.getProjectId(), event.getTeamId());
                    
        } catch (Exception e) {
            log.error("项目状态同步失败 bidId={}, projectId={}",
                    event.getBidId(), event.getProjectId(), e);
            throw e;
        }
    }
}

