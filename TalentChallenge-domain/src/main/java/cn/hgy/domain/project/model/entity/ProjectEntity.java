package cn.hgy.domain.project.model.entity;

import cn.hgy.types.enums.ResponseCode;
import cn.hgy.types.exception.AppException;
import cn.hutool.core.util.StrUtil;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-10-07
 * @Description: 项目实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectEntity {
    /**
     * 项目ID，唯一标识符
     */
    private String projectId;

    /**
     * 项目序列号，用于业务流水号或展示编号
     */
    private String serialNumber;

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
     * 提交审核时间（状态变为pending_review的时间）
     */
    private LocalDateTime submittedAt;

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

    /**
     * 项目状态（如：草稿/已发布/进行中/已结束/已取消）
     */
    private String status;

    /**
     * 项目被拒绝的原因说明（当状态为"已拒绝"时有效）
     */
    private String rejectReason;

    /**
     * 项目进度百分比（0-100）
     */
    private Integer progress;

    /**
     * 项目发布者ID
     */
    private String publisherId;

    /**
     * 中标团队ID（被接受的团队）
     */
    private String acceptedTeamId;

    /**
     * 已申请该项目的团队数量
     */
    private Integer applicationCount;

    /**
     * 项目被查看次数
     */
    private Integer viewCount;

    /**
     * 项目被收藏次数
     */
    private Integer favoriteCount;

    /**
     * 数据版本号（用于乐观锁控制）
     */
    private Long version;

    /**
     * 发布方企业名称
     */
    private String publisherName;

    /**
     * 中标团队名称
     */
    private String acceptedTeamName;

    public void validateForCreation() {
        if (StrUtil.isBlank(this.title) || this.title.length() < 5 || this.title.length() > 100) {
            throw new AppException(ResponseCode.PROJECT_TITLE_INVALID.getCode(), ResponseCode.PROJECT_TITLE_INVALID.getInfo());
        }

        if (StrUtil.isNotBlank(this.description) && this.description.length() < 10) {
            throw new AppException(ResponseCode.PROJECT_DESCRIPTION_INVALID.getCode(), ResponseCode.PROJECT_DESCRIPTION_INVALID.getInfo());
        }

        if (this.budgetAmount != null && this.budgetAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AppException(ResponseCode.PROJECT_BUDGET_INVALID.getCode(), ResponseCode.PROJECT_BUDGET_INVALID.getInfo());
        }

        if (this.teamSizeMin != null && this.teamSizeMax != null) {
            if (this.teamSizeMin <= 0 || this.teamSizeMax <= 0 || this.teamSizeMin > this.teamSizeMax) {
                throw new AppException(ResponseCode.PROJECT_TEAM_SIZE_INVALID.getCode(), ResponseCode.PROJECT_TEAM_SIZE_INVALID.getInfo());
            }
        }

        if (this.durationDays != null && this.durationDays <= 0) {
            throw new AppException(ResponseCode.PROJECT_DURATION_INVALID.getCode(), ResponseCode.PROJECT_DURATION_INVALID.getInfo());
        }
    }
}
