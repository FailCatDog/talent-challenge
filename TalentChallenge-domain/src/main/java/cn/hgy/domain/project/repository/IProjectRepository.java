package cn.hgy.domain.project.repository;

import cn.hgy.domain.project.model.entity.ProjectEntity;
import cn.hgy.domain.project.model.valobj.CategoryDistributionVO;
import cn.hgy.domain.project.model.valobj.ProjectStatisticsVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-10-07
 * @Description: 项目仓储接口
 */
public interface IProjectRepository {
    /**
     * 创建项目
     * @param projectEntity 项目实体
     */
    void createProject(ProjectEntity projectEntity);

    /**
     * 更新项目
     * @param projectEntity 项目实体
     */
    void updateProject(ProjectEntity projectEntity);

    /**
     * 根据项目ID查询项目
     * @param projectId 项目ID
     * @return 项目实体
     */
    ProjectEntity queryProjectById(String projectId);

    /**
     * 根据项目ID查询项目（包含企业名称和团队名称）
     * @param projectId 项目ID
     * @return 项目实体
     */
    ProjectEntity queryProjectByIdWithNames(String projectId);

    /**
     * 根据发布者ID查询项目列表
     * @param publisherId 发布者ID
     * @param status 项目状态（可选）
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 项目列表
     */
    List<ProjectEntity> queryProjectsByPublisherId(String publisherId, String status, Integer offset, Integer limit);

    /**
     * 统计发布者的项目数量
     * @param publisherId 发布者ID
     * @param status 项目状态（可选）
     * @return 项目数量
     */
    Long countProjectsByPublisherId(String publisherId, String status);

    /**
     * 更新项目状态
     * @param projectId 项目ID
     * @param status 项目状态
     */
    void updateProjectStatus(String projectId, String status);

    /**
     * 查询在指定更新时间之前的某状态项目
     * @param status 项目状态
     * @param updateTimeBefore 更新时间上限
     * @return 项目列表
     */
    List<ProjectEntity> queryProjectsByStatusUpdatedBefore(String status, LocalDateTime updateTimeBefore);

    /**
     * 更新项目状态和提交时间
     * @param projectId 项目ID
     * @param status 项目状态
     * @param submittedAt 提交时间
     */
    void updateProjectStatusAndSubmittedAt(String projectId, String status, LocalDateTime submittedAt);

    /**
     * 查询已发布项目列表
     * @param categoryId 分类ID（可选）
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 项目列表
     */
    List<ProjectEntity> queryPublishedProjects(String categoryId, Integer offset, Integer limit);

    /**
     * 统计已发布项目数量
     * @param categoryId 分类ID（可选）
     * @return 项目数量
     */
    Long countPublishedProjects(String categoryId);

    /**
     * 搜索项目
     * @param keyword 关键词
     * @param categoryId 分类ID
     * @param skillLevel 技能等级
     * @param budgetMin 最低预算
     * @param budgetMax 最高预算
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 项目列表
     */
    List<ProjectEntity> searchProjects(String keyword, String categoryId, String skillLevel,
                                      java.math.BigDecimal budgetMin, java.math.BigDecimal budgetMax,
                                      Integer offset, Integer limit);

    /**
     * 统计搜索结果数量
     * @param keyword 关键词
     * @param categoryId 分类ID
     * @param skillLevel 技能等级
     * @param budgetMin 最低预算
     * @param budgetMax 最高预算
     * @return 项目数量
     */
    Long countSearchResults(String keyword, String categoryId, String skillLevel,
                           java.math.BigDecimal budgetMin, java.math.BigDecimal budgetMax);

    /**
     * 获取热门项目
     * @param limit 数量限制
     * @return 项目列表
     */
    List<ProjectEntity> queryPopularProjects(Integer limit);

    /**
     * 获取最新项目
     * @param limit 数量限制
     * @return 项目列表
     */
    List<ProjectEntity> queryLatestProjects(Integer limit);

    /**
     * 增加项目浏览量
     * @param projectId 项目ID
     */
    void incrementViewCount(String projectId);

    /**
     * 更新项目状态和拒绝原因
     * @param projectId 项目ID
     * @param status 项目状态
     * @param rejectReason 拒绝原因
     */
    void updateProjectStatusAndReason(String projectId, String status, String rejectReason);

    /**
     * 根据状态查询项目列表
     * @param status 项目状态
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 项目列表
     */
    List<ProjectEntity> queryProjectsByStatus(String status, Integer offset, Integer limit);

    /**
     * 统计特定状态的项目数量
     * @param status 项目状态
     * @return 项目数量
     */
    Long countProjectsByStatus(String status);

    void incrementApplicationCount(String projectId);

    void updateAcceptedTeam(String projectId, String teamId);

    /**
     * 创建项目分类
     * @param projectCategoryEntity 项目分类实体
     */
    void createProjectCategory(cn.hgy.domain.project.model.entity.ProjectCategoryEntity projectCategoryEntity);

    /**
     * 根据分类名称查询是否存在
     * @param name 分类名称
     * @return 是否存在
     */
    boolean existsCategoryByName(String name);

    /**
     * 根据分类ID查询是否存在
     * @param categoryId 分类ID
     * @return 是否存在
     */
    boolean existsCategoryById(String categoryId);

    /**
     * 更新项目分类
     * @param projectCategoryEntity 项目分类实体
     */
    void updateProjectCategory(cn.hgy.domain.project.model.entity.ProjectCategoryEntity projectCategoryEntity);

    /**
     * 根据分类ID删除分类
     * @param categoryId 分类ID
     */
    void deleteProjectCategory(String categoryId);

    /**
     * 查询所有项目分类
     * @return 分类列表
     */
    java.util.List<cn.hgy.domain.project.model.entity.ProjectCategoryEntity> queryAllCategories();

    /**
     * 查询所有可见的项目分类
     * @return 分类列表
     */
    java.util.List<cn.hgy.domain.project.model.entity.ProjectCategoryEntity> queryVisibleCategories();

    /**
     * 根据分类ID查询分类
     * @param categoryId 分类ID
     * @return 分类实体
     */
    cn.hgy.domain.project.model.entity.ProjectCategoryEntity queryCategoryById(String categoryId);

    /**
     * 根据承接团队ID查询项目列表
     * @param teamId 团队ID
     * @param status 项目状态（可选）
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 项目列表
     */
    List<ProjectEntity> queryProjectsByAcceptedTeamId(String teamId, String status, Integer offset, Integer limit);

    /**
     * 统计承接团队的项目数量
     * @param teamId 团队ID
     * @param status 项目状态（可选）
     * @return 项目数量
     */
    Long countProjectsByAcceptedTeamId(String teamId, String status);

    /**
     * 获取项目统计数据（各状态数量、时间维度统计）
     * @return 项目统计VO
     */
    ProjectStatisticsVO getProjectStatistics();

    /**
     * 获取项目分类分布统计
     * @return 分类分布列表
     */
    List<CategoryDistributionVO> getCategoryDistribution();
}
