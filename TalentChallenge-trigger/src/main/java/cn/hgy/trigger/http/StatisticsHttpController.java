package cn.hgy.trigger.http;

import cn.hgy.api.dto.CategoryDistributionDTO;
import cn.hgy.api.dto.ProjectStatisticsDTO;
import cn.hgy.api.dto.StatisticsResponse;
import cn.hgy.api.dto.UserStatisticsDTO;
import cn.hgy.api.response.Response;
import cn.hgy.domain.project.model.valobj.CategoryDistributionVO;
import cn.hgy.domain.project.model.valobj.ProjectStatisticsVO;
import cn.hgy.domain.project.service.CategoryDistributionService;
import cn.hgy.domain.project.service.ProjectStatisticsService;
import cn.hgy.domain.user.model.valobj.UserStatisticsVO;
import cn.hgy.domain.user.service.UserStatisticsService;
import cn.hgy.types.enums.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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
        log.info("管理员查询平台统计数据");
        // 项目统计 VO → DTO
        ProjectStatisticsVO projectVO = projectStatisticsService.getProjectStatistics();
        ProjectStatisticsDTO projectDTO = ProjectStatisticsDTO.builder()
                .statusCount(projectVO.getStatusCount())
                .todayCount(projectVO.getTodayCount())
                .weekCount(projectVO.getWeekCount())
                .monthCount(projectVO.getMonthCount())
                .totalCount(projectVO.getTotalCount())
                .build();

        // 用户统计 VO → DTO
        UserStatisticsVO userVO = userStatisticsService.getUserStatistics();
        UserStatisticsDTO userDTO = UserStatisticsDTO.builder()
                .totalCount(userVO.getTotalCount())
                .studentCount(userVO.getStudentCount())
                .companyCount(userVO.getCompanyCount())
                .adminCount(userVO.getAdminCount())
                .activeCount(userVO.getActiveCount())
                .certificationStatusCount(userVO.getCertificationStatusCount())
                .build();

        // 分类分布 VO → DTO
        List<CategoryDistributionVO> categoryVOs = categoryDistributionService.getCategoryDistribution();
        List<CategoryDistributionDTO> categoryDTOs = categoryVOs.stream()
                .map(vo -> CategoryDistributionDTO.builder()
                        .categoryId(vo.getCategoryId())
                        .categoryName(vo.getCategoryName())
                        .projectCount(vo.getProjectCount())
                        .build())
                .collect(Collectors.toList());

        StatisticsResponse data = new StatisticsResponse(projectDTO, userDTO, categoryDTOs);
        return Response.<StatisticsResponse>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(data)
                .build();
    }
}
