package cn.hgy.api.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-10-19
 * @Description: 项目更新请求传输对象
 */
@Data
public class ProjectUpdateRequestDTO {
    /**
     * 项目唯一标识符
     */
    private String projectId;

    /**
     * 项目标题
     */
    private String title;

    /**
     * 项目详细描述
     */
    private String description;

    /**
     * 项目分类ID
     */
    private String categoryId;

    /**
     * 项目标签
     */
    private String tags;

    /**
     * 项目特殊要求
     */
    private String requirements;

    /**
     * 所需技能等级
     */
    private String skillLevel;

    /**
     * 团队最小人数
     */
    private Integer teamSizeMin;

    /**
     * 团队最大人数
     */
    private Integer teamSizeMax;

    /**
     * 项目持续天数
     */
    private Integer durationDays;

    /**
     * 预算类型
     */
    private String budgetType;

    /**
     * 预算金额
     */
    private BigDecimal budgetAmount;

    /**
     * 货币类型
     */
    private String currency;

    /**
     * 项目发布时间
     */
    private LocalDateTime publishedAt;

    /**
     * 申请截止时间
     */
    private LocalDateTime applicationDeadline;

    /**
     * 预计开始日期
     */
    private LocalDate expectedStartDate;

    /**
     * 预计结束日期
     */
    private LocalDate expectedEndDate;
}

