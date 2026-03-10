package cn.hgy.domain.bid.service;

import cn.hgy.api.dto.BidDetailResponseDTO;
import cn.hgy.api.dto.BidPublicListItemResponseDTO;
import cn.hgy.api.dto.StudentBidListResponseDTO;
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

    /**
     * 查询项目的公开揭榜名单
     */
    List<BidPublicListItemResponseDTO> queryPublicBidsByProject(String projectId);

    /**
     * 查询学生的揭榜列表
     */
    List<StudentBidListResponseDTO> queryStudentBids(String userId);

    /**
     * 查询揭榜详情
     */
    BidDetailResponseDTO getBidDetail(String bidId);
}

