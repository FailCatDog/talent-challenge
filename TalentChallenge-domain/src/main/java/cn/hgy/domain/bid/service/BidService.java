package cn.hgy.domain.bid.service;

import cn.hgy.domain.bid.model.entity.BidEntity;
import cn.hgy.domain.bid.model.valobj.BidStatusEnum;
import cn.hgy.domain.bid.repository.IBidRepository;
import cn.hgy.domain.bid.service.IBidEventPublisher;
import cn.hgy.domain.project.model.entity.ProjectEntity;
import cn.hgy.domain.project.model.valobj.ProjectStatusEnum;
import cn.hgy.domain.project.repository.IProjectRepository;
import cn.hgy.types.common.BidCommand;
import cn.hgy.types.enums.ResponseCode;
import cn.hgy.types.event.BidConfirmedEvent;
import cn.hgy.types.exception.AppException;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BidService implements IBidService {

    private final Snowflake snowflake;
    private final IBidRepository bidRepository;
    private final IProjectRepository projectRepository;
    private final IBidEventPublisher bidEventPublisher;

    @Override
    public boolean apply(BidCommand command) {
        if (command == null || StrUtil.isBlank(command.getProjectId()) || StrUtil.isBlank(command.getStudentId())) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        ProjectEntity project = requireProject(command.getProjectId());
        if (StrUtil.equals(project.getPublisherId(), command.getStudentId())) {
            throw new AppException(ResponseCode.BID_PERMISSION_DENIED.getCode(), ResponseCode.BID_PERMISSION_DENIED.getInfo());
        }
        ProjectStatusEnum projectStatus = ProjectStatusEnum.getByCode(project.getStatus());
        if (projectStatus == null || !(ProjectStatusEnum.PUBLISHED.equals(projectStatus) || ProjectStatusEnum.IN_PROGRESS.equals(projectStatus))) {
            throw new AppException(ResponseCode.BID_PROJECT_INVALID.getCode(), ResponseCode.BID_PROJECT_INVALID.getInfo());
        }
        if (bidRepository.existsActiveBid(command.getProjectId(), command.getStudentId())) {
            throw new AppException(ResponseCode.BID_DUPLICATE.getCode(), ResponseCode.BID_DUPLICATE.getInfo());
        }
        LocalDateTime now = LocalDateTime.now();
        BidEntity bidEntity = BidEntity.builder()
                .bidId(snowflake.nextIdStr())
                .projectId(command.getProjectId())
                .studentId(command.getStudentId())
                .status(BidStatusEnum.APPLIED)
                .content(command.getContent())
                .attachmentUrl(command.getAttachmentUrl())
                .remark(null)
                .createdAt(now)
                .updatedAt(now)
                .version(0L)
                .build();
        boolean saved;
        try {
            saved = bidRepository.save(bidEntity);
        } catch (Exception e) {
            throw new AppException(ResponseCode.BID_APPLY_FAIL.getCode(), ResponseCode.BID_APPLY_FAIL.getInfo(), e);
        }
        if (!saved) {
            throw new AppException(ResponseCode.BID_APPLY_FAIL.getCode(), ResponseCode.BID_APPLY_FAIL.getInfo());
        }
        projectRepository.incrementApplicationCount(command.getProjectId());
        return true;
    }

    @Override
    public boolean shortlist(String bidId, String operatorId) {
        BidEntity bidEntity = loadBid(bidId);
        ProjectEntity project = requireProject(bidEntity.getProjectId());
        ensurePublisher(project, operatorId);
        if (!BidStatusEnum.APPLIED.equals(bidEntity.getStatus())) {
            throw new AppException(ResponseCode.BID_STATUS_INVALID.getCode(), ResponseCode.BID_STATUS_INVALID.getInfo());
        }
        return changeStatus(bidEntity, BidStatusEnum.SHORTLISTED, null);
    }

    @Override
    public boolean confirm(String bidId, String operatorId) {
        BidEntity bidEntity = loadBid(bidId);
        ProjectEntity project = requireProject(bidEntity.getProjectId());
        ensurePublisher(project, operatorId);
        if (!BidStatusEnum.SHORTLISTED.equals(bidEntity.getStatus())) {
            throw new AppException(ResponseCode.BID_STATUS_INVALID.getCode(), ResponseCode.BID_STATUS_INVALID.getInfo());
        }
        
        boolean success = changeStatus(bidEntity, BidStatusEnum.CONFIRMED, null);
        
        if (success) {
            BidConfirmedEvent event = BidConfirmedEvent.builder()
                    .bidId(bidId)
                    .projectId(bidEntity.getProjectId())
                    .studentId(bidEntity.getStudentId())
                    .operatorId(operatorId)
                    .confirmedAt(LocalDateTime.now())
                    .build();
            bidEventPublisher.publishBidConfirmedEvent(event);
        }
        
        return success;
    }

    @Override
    public boolean reject(String bidId, String operatorId, String reason) {
        BidEntity bidEntity = loadBid(bidId);
        ProjectEntity project = requireProject(bidEntity.getProjectId());
        ensurePublisher(project, operatorId);
        if (!Arrays.asList(BidStatusEnum.APPLIED, BidStatusEnum.SHORTLISTED).contains(bidEntity.getStatus())) {
            throw new AppException(ResponseCode.BID_STATUS_INVALID.getCode(), ResponseCode.BID_STATUS_INVALID.getInfo());
        }
        return changeStatus(bidEntity, BidStatusEnum.REJECTED, reason);
    }

    @Override
    public boolean cancel(String bidId, String studentId) {
        BidEntity bidEntity = loadBid(bidId);
        if (!StrUtil.equals(bidEntity.getStudentId(), studentId)) {
            throw new AppException(ResponseCode.BID_PERMISSION_DENIED.getCode(), ResponseCode.BID_PERMISSION_DENIED.getInfo());
        }
        if (!Arrays.asList(BidStatusEnum.APPLIED, BidStatusEnum.SHORTLISTED).contains(bidEntity.getStatus())) {
            throw new AppException(ResponseCode.BID_STATUS_INVALID.getCode(), ResponseCode.BID_STATUS_INVALID.getInfo());
        }
        return changeStatus(bidEntity, BidStatusEnum.CANCELLED, null);
    }

    @Override
    public List<BidEntity> queryProjectBids(String projectId, BidStatusEnum status, Integer pageNum, Integer pageSize) {
        if (StrUtil.isBlank(projectId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        requireProject(projectId);
        int currentPage = pageNum == null || pageNum <= 0 ? 1 : pageNum;
        int size = pageSize == null || pageSize <= 0 ? 10 : pageSize;
        return bidRepository.queryByProject(projectId, status, currentPage, size);
    }

    @Override
    public Long countProjectBids(String projectId, BidStatusEnum status) {
        if (StrUtil.isBlank(projectId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        requireProject(projectId);
        return bidRepository.countByProject(projectId, status);
    }

    private BidEntity loadBid(String bidId) {
        if (StrUtil.isBlank(bidId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        return bidRepository.queryById(bidId).orElseThrow(() ->
                new AppException(ResponseCode.BID_NOT_FOUND.getCode(), ResponseCode.BID_NOT_FOUND.getInfo()));
    }

    private ProjectEntity requireProject(String projectId) {
        ProjectEntity project = projectRepository.queryProjectById(projectId);
        if (project == null) {
            throw new AppException(ResponseCode.PROJECT_NOT_FOUND.getCode(), ResponseCode.PROJECT_NOT_FOUND.getInfo());
        }
        return project;
    }

    private void ensurePublisher(ProjectEntity project, String operatorId) {
        if (StrUtil.isBlank(operatorId) || !StrUtil.equals(project.getPublisherId(), operatorId)) {
            throw new AppException(ResponseCode.BID_PERMISSION_DENIED.getCode(), ResponseCode.BID_PERMISSION_DENIED.getInfo());
        }
    }

    private boolean changeStatus(BidEntity bidEntity, BidStatusEnum targetStatus, String remark) {
        boolean updated = bidRepository.updateStatus(bidEntity.getBidId(), targetStatus, bidEntity.getVersion(), remark);
        if (!updated) {
            throw new AppException(ResponseCode.BID_STATUS_INVALID.getCode(), ResponseCode.BID_STATUS_INVALID.getInfo());
        }
        return true;
    }
}

