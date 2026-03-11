package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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

    /** 企业统一社会信用代码 */
    private String creditCode;

    /** 资质文件URL列表 */
    private List<String> qualificationFiles;

    /** 认证状态 */
    private String certificationStatus;

    /** 拒绝原因 */
    private String rejectReason;

    /** 提交/更新时间 */
    private LocalDateTime submitTime;
}
