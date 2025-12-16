package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-11-13
 * @Description: 跳过里程碑请求DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MilestoneSkipRequestDTO {
    
    private String milestoneId;
    private String skipReason;
}

