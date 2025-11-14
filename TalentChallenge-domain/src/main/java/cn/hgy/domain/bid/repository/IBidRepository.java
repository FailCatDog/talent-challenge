package cn.hgy.domain.bid.repository;

import cn.hgy.domain.bid.model.entity.BidEntity;
import cn.hgy.domain.bid.model.valobj.BidStatusEnum;

import java.util.List;
import java.util.Optional;

public interface IBidRepository {

    boolean save(BidEntity bidEntity);

    boolean update(BidEntity bidEntity);

    boolean updateStatus(String bidId, BidStatusEnum status, Long version, String remark);

    Optional<BidEntity> queryById(String bidId);

    List<BidEntity> queryByProject(String projectId, BidStatusEnum status, Integer pageNum, Integer pageSize);

    Long countByProject(String projectId, BidStatusEnum status);

    boolean existsActiveBid(String projectId, String studentId);

    List<String> findOtherActiveBidIds(String projectId, String excludeBidId);

    void batchUpdateStatus(List<String> bidIds, BidStatusEnum status);
}

