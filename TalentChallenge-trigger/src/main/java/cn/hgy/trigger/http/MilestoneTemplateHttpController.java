package cn.hgy.trigger.http;

import cn.hgy.api.IMilestoneTemplateHttpService;
import cn.hgy.api.dto.*;
import cn.hgy.api.response.Response;
import cn.hgy.domain.project.model.entity.MilestoneTemplateEntity;
import cn.hgy.domain.project.service.IMilestoneTemplateService;
import cn.hgy.trigger.interceptor.UserContextDynamic;
import cn.hgy.types.enums.ResponseCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-11-25
 * @Description: 里程碑模板HTTP控制器
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/milestone/template/")
public class MilestoneTemplateHttpController implements IMilestoneTemplateHttpService {
    
    private final IMilestoneTemplateService templateService;
    
    @GetMapping("all")
    @Override
    public Response<MilestoneTemplateListResponseDTO> getAllTemplates(HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        log.info("查询所有里程碑模板 userId={}", userId);
        
        List<MilestoneTemplateEntity> templates = templateService.getAllAvailableTemplates();
        List<MilestoneTemplateResponseDTO> dtos = templates.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        MilestoneTemplateListResponseDTO response = MilestoneTemplateListResponseDTO.builder()
                .templates(dtos)
                .build();
        
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), response);
    }
    
    @GetMapping("optional")
    @Override
    public Response<MilestoneTemplateListResponseDTO> getOptionalTemplates(HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        log.info("查询可选里程碑模板 userId={}", userId);
        
        List<MilestoneTemplateEntity> templates = templateService.getOptionalTemplates();
        List<MilestoneTemplateResponseDTO> dtos = templates.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        MilestoneTemplateListResponseDTO response = MilestoneTemplateListResponseDTO.builder()
                .templates(dtos)
                .build();
        
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), response);
    }
    
    @PostMapping("create")
    @Override
    public Response<Boolean> createMilestonesFromTemplates(@RequestBody MilestoneTemplateCreateRequestDTO dto, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        log.info("从模板创建里程碑 userId={}, projectId={}, templateIds={}", userId, dto.getProjectId(), dto.getTemplateIds());
        
        try {
            templateService.batchCreateMilestonesFromTemplates(dto.getProjectId(), dto.getTemplateIds());
            return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), true);
        } catch (Exception e) {
            log.error("从模板创建里程碑失败", e);
            return new Response<>(ResponseCode.MILESTONE_CREATE_FAIL.getCode(), 
                    ResponseCode.MILESTONE_CREATE_FAIL.getInfo(), false);
        }
    }
    
    private MilestoneTemplateResponseDTO convertToDTO(MilestoneTemplateEntity entity) {
        return MilestoneTemplateResponseDTO.builder()
                .templateId(entity.getTemplateId())
                .milestoneCode(entity.getMilestoneCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .milestoneType(entity.getMilestoneType())
                .defaultSequenceOrder(entity.getDefaultSequenceOrder())
                .defaultApprovalRule(entity.getDefaultApprovalRule())
                .defaultRemindBeforeHours(entity.getDefaultRemindBeforeHours())
                .isEnabled(entity.getIsEnabled())
                .build();
    }
}

