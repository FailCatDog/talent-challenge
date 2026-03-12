package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-11-13
 * @Description: 里程碑响应DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MilestoneResponseDTO {
    
    private String milestoneId;
    private String projectId;
    private String milestoneCode;
    private String name;
    private String description;
    private Integer sequenceOrder;
    private String status;
    private Integer progressPercent;
    private String deliverableUrl;
    private String deliverableFiles;
    private String referenceFiles;
    private String approvalStatus;
    private String currentOwnerId;
    private LocalDateTime plannedStartTime;
    private LocalDateTime plannedEndTime;
    private LocalDateTime actualStartTime;
    private LocalDateTime actualEndTime;
    private Integer autoExtendedCount;
    private Integer remindBeforeHours;
    private Boolean isSkipped;
    private String skipReason;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

