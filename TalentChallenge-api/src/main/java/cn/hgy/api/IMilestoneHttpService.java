package cn.hgy.api;

import cn.hgy.api.dto.*;
import cn.hgy.api.response.Response;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-11-13
 * @Description: 里程碑HTTP服务接口
 */
public interface IMilestoneHttpService {
    
    /**
     * 查询项目所有里程碑
     * @param projectId 项目ID
     * @param request HTTP请求对象
     * @return 里程碑列表
     */
    Response<MilestoneListResponseDTO> getMilestonesByProjectId(String projectId, HttpServletRequest request);
    
    /**
     * 查询里程碑详情
     * @param milestoneId 里程碑ID
     * @param request HTTP请求对象
     * @return 里程碑详情
     */
    Response<MilestoneResponseDTO> getMilestoneDetail(String milestoneId, HttpServletRequest request);
    
    /**
     * 根据项目ID和里程碑编码查询里程碑
     * @param projectId 项目ID
     * @param milestoneCode 里程碑编码
     * @param request HTTP请求对象
     * @return 里程碑详情
     */
    Response<MilestoneResponseDTO> getMilestoneByProjectIdAndCode(String projectId, String milestoneCode, HttpServletRequest request);
    
    /**
     * 提交里程碑成果
     * @param dto 提交成果请求对象
     * @param request HTTP请求对象
     * @return 是否成功
     */
    Response<Boolean> submitMilestoneDeliverable(MilestoneDeliverableRequestDTO dto, HttpServletRequest request);
    
    /**
     * 审批里程碑
     * @param dto 审批请求对象
     * @param request HTTP请求对象
     * @return 是否成功
     */
    Response<Boolean> approveMilestone(MilestoneApprovalRequestDTO dto, HttpServletRequest request);
    
    /**
     * 更新里程碑状态
     * @param dto 更新状态请求对象
     * @param request HTTP请求对象
     * @return 是否成功
     */
    Response<Boolean> updateMilestoneStatus(MilestoneStatusUpdateRequestDTO dto, HttpServletRequest request);
    
    /**
     * 跳过里程碑
     * @param dto 跳过里程碑请求对象
     * @param request HTTP请求对象
     * @return 是否成功
     */
    Response<Boolean> skipMilestone(MilestoneSkipRequestDTO dto, HttpServletRequest request);
}

