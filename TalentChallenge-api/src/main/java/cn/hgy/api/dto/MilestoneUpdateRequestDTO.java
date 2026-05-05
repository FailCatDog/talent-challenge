package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MilestoneUpdateRequestDTO {
    
    private String milestoneId;
    private String description;
    private LocalDateTime plannedStartTime;
    private LocalDateTime plannedEndTime;
    private Integer remindBeforeHours;
}