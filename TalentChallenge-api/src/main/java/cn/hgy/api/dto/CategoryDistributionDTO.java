package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目分类分布DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDistributionDTO {
    /** 分类ID */
    private String categoryId;
    /** 分类名称 */
    private String categoryName;
    /** 项目数量 */
    private Long projectCount;
}
