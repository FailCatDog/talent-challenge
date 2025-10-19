package cn.hgy.api;

import cn.hgy.api.dto.*;
import cn.hgy.api.response.Response;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-10-07
 * @Description: 项目业务app请求服务接口
 */
public interface IProjectHttpService {

    /**
     * 企业创建项目
     * @param dto 创建项目请求参数对象
     * @return 是否成功
     */
    Response<Boolean> createProject(ProjectCreateRequestDTO dto);

    /**
     * 企业保存项目草稿
     * @param dto 项目创建请求对象
     * @return 保存结果
     */
    Response<Boolean> saveDraft(ProjectCreateRequestDTO dto);

    /**
     * 企业更新项目
     * @param dto 项目更新请求对象
     * @return 更新结果
     */
    Response<Boolean> updateProject(ProjectUpdateRequestDTO dto);

    /**
     * 查询项目详情
     * @param projectId 项目ID
     * @return 项目详情
     */
    Response<ProjectDetailResponseDTO> getProjectDetail(String projectId);

    /**
     * 查询企业自己的项目列表
     * @param status 项目状态（可选）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 项目列表
     */
    Response<ProjectListResponseDTO> getMyProjects(String status, Integer pageNum, Integer pageSize);

    /**
     * 企业提交项目审核
     * @param projectId 项目ID
     * @return 提交结果
     */
    Response<Boolean> submitForReview(String projectId);

    /**
     * 学生浏览已发布项目列表
     * @param categoryId 分类ID（可选）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 项目列表
     */
    Response<ProjectListResponseDTO> getPublishedProjects(String categoryId, Integer pageNum, Integer pageSize);

    /**
     * 学生搜索项目
     * @param dto 搜索条件
     * @return 项目列表
     */
    Response<ProjectListResponseDTO> searchProjects(ProjectSearchRequestDTO dto);

    /**
     * 获取热门项目列表
     * @param limit 数量限制
     * @return 项目列表
     */
    Response<ProjectListResponseDTO> getPopularProjects(Integer limit);

    /**
     * 获取最新项目列表
     * @param limit 数量限制
     * @return 项目列表
     */
    Response<ProjectListResponseDTO> getLatestProjects(Integer limit);

    /**
     * 管理员获取待审核项目列表
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 项目列表
     */
    Response<ProjectListResponseDTO> getPendingReviewProjects(Integer pageNum, Integer pageSize);

    /**
     * 管理员审核通过项目
     * @param projectId 项目ID
     * @return 审核结果
     */
    Response<Boolean> approveProject(String projectId);

    /**
     * 管理员拒绝项目
     * @param dto 拒绝请求对象
     * @return 拒绝结果
     */
    Response<Boolean> rejectProject(ProjectRejectRequestDTO dto);
}
