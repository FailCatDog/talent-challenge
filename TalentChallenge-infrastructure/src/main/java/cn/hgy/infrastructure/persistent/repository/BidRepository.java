package cn.hgy.infrastructure.persistent.repository;

import cn.hgy.api.dto.BidPublicListItemResponseDTO;
import cn.hgy.api.dto.StudentBidListResponseDTO;
import cn.hgy.domain.bid.model.entity.BidEntity;
import cn.hgy.domain.bid.model.valobj.BidStatusEnum;
import cn.hgy.domain.bid.repository.IBidRepository;
import cn.hgy.infrastructure.persistent.dao.BidDao;
import cn.hgy.infrastructure.persistent.po.Bid;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BidRepository implements IBidRepository {

    private final BidDao bidDao;

    @Override
    public boolean save(BidEntity bidEntity) {
        Bid bid = convertToPo(bidEntity);
        bid.setCreateTime(LocalDateTime.now());
        bid.setUpdateTime(LocalDateTime.now());
        return bidDao.insert(bid) > 0;
    }

    @Override
    public boolean update(BidEntity bidEntity) {
        Bid bid = convertToPo(bidEntity);
        bid.setUpdateTime(LocalDateTime.now());
        return bidDao.update(bid) > 0;
    }

    @Override
    public boolean updateStatus(String bidId, BidStatusEnum status, Long version, String remark) {
        return bidDao.updateStatus(bidId, status.getCode(), version, remark) > 0;
    }

    @Override
    public Optional<BidEntity> queryById(String bidId) {
        Bid bid = bidDao.selectByBidId(bidId);
        if (bid == null) {
            return Optional.empty();
        }
        return Optional.of(convertToEntity(bid));
    }

    @Override
    public List<BidEntity> queryByProject(String projectId, BidStatusEnum status, Integer pageNum, Integer pageSize) {
        Integer offset = null;
        Integer limit = null;
        if (pageNum != null && pageSize != null && pageNum > 0 && pageSize > 0) {
            offset = (pageNum - 1) * pageSize;
            limit = pageSize;
        }
        List<Bid> bids = bidDao.selectByProjectId(projectId, status == null ? null : status.getCode(), offset, limit);
        if (bids == null || bids.isEmpty()) {
            return new ArrayList<>();
        }
        List<BidEntity> entities = new ArrayList<>();
        for (Bid bid : bids) {
            entities.add(convertToEntity(bid));
        }
        return entities;
    }

    @Override
    public Long countByProject(String projectId, BidStatusEnum status) {
        return bidDao.countByProjectId(projectId, status == null ? null : status.getCode());
    }

    @Override
    public boolean existsActiveBid(String projectId, String teamId) {
        List<String> statusList = Arrays.asList(
                BidStatusEnum.APPLIED.getCode(),
                BidStatusEnum.SHORTLISTED.getCode(),
                BidStatusEnum.CONFIRMED.getCode()
        );
        return bidDao.existsActiveBid(projectId, teamId, statusList) > 0;
    }

    @Override
    public List<String> findOtherActiveBidIds(String projectId, String excludeBidId) {
        List<String> statusList = Arrays.asList(
                BidStatusEnum.APPLIED.getCode(),
                BidStatusEnum.SHORTLISTED.getCode()
        );
        return bidDao.findOtherActiveBidIds(projectId, excludeBidId, statusList);
    }

    @Override
    public void batchUpdateStatus(List<String> bidIds, BidStatusEnum status) {
        if (bidIds == null || bidIds.isEmpty()) {
            return;
        }
        for (String bidId : bidIds) {
            bidDao.updateStatus(bidId, status.getCode(), null, "其他申请已中标");
        }
    }

    @Override
    public List<BidPublicListItemResponseDTO> queryPublicBidsByProject(String projectId) {
        return bidDao.selectPublicBidsByProject(projectId);
    }

    @Override
    public List<StudentBidListResponseDTO> queryStudentBids(String userId) {
        return bidDao.selectStudentBids(userId);
    }

    @Override
    public void updateBidAttachment(String bidId, String attachmentUrl) {
        bidDao.updateBidAttachment(bidId, attachmentUrl);
    }

    private Bid convertToPo(BidEntity entity) {
        Bid bid = new Bid();
        bid.setBidId(entity.getBidId());
        bid.setProjectId(entity.getProjectId());
        bid.setTeamId(entity.getTeamId());
        bid.setStatus(entity.getStatus() == null ? null : entity.getStatus().getCode());
        bid.setContent(entity.getContent());
        bid.setAttachmentUrl(entity.getAttachmentUrl());
        bid.setRemark(entity.getRemark());
        bid.setCreateTime(entity.getCreatedAt());
        bid.setUpdateTime(entity.getUpdatedAt());
        bid.setVersion(entity.getVersion());
        return bid;
    }

    private BidEntity convertToEntity(Bid bid) {
        return BidEntity.builder()
                .bidId(bid.getBidId())
                .projectId(bid.getProjectId())
                .teamId(bid.getTeamId())
                .status(bid.getStatus() == null ? null : BidStatusEnum.getByCode(bid.getStatus()))
                .content(bid.getContent())
                .attachmentUrl(bid.getAttachmentUrl())
                .remark(bid.getRemark())
                .createdAt(bid.getCreateTime())
                .updatedAt(bid.getUpdateTime())
                .version(bid.getVersion())
                .build();
    }
}
