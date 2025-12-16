package cn.hgy.domain.bid.service;

import cn.hgy.domain.bid.model.entity.BidEntity;
import cn.hgy.domain.bid.model.valobj.BidStatusEnum;
import cn.hgy.types.common.BidCommand;

import java.util.List;

public interface IBidService {

    boolean apply(BidCommand command);

    boolean shortlist(String bidId, String operatorId);

    boolean confirm(String bidId, String operatorId);

    boolean reject(String bidId, String operatorId, String reason);

    boolean cancel(String bidId, String teamId);

    List<BidEntity> queryProjectBids(String projectId, BidStatusEnum status, Integer pageNum, Integer pageSize);

    Long countProjectBids(String projectId, BidStatusEnum status);
}

