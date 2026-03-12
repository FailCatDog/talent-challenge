package cn.hgy.domain.user.model.valobj;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * 用户统计值对象
 */
@Data
@Builder
public class UserStatisticsVO {

    /**
     * 用户总数
     */
    private Long totalCount;

    /**
     * 学生数量
     */
    private Long studentCount;

    /**
     * 企业数量
     */
    private Long companyCount;

    /**
     * 管理员数量
     */
    private Long adminCount;

    /**
     * 活跃用户数
     */
    private Long activeCount;

    /**
     * 认证状态统计
     */
    private Map<String, Long> certificationStatusCount;
}
