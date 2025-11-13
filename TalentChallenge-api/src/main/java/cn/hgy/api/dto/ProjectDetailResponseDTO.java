package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-10-19
 * @Description: 项目详情响应传输对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectDetailResponseDTO {
    /**
     * 项目唯一标识符
     */
    private String projectId;

    /**
     * 项目流水号
     */
    private String serialNumber;

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

    /**
     * 项目状态
     */
    private String status;

    /**
     * 拒绝原因
     */
    private String rejectReason;

    /**
     * 项目进度
     */
    private Integer progress;

    /**
     * 发布者ID
     */
    private String publisherId;

    /**
     * 中标团队ID
     */
    private String acceptedTeamId;

    /**
     * 申请人数
     */
    private Integer applicationCount;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 收藏次数
     */
    private Integer favoriteCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

