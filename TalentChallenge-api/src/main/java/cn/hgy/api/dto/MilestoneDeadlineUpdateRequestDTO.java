package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-11-25
 * @Description: 里程碑期限更新请求DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MilestoneDeadlineUpdateRequestDTO {
    
    private String milestoneId;
    
    /**
     * 计划开始时间
     */
    private LocalDateTime plannedStartTime;
    
    /**
     * 计划结束时间
     */
    private LocalDateTime plannedEndTime;
    
    /**
     * 提前提醒小时数
     */
    private Integer remindBeforeHours;
}

