package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-11-13
 * @Description: 里程碑列表响应DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MilestoneListResponseDTO {
    
    private List<MilestoneResponseDTO> milestones;
    private Integer total;
}

