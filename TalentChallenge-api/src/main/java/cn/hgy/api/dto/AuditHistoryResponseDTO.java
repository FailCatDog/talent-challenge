package cn.hgy.api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuditHistoryResponseDTO {

    private String recordId;

    private String auditType;

    private String targetId;

    private String targetName;

    private String action;

    private String fromStatus;

    private String toStatus;

    private String reason;

    private String reviewerId;

    private String reviewerName;

    private LocalDateTime createTime;
}
