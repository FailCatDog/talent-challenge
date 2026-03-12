package cn.hgy.domain.project.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目分类分布值对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDistributionVO {

    /**
     * 分类ID
     */
    private String categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 项目数量
     */
    private Long projectCount;
}
