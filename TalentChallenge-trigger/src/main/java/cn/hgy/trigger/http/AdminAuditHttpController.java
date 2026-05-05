package cn.hgy.trigger.http;

import cn.hgy.api.dto.AuditHistoryListResponseDTO;
import cn.hgy.api.dto.AuditHistoryResponseDTO;
import cn.hgy.api.response.Response;
import cn.hgy.domain.audit.model.entity.AuditReviewRecordEntity;
import cn.hgy.domain.audit.service.IAuditHistoryService;
import cn.hgy.types.enums.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/audit")
public class AdminAuditHttpController {

    private final IAuditHistoryService auditHistoryService;

    @GetMapping("/history")
    public Response<AuditHistoryListResponseDTO> getAuditHistory(
            @RequestParam(value = "auditType", required = false) String auditType,
            @RequestParam(value = "targetId", required = false) String targetId,
            @RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        List<AuditReviewRecordEntity> history = auditHistoryService.queryHistory(auditType, targetId, action, pageNum, pageSize);
        long total = auditHistoryService.countHistory(auditType, targetId, action);
        AuditHistoryListResponseDTO dto = AuditHistoryListResponseDTO.builder()
                .list(history.stream().map(this::toDTO).toList())
                .total(total)
                .pageNum(pageNum)
                .pageSize(pageSize)
                .build();
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), dto);
    }

    private AuditHistoryResponseDTO toDTO(AuditReviewRecordEntity entity) {
        return AuditHistoryResponseDTO.builder()
                .recordId(entity.getRecordId())
                .auditType(entity.getAuditType())
                .targetId(entity.getTargetId())
                .targetName(entity.getTargetName())
                .action(entity.getAction())
                .fromStatus(entity.getFromStatus())
                .toStatus(entity.getToStatus())
                .reason(entity.getReason())
                .reviewerId(entity.getReviewerId())
                .reviewerName(entity.getReviewerName())
                .createTime(entity.getCreateTime())
                .build();
    }
}
