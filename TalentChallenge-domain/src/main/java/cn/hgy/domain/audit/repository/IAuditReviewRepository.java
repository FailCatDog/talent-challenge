package cn.hgy.domain.audit.repository;

import cn.hgy.domain.audit.model.entity.AuditReviewRecordEntity;

import java.util.List;

public interface IAuditReviewRepository {

    void save(AuditReviewRecordEntity entity);

    List<AuditReviewRecordEntity> queryHistory(String auditType, String targetId, String action, int offset, int limit);

    long countHistory(String auditType, String targetId, String action);
}
