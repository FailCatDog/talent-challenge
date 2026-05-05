package cn.hgy.domain.project.service;

import cn.hgy.domain.project.model.valobj.ProjectStatisticsVO;
import cn.hgy.domain.project.repository.IProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 项目统计服务
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectStatisticsService {

    private final IProjectRepository projectRepository;

    /**
     * 获取项目统计数据
     */
    public ProjectStatisticsVO getProjectStatistics() {
        return projectRepository.getProjectStatistics();
    }
}
