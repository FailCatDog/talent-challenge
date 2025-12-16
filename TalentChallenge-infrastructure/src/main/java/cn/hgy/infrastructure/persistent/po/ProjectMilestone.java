package cn.hgy.infrastructure.persistent.po;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 里程碑持久化对象
 *
 * @author hgy
 */
@Data
public class ProjectMilestone {
    
    private Long id;
    private String milestoneId;
    private String projectId;
    private String milestoneCode;
    private String name;
    private String description;
    private Integer sequenceOrder;
    private String status;
    private Integer progressPercent;
    private String deliverableUrl;
    private String approvalRule;
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
    private Long version;
}

