package cn.hgy.infrastructure.persistent.repository;

import cn.hgy.domain.audit.model.entity.AuditReviewRecordEntity;
import cn.hgy.domain.audit.repository.IAuditReviewRepository;
import cn.hgy.infrastructure.persistent.dao.AuditReviewRecordDao;
import cn.hgy.infrastructure.persistent.po.AuditReviewRecord;
import cn.hutool.core.lang.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AuditReviewRepository implements IAuditReviewRepository {

    private final AuditReviewRecordDao auditReviewRecordDao;
    private final Snowflake snowflake;

    @Override
    public void save(AuditReviewRecordEntity entity) {
        AuditReviewRecord record = new AuditReviewRecord();
        record.setRecordId(entity.getRecordId() != null ? entity.getRecordId() : snowflake.nextIdStr());
        record.setAuditType(entity.getAuditType());
        record.setTargetId(entity.getTargetId());
        record.setAction(entity.getAction());
        record.setFromStatus(entity.getFromStatus());
        record.setToStatus(entity.getToStatus());
        record.setReason(entity.getReason());
        record.setReviewerId(entity.getReviewerId());
        record.setCreateTime(entity.getCreateTime() != null ? entity.getCreateTime() : LocalDateTime.now());
        auditReviewRecordDao.insert(record);
    }

    @Override
    public List<AuditReviewRecordEntity> queryHistory(String auditType, String targetId, String action, int offset, int limit) {
        return auditReviewRecordDao.selectHistory(auditType, targetId, action, offset, limit)
                .stream()
                .map(this::toEntity)
                .toList();
    }

    @Override
    public long countHistory(String auditType, String targetId, String action) {
        return auditReviewRecordDao.countHistory(auditType, targetId, action);
    }

    private AuditReviewRecordEntity toEntity(AuditReviewRecord record) {
        return AuditReviewRecordEntity.builder()
                .recordId(record.getRecordId())
                .auditType(record.getAuditType())
                .targetId(record.getTargetId())
                .targetName(record.getTargetName())
                .action(record.getAction())
                .fromStatus(record.getFromStatus())
                .toStatus(record.getToStatus())
                .reason(record.getReason())
                .reviewerId(record.getReviewerId())
                .reviewerName(record.getReviewerName())
                .createTime(record.getCreateTime())
                .build();
    }
}
