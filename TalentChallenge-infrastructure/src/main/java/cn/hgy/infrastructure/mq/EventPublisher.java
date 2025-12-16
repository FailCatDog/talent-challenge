package cn.hgy.infrastructure.mq;

import cn.hgy.domain.bid.service.IBidEventPublisher;
import cn.hgy.types.event.BidConfirmedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventPublisher implements IBidEventPublisher {
    
    private static final String BID_EXCHANGE = "bid.exchange";
    private static final String ROUTING_KEY_BID_CONFIRMED = "bid.confirmed";
    
    private final RabbitTemplate rabbitTemplate;
    
    @Override
    public void publishBidConfirmedEvent(BidConfirmedEvent event) {
        try {
            rabbitTemplate.convertAndSend(
                    BID_EXCHANGE,
                    ROUTING_KEY_BID_CONFIRMED,
                    event
            );
            log.info("发布竞榜确认事件成功 bidId={}, projectId={}, teamId={}",
                    event.getBidId(), event.getProjectId(), event.getTeamId());
        } catch (Exception e) {
            log.error("发布竞榜确认事件失败 bidId={}, projectId={}",
                    event.getBidId(), event.getProjectId(), e);
            throw new RuntimeException("发布消息失败", e);
        }
    }
}

