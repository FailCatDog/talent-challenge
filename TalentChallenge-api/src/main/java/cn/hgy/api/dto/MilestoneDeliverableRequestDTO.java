package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-11-13
 * @Description: 提交里程碑成果请求DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MilestoneDeliverableRequestDTO {

    private String milestoneId;
    private String fileName;
    private String deliverableUrl;
    private Long fileSize;
    private String remark;
}

