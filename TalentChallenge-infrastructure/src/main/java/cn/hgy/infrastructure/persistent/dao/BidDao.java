package cn.hgy.infrastructure.persistent.dao;

import cn.hgy.api.dto.BidPublicListItemResponseDTO;
import cn.hgy.api.dto.StudentBidListResponseDTO;
import cn.hgy.infrastructure.persistent.po.Bid;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BidDao {

    int insert(Bid bid);

    int update(Bid bid);

    Bid selectByBidId(@Param("bidId") String bidId);

    List<Bid> selectByProjectId(@Param("projectId") String projectId,
                                @Param("status") String status,
                                @Param("offset") Integer offset,
                                @Param("limit") Integer limit);

    long countByProjectId(@Param("projectId") String projectId, @Param("status") String status);

    int existsActiveBid(@Param("projectId") String projectId, @Param("teamId") String teamId, @Param("statusList") List<String> statusList);

    int updateStatus(@Param("bidId") String bidId, @Param("status") String status, @Param("version") Long version, @Param("remark") String remark);

    List<String> findOtherActiveBidIds(@Param("projectId") String projectId,
                                       @Param("excludeBidId") String excludeBidId,
                                       @Param("statusList") List<String> statusList);

    /**
     * 查询项目的公开揭榜名单
     */
    List<BidPublicListItemResponseDTO> selectPublicBidsByProject(@Param("projectId") String projectId);

    /**
     * 查询学生的揭榜列表
     */
    List<StudentBidListResponseDTO> selectStudentBids(@Param("userId") String userId);
}

