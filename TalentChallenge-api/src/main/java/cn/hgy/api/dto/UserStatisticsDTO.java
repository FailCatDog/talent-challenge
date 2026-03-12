package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 用户统计响应DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserStatisticsDTO {

    /** 总用户数 */
    private Long totalCount;

    /** 学生用户数 */
    private Long studentCount;

    /** 企业用户数 */
    private Long companyCount;

    /** 管理员用户数 */
    private Long adminCount;

    /** 最近登录活跃用户数 */
    private Long activeCount;

    /** 认证状态统计（certified/pending_review/uncertified/rejected） */
    private Map<String, Long> certificationStatusCount;
}
