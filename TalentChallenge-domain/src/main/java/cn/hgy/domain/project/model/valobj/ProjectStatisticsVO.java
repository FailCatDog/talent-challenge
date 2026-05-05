package cn.hgy.domain.project.model.valobj;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * 项目统计值对象
 */
@Data
@Builder
public class ProjectStatisticsVO {

    /**
     * 各状态项目数量统计
     */
    private Map<String, Long> statusCount;

    /**
     * 今日新增数量
     */
    private Long todayCount;

    /**
     * 本周新增数量
     */
    private Long weekCount;

    /**
     * 本月新增数量
     */
    private Long monthCount;

    /**
     * 总发布数量
     */
    private Long totalCount;
}
