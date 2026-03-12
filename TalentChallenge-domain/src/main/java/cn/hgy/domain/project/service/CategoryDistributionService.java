package cn.hgy.domain.project.service;

import cn.hgy.domain.project.model.valobj.CategoryDistributionVO;
import cn.hgy.domain.project.repository.IProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类分布统计服务
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryDistributionService {

    private final IProjectRepository projectRepository;

    /**
     * 获取所有分类的项目分布统计
     */
    public List<CategoryDistributionVO> getCategoryDistribution() {
        return projectRepository.getCategoryDistribution();
    }
}
