package cn.hgy.trigger.http;

import cn.hgy.api.IMilestoneHttpService;
import cn.hgy.api.dto.*;
import cn.hgy.api.response.Response;
import cn.hgy.domain.project.model.entity.ProjectMilestoneEntity;
import cn.hgy.domain.project.service.IMilestoneService;
import cn.hgy.infrastructure.storage.StorageService;
import cn.hgy.trigger.interceptor.UserContextDynamic;
import cn.hgy.types.enums.ResponseCode;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-11-25
 * @Description: 里程碑HTTP控制器
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/milestone")
public class MilestoneHttpController implements IMilestoneHttpService {
    
    private final IMilestoneService milestoneService;
    private final StorageService storageService;
    
    @GetMapping("project/{projectId}")
    @Override
    public Response<MilestoneListResponseDTO> getMilestonesByProjectId(@PathVariable(value = "projectId") String projectId, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        Integer userType = UserContextDynamic.getUserType();
        log.info("查询项目里程碑列表 userId={}, userType={}, projectId={}", userId, userType, projectId);

        // 查询放宽：企业 / 团队 / 学生均可查询；如需更细权限，可在此处细分
        ensureLogin(userId);

        List<ProjectMilestoneEntity> entities = milestoneService.getMilestonesByProjectId(projectId);
        List<MilestoneResponseDTO> dtos = new ArrayList<>();
        for (ProjectMilestoneEntity entity : entities) {
            dtos.add(convertToDTO(entity));
        }

        MilestoneListResponseDTO response = MilestoneListResponseDTO.builder()
                .milestones(dtos)
                .total(dtos.size())
                .build();

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), response);
    }
    
    @GetMapping("{milestoneId}")
    @Override
    public Response<MilestoneResponseDTO> getMilestoneDetail(@PathVariable("milestoneId") String milestoneId, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        Integer userType = UserContextDynamic.getUserType();
        log.info("查询里程碑详情 userId={}, userType={}, milestoneId={}", userId, userType, milestoneId);
        ensureLogin(userId);
        
        ProjectMilestoneEntity entity = milestoneService.getMilestoneById(milestoneId);
        MilestoneResponseDTO dto = convertToDTO(entity);
        
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), dto);
    }
    
    @GetMapping("project/{projectId}/code/{milestoneCode}")
    @Override
    public Response<MilestoneResponseDTO> getMilestoneByProjectIdAndCode(
            @PathVariable("projectId") String projectId,
            @PathVariable("milestoneCode") String milestoneCode, 
            HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        Integer userType = UserContextDynamic.getUserType();
        log.info("按编码查询里程碑 userId={}, userType={}, projectId={}, milestoneCode={}", userId, userType, projectId, milestoneCode);
        ensureLogin(userId);
        
        ProjectMilestoneEntity entity = milestoneService.getMilestoneByProjectIdAndCode(projectId, milestoneCode);
        MilestoneResponseDTO dto = convertToDTO(entity);
        
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), dto);
    }
    
    @PostMapping("deliverable")
    @Override
    public Response<Boolean> submitMilestoneDeliverable(@RequestBody MilestoneDeliverableRequestDTO dto, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        log.info("提交里程碑成果 userId={}, milestoneId={}, deliverableUrl={}", userId, dto.getMilestoneId(), dto.getDeliverableUrl());
        
        if (dto == null || StrUtil.isBlank(dto.getMilestoneId()) || StrUtil.isBlank(dto.getDeliverableUrl())) {
            return new Response<>(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo(), false);
        }
        
        try {
            boolean result = milestoneService.submitMilestoneDeliverable(dto.getMilestoneId(), dto.getDeliverableUrl());
            return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
        } catch (Exception e) {
            log.error("提交里程碑成果失败", e);
            return new Response<>(ResponseCode.MILESTONE_CREATE_FAIL.getCode(), e.getMessage(), false);
        }
    }

    /**
     * 文件上传版：提交里程碑成果
     * Content-Type: multipart/form-data
     * 参数：milestoneId（表单字段）、file（文件字段）
     */
    @PostMapping(value = "deliverable/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response<Boolean> uploadMilestoneDeliverable(@RequestParam("milestoneId") String milestoneId,
                                                        @RequestParam("file") MultipartFile file,
                                                        HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        log.info("上传里程碑成果文件 userId={}, milestoneId={}, fileName={}", userId, milestoneId,
                file != null ? file.getOriginalFilename() : null);

        if (StrUtil.isBlank(milestoneId) || file == null || file.isEmpty()) {
            return new Response<>(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo(), false);
        }

        try {
            // 查询里程碑获取 projectId
            ProjectMilestoneEntity entity = milestoneService.getMilestoneById(milestoneId);
            String projectId = entity.getProjectId();

            // 存储文件并获取访问URL
            String url = storageService.storeProposalFile(projectId, milestoneId, file);

            // 走原有业务逻辑：提交成果 + 状态流转
            boolean result = milestoneService.submitMilestoneDeliverable(milestoneId, url);
            return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
        } catch (Exception e) {
            log.error("上传里程碑成果文件失败", e);
            return new Response<>(ResponseCode.MILESTONE_CREATE_FAIL.getCode(), e.getMessage(), false);
        }
    }
    
    @PostMapping("approve")
    @Override
    public Response<Boolean> approveMilestone(@RequestBody MilestoneApprovalRequestDTO dto, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        log.info("审批里程碑 userId={}, milestoneId={}, approved={}", userId, dto.getMilestoneId(), dto.getApproved());
        
        if (dto == null || StrUtil.isBlank(dto.getMilestoneId()) || dto.getApproved() == null) {
            return new Response<>(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo(), false);
        }
        
        try {
            boolean result = milestoneService.approveMilestone(dto.getMilestoneId(), userId, dto.getApproved(), dto.getRemark());
            return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
        } catch (Exception e) {
            log.error("审批里程碑失败", e);
            return new Response<>(ResponseCode.MILESTONE_CREATE_FAIL.getCode(), e.getMessage(), false);
        }
    }
    
    @PostMapping("status")
    @Override
    public Response<Boolean> updateMilestoneStatus(@RequestBody MilestoneStatusUpdateRequestDTO dto, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        log.info("更新里程碑状态 userId={}, milestoneId={}, status={}", userId, dto.getMilestoneId(), dto.getStatus());
        
        if (dto == null || StrUtil.isBlank(dto.getMilestoneId()) || StrUtil.isBlank(dto.getStatus())) {
            return new Response<>(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo(), false);
        }
        
        try {
            boolean result = milestoneService.updateMilestoneStatus(dto.getMilestoneId(), dto.getStatus());
            return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
        } catch (Exception e) {
            log.error("更新里程碑状态失败", e);
            return new Response<>(ResponseCode.MILESTONE_CREATE_FAIL.getCode(), e.getMessage(), false);
        }
    }
    
    @PostMapping("update")
    @Override
    public Response<Boolean> updateMilestone(@RequestBody MilestoneUpdateRequestDTO dto, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        log.info("更新里程碑信息 userId={}, milestoneId={}", userId, dto.getMilestoneId());
        
        if (dto == null || StrUtil.isBlank(dto.getMilestoneId())) {
            return new Response<>(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo(), false);
        }
        
        try {
            ProjectMilestoneEntity entity = milestoneService.getMilestoneById(dto.getMilestoneId());
            
            if (StrUtil.isNotBlank(dto.getDescription())) {
                entity.setDescription(dto.getDescription());
            }
            if (dto.getPlannedStartTime() != null) {
                entity.setPlannedStartTime(dto.getPlannedStartTime());
            }
            if (dto.getPlannedEndTime() != null) {
                entity.setPlannedEndTime(dto.getPlannedEndTime());
            }
            if (dto.getRemindBeforeHours() != null) {
                entity.setRemindBeforeHours(dto.getRemindBeforeHours());
            }
            
            boolean result = milestoneService.updateMilestone(entity);
            return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
        } catch (Exception e) {
            log.error("更新里程碑信息失败", e);
            return new Response<>(ResponseCode.MILESTONE_CREATE_FAIL.getCode(), e.getMessage(), false);
        }
    }
    
    @PostMapping("skip")
    @Override
    public Response<Boolean> skipMilestone(@RequestBody MilestoneSkipRequestDTO dto, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        log.info("跳过里程碑 userId={}, milestoneId={}, skipReason={}", userId, dto.getMilestoneId(), dto.getSkipReason());
        
        if (dto == null || StrUtil.isBlank(dto.getMilestoneId())) {
            return new Response<>(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo(), false);
        }
        
        try {
            // 先取出实体，保存 skipReason 和 isSkipped，再整体更新
            ProjectMilestoneEntity entity = milestoneService.getMilestoneById(dto.getMilestoneId());
            entity.setStatus("skipped");
            entity.setIsSkipped(true);
            entity.setSkipReason(dto.getSkipReason());
            boolean result = milestoneService.updateMilestone(entity);
            return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
        } catch (Exception e) {
            log.error("跳过里程碑失败", e);
            return new Response<>(ResponseCode.MILESTONE_CREATE_FAIL.getCode(), e.getMessage(), false);
        }
    }

    /**
     * 学生上传里程碑交付物文件（支持多文件）
     * Content-Type: multipart/form-data
     * 参数：milestoneId（表单字段）、file（文件字段）
     */
    @PostMapping(value = "deliverable/files/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response<String> uploadMilestoneDeliverableFile(
            @RequestParam("milestoneId") String milestoneId,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        try {
            String userId = UserContextDynamic.getUserContextDynamic();
            ensureLogin(userId);

            if (StrUtil.isBlank(milestoneId)) {
                return new Response<>(ResponseCode.ILLEGAL_PARAMETER.getCode(), "里程碑ID不能为空", null);
            }

            ProjectMilestoneEntity milestone = milestoneService.getMilestoneById(milestoneId);
            if (milestone == null) {
                return new Response<>(ResponseCode.ILLEGAL_PARAMETER.getCode(), "里程碑不存在", null);
            }

            String fileUrl = storageService.storeProposalFile(milestone.getProjectId(), milestoneId, file);

            JSONArray fileList;
            String existingFiles = milestone.getDeliverableFiles();
            if (StrUtil.isBlank(existingFiles)) {
                fileList = new JSONArray();
            } else {
                fileList = JSON.parseArray(existingFiles);
            }

            JSONObject fileInfo = new JSONObject();
            fileInfo.put("fileName", file.getOriginalFilename());
            fileInfo.put("fileUrl", fileUrl);
            fileInfo.put("fileSize", file.getSize());
            fileInfo.put("uploadTime", java.time.LocalDateTime.now().toString());
            fileList.add(fileInfo);

            milestone.setDeliverableFiles(fileList.toJSONString());
            milestoneService.updateMilestone(milestone);

            log.info("上传里程碑交付物文件成功 milestoneId={}, fileUrl={}, 当前文件数={}", milestoneId, fileUrl, fileList.size());
            return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), fileUrl);
        } catch (Exception e) {
            log.error("上传里程碑交付物文件失败 milestoneId={}", milestoneId, e);
            return new Response<>(ResponseCode.UN_ERROR.getCode(), "上传失败：" + e.getMessage(), null);
        }
    }

    /**
     * 企业上传里程碑参考文件（要求文档、规范文件等）
     * 自动将文件信息追加到里程碑的 referenceFiles 字段
     */
    @PostMapping(value = "reference/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response<String> uploadMilestoneReferenceFile(
            @RequestParam("milestoneId") String milestoneId,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        try {
            String userId = UserContextDynamic.getUserContextDynamic();
            ensureLogin(userId);

            if (StrUtil.isBlank(milestoneId)) {
                return new Response<>(ResponseCode.ILLEGAL_PARAMETER.getCode(), "里程碑ID不能为空", null);
            }

            ProjectMilestoneEntity milestone = milestoneService.getMilestoneById(milestoneId);
            if (milestone == null) {
                return new Response<>(ResponseCode.ILLEGAL_PARAMETER.getCode(), "里程碑不存在", null);
            }

            String fileUrl = storageService.storeMilestoneReferenceFile(milestone.getProjectId(), milestoneId, file);

            JSONArray fileList;
            String existingFiles = milestone.getReferenceFiles();
            if (StrUtil.isBlank(existingFiles)) {
                fileList = new JSONArray();
            } else {
                fileList = JSON.parseArray(existingFiles);
            }

            JSONObject fileInfo = new JSONObject();
            fileInfo.put("fileName", file.getOriginalFilename());
            fileInfo.put("fileUrl", fileUrl);
            fileInfo.put("fileSize", file.getSize());
            fileInfo.put("uploadTime", java.time.LocalDateTime.now().toString());
            fileList.add(fileInfo);

            milestone.setReferenceFiles(fileList.toJSONString());
            milestoneService.updateMilestone(milestone);

            log.info("上传里程碑参考文件成功 milestoneId={}, fileUrl={}, 当前文件数={}", milestoneId, fileUrl, fileList.size());
            return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), fileUrl);
        } catch (Exception e) {
            log.error("上传里程碑参考文件失败 milestoneId={}", milestoneId, e);
            return new Response<>(ResponseCode.UN_ERROR.getCode(), "上传失败：" + e.getMessage(), null);
        }
    }

    private MilestoneResponseDTO convertToDTO(ProjectMilestoneEntity entity) {
        return MilestoneResponseDTO.builder()
                .milestoneId(entity.getMilestoneId())
                .projectId(entity.getProjectId())
                .milestoneCode(entity.getMilestoneCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .sequenceOrder(entity.getSequenceOrder())
                .status(entity.getStatus())
                .progressPercent(entity.getProgressPercent())
                .deliverableUrl(entity.getDeliverableUrl())
                .deliverableFiles(entity.getDeliverableFiles())
                .referenceFiles(entity.getReferenceFiles())
                .approvalStatus(entity.getApprovalStatus())
                .currentOwnerId(entity.getCurrentOwnerId())
                .plannedStartTime(entity.getPlannedStartTime())
                .plannedEndTime(entity.getPlannedEndTime())
                .actualStartTime(entity.getActualStartTime())
                .actualEndTime(entity.getActualEndTime())
                .autoExtendedCount(entity.getAutoExtendedCount())
                .remindBeforeHours(entity.getRemindBeforeHours())
                .isSkipped(entity.getIsSkipped())
                .skipReason(entity.getSkipReason())
                .remark(entity.getRemark())
                .build();
    }

    /**
     * 简单登录校验：仅检查 userId 是否存在
     */
    private void ensureLogin(String userId) {
        if (StrUtil.isBlank(userId)) {
            throw new cn.hgy.types.exception.AppException(
                    cn.hgy.types.enums.ResponseCode.ILLEGAL_PARAMETER.getCode(),
                    cn.hgy.types.enums.ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
    }
}

