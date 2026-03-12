package cn.hgy.trigger.http;

import cn.hgy.api.dto.StatisticsResponse;
import cn.hgy.api.response.Response;
import cn.hgy.domain.project.service.ProjectStatisticsService;
import cn.hgy.domain.project.service.CategoryDistributionService;
import cn.hgy.domain.user.service.UserStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 统计接口控制器（管理员）
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/statistics")
public class StatisticsHttpController {

    private final ProjectStatisticsService projectStatisticsService;
    private final UserStatisticsService userStatisticsService;
    private final CategoryDistributionService categoryDistributionService;

    @GetMapping
    public Response<StatisticsResponse> getStatistics() {
        StatisticsResponse data = new StatisticsResponse(
                projectStatisticsService.getProjectStatistics(),
                userStatisticsService.getUserStatistics(),
                categoryDistributionService.getCategoryDistribution()
        );
        return Response.<StatisticsResponse>builder()
                .code("200")
                .info("Success")
                .data(data)
                .build();
    }
}
