package cn.hgy.api.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-10-19
 * @Description: 项目搜索请求传输对象
 */
@Data
public class ProjectSearchRequestDTO {
    private String keyword;
    private String categoryId;
    private String skillLevel;
    private BigDecimal budgetMin;
    private BigDecimal budgetMax;
    private Integer pageNum;
    private Integer pageSize;
}

