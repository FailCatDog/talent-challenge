package cn.hgy.infrastructure.persistent.po;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 里程碑模板持久化对象
 *
 * @author hgy
 */
@Data
public class MilestoneTemplate {
    
    private Long id;
    private String templateId;
    private String milestoneCode;
    private String name;
    private String description;
    private String milestoneType;
    private Integer defaultSequenceOrder;
    private String defaultApprovalRule;
    private Integer defaultRemindBeforeHours;
    private Boolean isEnabled;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

