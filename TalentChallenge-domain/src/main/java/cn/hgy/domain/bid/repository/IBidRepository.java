package cn.hgy.domain.bid.repository;

import cn.hgy.api.dto.BidPublicListItemResponseDTO;
import cn.hgy.api.dto.StudentBidListResponseDTO;
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

    boolean existsActiveBid(String projectId, String teamId);

    List<String> findOtherActiveBidIds(String projectId, String excludeBidId);

    void batchUpdateStatus(List<String> bidIds, BidStatusEnum status);

    /**
     * 查询项目的公开揭榜名单
     * @param projectId 项目ID
     * @return 公开揭榜列表
     */
    List<BidPublicListItemResponseDTO> queryPublicBidsByProject(String projectId);

    /**
     * 查询学生的揭榜列表
     * @param userId 学生用户ID
     * @return 学生揭榜列表
     */
    List<StudentBidListResponseDTO> queryStudentBids(String userId);

    /**
     * 更新揭榜申请附件URL
     * @param bidId 揭榜ID
     * @param attachmentUrl 附件URL
     */
    void updateBidAttachment(String bidId, String attachmentUrl);
}

