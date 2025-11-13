package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-10-19
 * @Description: 项目列表响应传输对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectListResponseDTO {
    private List<ProjectListItemResponseDTO> projects;
    private Long total;
    private Integer pageNum;
    private Integer pageSize;
}

