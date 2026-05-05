package cn.hgy.trigger.http;

import cn.hgy.api.dto.MilestoneDeadlineUpdateRequestDTO;
import cn.hgy.api.response.Response;
import cn.hgy.domain.project.service.IMilestoneService;
import cn.hgy.trigger.interceptor.UserContextDynamic;
import cn.hgy.types.enums.ResponseCode;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-11-25
 * @Description: 里程碑期限更新接口（对企业开放）
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/milestone/deadline")
public class MilestoneDeadlineHttpController {

    private final IMilestoneService milestoneService;

    @PostMapping("update")
    public Response<Boolean> updateDeadline(@RequestBody MilestoneDeadlineUpdateRequestDTO dto, HttpServletRequest request) {
        String userId = UserContextDynamic.getUserContextDynamic();
        log.info("企业更新里程碑期限 userId={}, milestoneId={}, plannedStartTime={}, plannedEndTime={}, remindBeforeHours={}",
                userId, dto.getMilestoneId(), dto.getPlannedStartTime(), dto.getPlannedEndTime(), dto.getRemindBeforeHours());

        if (dto == null || StrUtil.isBlank(dto.getMilestoneId())) {
            return new Response<>(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo(), false);
        }

        boolean result = milestoneService.updateMilestoneDeadline(
                dto.getMilestoneId(),
                dto.getPlannedStartTime(),
                dto.getPlannedEndTime(),
                dto.getRemindBeforeHours()
        );
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }
}

