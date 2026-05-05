package cn.hgy.domain.audit.service;

import cn.hgy.domain.audit.model.entity.AuditReviewRecordEntity;
import cn.hgy.domain.audit.repository.IAuditReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditHistoryService implements IAuditHistoryService {

    private final IAuditReviewRepository auditReviewRepository;

    @Override
    public List<AuditReviewRecordEntity> queryHistory(String auditType, String targetId, String action, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return auditReviewRepository.queryHistory(auditType, targetId, action, offset, pageSize);
    }

    @Override
    public long countHistory(String auditType, String targetId, String action) {
        return auditReviewRepository.countHistory(auditType, targetId, action);
    }
}
