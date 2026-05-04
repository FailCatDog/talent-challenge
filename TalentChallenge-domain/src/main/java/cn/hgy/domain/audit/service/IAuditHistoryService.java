package cn.hgy.domain.audit.service;

import cn.hgy.domain.audit.model.entity.AuditReviewRecordEntity;

import java.util.List;

public interface IAuditHistoryService {

    List<AuditReviewRecordEntity> queryHistory(String auditType, String targetId, String action, int pageNum, int pageSize);

    long countHistory(String auditType, String targetId, String action);
}
