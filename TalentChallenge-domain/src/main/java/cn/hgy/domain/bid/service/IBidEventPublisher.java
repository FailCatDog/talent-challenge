package cn.hgy.domain.bid.service;

import cn.hgy.types.event.BidConfirmedEvent;

public interface IBidEventPublisher {
    
    void publishBidConfirmedEvent(BidConfirmedEvent event);
}

