package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-11-13
 * @Description: 更新里程碑状态请求DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MilestoneStatusUpdateRequestDTO {
    
    private String milestoneId;
    private String status;
    private String remark;
}

