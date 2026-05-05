package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 管理员查看待审核企业列表的响应DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyPendingReviewDTO {

    /** 用户ID */
    private String userId;

    /** 企业名称（user表nickname） */
    private String companyName;

    /** 企业统一社会信用代码 */
    private String creditCode;

    /** 行业领域 */
    private String companyIndustry;

    /** 认证状态 */
    private String certificationStatus;

    /** 提交/更新时间 */
    private LocalDateTime updateTime;
}
