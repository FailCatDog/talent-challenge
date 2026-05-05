package cn.hgy.api;

import cn.hgy.api.dto.*;
import cn.hgy.api.response.Response;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-10-07
 * @Description: 项目业务app请求服务接口
 */
public interface IProjectHttpService {

    /**
     * 企业创建项目
     * @param dto 创建项目请求参数对象
     * @param request HTTP请求对象
     * @return 是否成功
     */
    Response<Boolean> createProject(ProjectCreateRequestDTO dto, HttpServletRequest request);

    /**
     * 企业保存项目草稿
     * @param dto 项目创建请求对象
     * @param request HTTP请求对象
     * @return 保存结果
     */
    Response<Boolean> saveDraft(ProjectCreateRequestDTO dto, HttpServletRequest request);

    /**
     * 企业更新项目
     * @param dto 项目更新请求对象
     * @param request HTTP请求对象
     * @return 更新结果
     */
    Response<Boolean> updateProject(ProjectUpdateRequestDTO dto, HttpServletRequest request);

    /**
     * 查询项目详情
     * @param projectId 项目ID
     * @param request HTTP请求对象
     * @return 项目详情
     */
    Response<ProjectDetailResponseDTO> getProjectDetail(String projectId, HttpServletRequest request);

    /**
     * 查询企业自己的项目列表
     * @param status 项目状态（可选）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param request HTTP请求对象
     * @return 项目列表
     */
    Response<ProjectListResponseDTO> getMyProjects(String status, Integer pageNum, Integer pageSize, HttpServletRequest request);

    /**
     * 企业提交项目审核
     * @param projectId 项目ID
     * @param request HTTP请求对象
     * @return 提交结果
     */
    Response<Boolean> submitForReview(String projectId, HttpServletRequest request);

    /**
     * 学生浏览已发布项目列表
     * @param categoryId 分类ID（可选）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param request HTTP请求对象
     * @return 项目列表
     */
    Response<ProjectListResponseDTO> getPublishedProjects(String categoryId, Integer pageNum, Integer pageSize, HttpServletRequest request);

    /**
     * 学生搜索项目
     * @param dto 搜索条件
     * @param request HTTP请求对象
     * @return 项目列表
     */
    Response<ProjectListResponseDTO> searchProjects(ProjectSearchRequestDTO dto, HttpServletRequest request);

    /**
     * 获取热门项目列表
     * @param limit 数量限制
     * @param request HTTP请求对象
     * @return 项目列表
     */
    Response<ProjectListResponseDTO> getPopularProjects(Integer limit, HttpServletRequest request);

    /**
     * 获取最新项目列表
     * @param limit 数量限制
     * @param request HTTP请求对象
     * @return 项目列表
     */
    Response<ProjectListResponseDTO> getLatestProjects(Integer limit, HttpServletRequest request);

    /**
     * 管理员获取项目列表（默认待审核，可按状态筛选）
     * @param status 项目状态（可选，为空则查全部）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param request HTTP请求对象
     * @return 项目列表
     */
    Response<ProjectListResponseDTO> getPendingReviewProjects(String status, Integer pageNum, Integer pageSize, HttpServletRequest request);

    /**
     * 管理员查看项目审核详情
     * @param projectId 项目ID
     * @param request HTTP请求对象
     * @return 项目详情
     */
    Response<ProjectDetailResponseDTO> getProjectReviewDetail(String projectId, HttpServletRequest request);

    /**
     * 管理员审核通过项目
     * @param projectId 项目ID
     * @param request HTTP请求对象
     * @return 审核结果
     */
    Response<Boolean> approveProject(String projectId, HttpServletRequest request);

    /**
     * 管理员拒绝项目
     * @param dto 拒绝请求对象
     * @param request HTTP请求对象
     * @return 拒绝结果
     */
    Response<Boolean> rejectProject(ProjectRejectRequestDTO dto, HttpServletRequest request);

    /**
     * 管理员创建项目分类
     * @param dto 创建分类请求对象
     * @param request HTTP请求对象
     * @return 创建结果
     */
    Response<Boolean> createProjectCategory(ProjectCategoryCreateRequestDTO dto, HttpServletRequest request);

    /**
     * 管理员更新项目分类
     * @param dto 更新分类请求对象
     * @param request HTTP请求对象
     * @return 更新结果
     */
    Response<Boolean> updateProjectCategory(ProjectCategoryUpdateRequestDTO dto, HttpServletRequest request);

    /**
     * 管理员删除项目分类
     * @param categoryId 分类ID
     * @param request HTTP请求对象
     * @return 删除结果
     */
    Response<Boolean> deleteProjectCategory(String categoryId, HttpServletRequest request);

    /**
     * 查询所有项目分类
     * @param request HTTP请求对象
     * @return 分类列表
     */
    Response<java.util.List<ProjectCategoryResponseDTO>> getAllCategories(HttpServletRequest request);

    /**
     * 查询所有可见的项目分类
     * @param request HTTP请求对象
     * @return 分类列表
     */
    Response<java.util.List<ProjectCategoryResponseDTO>> getVisibleCategories(HttpServletRequest request);

    /**
     * 根据分类ID查询分类详情
     * @param categoryId 分类ID
     * @param request HTTP请求对象
     * @return 分类详情
     */
    Response<ProjectCategoryResponseDTO> getCategoryById(String categoryId, HttpServletRequest request);

    /**
     * 企业直接发布草稿项目（跳过审核）
     * @param projectId 项目ID
     * @param request HTTP请求对象
     * @return 发布结果
     */
    Response<Boolean> publishDraftDirectly(String projectId, HttpServletRequest request);

    /**
     * 学生查询自己承接的项目列表
     * @param status 项目状态（可选）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param request HTTP请求对象
     * @return 项目列表
     */
    Response<ProjectListResponseDTO> getMyAcceptedProjects(String status, Integer pageNum, Integer pageSize, HttpServletRequest request);
    /**
     * 企业手动完结项目
     * @param projectId 项目ID
     * @param request HTTP请求
     * @return 完结结果
     */
    Response<Boolean> closeProject(String projectId, HttpServletRequest request);
}
