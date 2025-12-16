package cn.hgy.api;

import cn.hgy.api.dto.*;
import cn.hgy.api.response.Response;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-11-25
 * @Description: 里程碑模板HTTP服务接口
 */
public interface IMilestoneTemplateHttpService {
    
    /**
     * 查询所有可用的里程碑模板
     * @param request HTTP请求对象
     * @return 模板列表
     */
    Response<MilestoneTemplateListResponseDTO> getAllTemplates(HttpServletRequest request);
    
    /**
     * 查询可选里程碑模板
     * @param request HTTP请求对象
     * @return 可选模板列表
     */
    Response<MilestoneTemplateListResponseDTO> getOptionalTemplates(HttpServletRequest request);
    
    /**
     * 从模板创建里程碑
     * @param dto 创建请求对象
     * @param request HTTP请求对象
     * @return 是否成功
     */
    Response<Boolean> createMilestonesFromTemplates(MilestoneTemplateCreateRequestDTO dto, HttpServletRequest request);
}

