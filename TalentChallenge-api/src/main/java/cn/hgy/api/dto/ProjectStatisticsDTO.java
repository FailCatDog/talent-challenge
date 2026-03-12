package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 项目统计响应DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectStatisticsDTO {

    /** 各状态项目数量 */
    private Map<String, Long> statusCount;

    /** 今日新增项目数 */
    private Long todayCount;

    /** 本周新增项目数 */
    private Long weekCount;

    /** 本月新增项目数 */
    private Long monthCount;

    /** 总项目数 */
    private Long totalCount;
}
