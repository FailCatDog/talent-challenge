package cn.hgy.api.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-10-07
 * @Description: 项目创建传输对象dto
 */
@Data
public class ProjectCreateRequestDTO {
    /**
     * 项目标题，简要描述项目内容
     */
    private String title;

    /**
     * 项目详细描述，说明具体需求和细节
     */
    private String description;

    /**
     * 项目分类ID，关联项目类型分类
     */
    private String categoryId;

    /**
     * 项目标签集合，存储为JSON字符串格式（如：["UI设计","前端开发"]）
     */
    private String tags;

    /**
     * 项目特殊要求或附加条件
     */
    private String requirements;

    /**
     * 所需技能等级（如：初级/中级/高级）
     */
    private String skillLevel;

    /**
     * 团队最小人数要求
     */
    private Integer teamSizeMin;

    /**
     * 团队最大人数限制
     */
    private Integer teamSizeMax;

    /**
     * 项目预计持续天数
     */
    private Integer durationDays;

    /**
     * 预算类型（如：固定总价/按时计费/按里程碑支付）
     */
    private String budgetType;

    /**
     * 预算金额（具体数值）
     */
    private BigDecimal budgetAmount;

    /**
     * 货币类型（如：USD/CNY/EUR）
     */
    private String currency;

    /**
     * 项目发布时间戳
     */
    private LocalDateTime publishedAt;

    /**
     * 申请截止时间
     */
    private LocalDateTime applicationDeadline;

    /**
     * 预计项目开始日期
     */
    private LocalDate expectedStartDate;

    /**
     * 预计项目结束日期
     */
    private LocalDate expectedEndDate;
}
