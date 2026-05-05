package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 统计数据响应包装类
 */
@Data
@AllArgsConstructor
public class StatisticsResponse {

    /** 项目统计 */
    private ProjectStatisticsDTO projectStatistics;

    /** 用户统计 */
    private UserStatisticsDTO userStatistics;

    /** 分类分布 */
    private List<CategoryDistributionDTO> categoryDistribution;
}
