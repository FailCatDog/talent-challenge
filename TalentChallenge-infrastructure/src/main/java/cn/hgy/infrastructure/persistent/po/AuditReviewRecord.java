package cn.hgy.infrastructure.persistent.po;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditReviewRecord {

    private Long id;

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
