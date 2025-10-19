package cn.hgy.domain.project.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-10-07
 * @Description: 项目类别实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCategoryEntity {
    /**
     * 项目分类ID（业务唯一标识）
     */
    private String projectCategoryId;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 父级分类ID（用于构建树形结构，根节点可为空）
     */
    private String parentId;

    /**
     * 分类描述信息
     */
    private String description;

    /**
     * 分类图标URL或图标资源标识
     */
    private String icon;

    /**
     * 排序序号（用于控制展示顺序）
     */
    private Integer sortOrder;

    /**
     * 是否可见（控制前端是否展示）
     */
    private Boolean isVisible;

    /**
     * 是否启用（控制业务可用性）
     */
    private Boolean isActive;
}
