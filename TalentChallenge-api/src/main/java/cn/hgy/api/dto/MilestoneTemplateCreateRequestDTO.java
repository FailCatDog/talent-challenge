package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-11-25
 * @Description: 从模板创建里程碑请求DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MilestoneTemplateCreateRequestDTO {
    
    private String projectId;
    private List<String> templateIds;
}

