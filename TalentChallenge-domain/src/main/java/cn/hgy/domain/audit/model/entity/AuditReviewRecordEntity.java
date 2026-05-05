package cn.hgy.domain.audit.model.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuditReviewRecordEntity {

    private String recordId;

    private String auditType;

    private String targetId;

    private String action;

    private String fromStatus;

    private String toStatus;

    private String reason;

    private String reviewerId;

    private LocalDateTime createTime;

    private String targetName;

    private String reviewerName;
}
