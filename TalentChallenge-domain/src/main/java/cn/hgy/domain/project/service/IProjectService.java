package cn.hgy.domain.project.service;

import cn.hgy.domain.project.model.entity.ProjectEntity;
import cn.hgy.types.common.ProjectComment;

import java.util.List;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-10-07
 * @Description: 项目领域服务
 */
public interface IProjectService {

    /**
     * 企业创建项目
     * @param projectComment 项目命令对象
     * @return 创建是否成功布尔值
     */
    boolean createProject(ProjectComment projectComment);

    /**
     * 企业保存项目草稿
     * @param projectComment 项目命令对象
     * @return 保存是否成功布尔值
     */
    boolean saveDraft(ProjectComment projectComment);

    /**
     * 企业更新项目
     * @param projectComment 项目命令对象
     * @param publisherId 发布者ID
     * @return 更新是否成功布尔值
     */
    boolean updateProject(ProjectComment projectComment, String publisherId);

    /**
     * 查询项目详情
     * @param projectId 项目ID
     * @return 项目实体
     */
    ProjectEntity getProjectDetail(String projectId);

    /**
     * 查询企业自己的项目列表
     * @param publisherId 发布者ID
     * @param status 项目状态（可选）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 项目列表和总数
     */
    List<ProjectEntity> getProjectsByPublisher(String publisherId, String status, Integer pageNum, Integer pageSize);

    /**
     * 统计企业项目总数
     * @param publisherId 发布者ID
     * @param status 项目状态（可选）
     * @return 总数
     */
    Long countProjectsByPublisher(String publisherId, String status);

    /**
     * 企业提交项目审核
     * @param projectId 项目ID
     * @param publisherId 发布者ID
     * @return 提交是否成功布尔值
     */
    boolean submitForReview(String projectId, String publisherId);

    /**
     * 查询已发布项目列表
     * @param categoryId 分类ID（可选）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 项目列表
     */
    List<ProjectEntity> getPublishedProjects(String categoryId, Integer pageNum, Integer pageSize);

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
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 项目列表
     */
    List<ProjectEntity> searchProjects(String keyword, String categoryId, String skillLevel, 
                                       java.math.BigDecimal budgetMin, java.math.BigDecimal budgetMax,
                                       Integer pageNum, Integer pageSize);

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
    List<ProjectEntity> getPopularProjects(Integer limit);

    /**
     * 获取最新项目
     * @param limit 数量限制
     * @return 项目列表
     */
    List<ProjectEntity> getLatestProjects(Integer limit);

    /**
     * 增加项目浏览量
     * @param projectId 项目ID
     */
    void incrementViewCount(String projectId);

    /**
     * 管理员审核通过项目
     * @param projectId 项目ID
     * @return 审核是否成功
     */
    boolean approveProject(String projectId);

    /**
     * 管理员拒绝项目
     * @param projectId 项目ID
     * @param rejectReason 拒绝原因
     * @return 拒绝是否成功
     */
    boolean rejectProject(String projectId, String rejectReason);

    /**
     * 根据状态查询项目列表
     * @param status 项目状态
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 项目列表
     */
    List<ProjectEntity> getProjectsByStatus(String status, Integer pageNum, Integer pageSize);

    /**
     * 统计特定状态的项目数量
     * @param status 项目状态
     * @return 项目数量
     */
    Long countProjectsByStatus(String status);
}
