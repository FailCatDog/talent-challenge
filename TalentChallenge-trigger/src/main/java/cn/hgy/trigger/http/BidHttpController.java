package cn.hgy.trigger.http;

import cn.hgy.api.IBidHttpService;
import cn.hgy.api.dto.BidApplyRequestDTO;
import cn.hgy.api.dto.BidDecisionRequestDTO;
import cn.hgy.api.dto.BidListResponseDTO;
import cn.hgy.api.dto.BidResponseDTO;
import cn.hgy.api.response.Response;
import cn.hgy.domain.bid.model.entity.BidEntity;
import cn.hgy.domain.bid.model.valobj.BidStatusEnum;
import cn.hgy.domain.bid.service.IBidService;
import cn.hgy.domain.project.model.entity.ProjectEntity;
import cn.hgy.domain.project.service.IProjectService;
import cn.hgy.domain.team.model.entity.TeamEntity;
import cn.hgy.domain.team.repository.ITeamRepository;
import cn.hgy.trigger.interceptor.UserContextDynamic;
import cn.hgy.types.common.BidCommand;
import cn.hgy.types.enums.ResponseCode;
import cn.hgy.types.exception.AppException;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/bid")
public class BidHttpController implements IBidHttpService {

    private final IBidService bidService;
    private final IProjectService projectService;
    private final ITeamRepository teamRepository;

    private static final int STUDENT_TYPE = 1;
    private static final int COMPANY_TYPE = 2;

    @PostMapping("apply")
    @Override
    public Response<Boolean> apply(@RequestBody BidApplyRequestDTO dto, HttpServletRequest request) {
        ensureStudent(UserContextDynamic.getUserType());
        String userId = requireUserId();
        TeamEntity team = teamRepository.queryByLeaderId(userId)
                .orElseThrow(() -> new AppException(ResponseCode.BID_TEAM_REQUIRED.getCode(), ResponseCode.BID_TEAM_REQUIRED.getInfo()));
        if (!StrUtil.equals("active", team.getStatus())) {
            throw new AppException(ResponseCode.BID_TEAM_NOT_FOUND.getCode(), ResponseCode.BID_TEAM_NOT_FOUND.getInfo());
        }
        log.info("team {} apply bid, project {}", team.getTeamId(), dto.getProjectId());

        BidCommand command = BidCommand.builder()
                .bidId(null)
                .projectId(dto.getProjectId())
                .teamId(team.getTeamId())
                .content(dto.getContent())
                .attachmentUrl(dto.getAttachmentUrl())
                .build();

        boolean result = bidService.apply(command);
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    @GetMapping("project/{projectId}")
    @Override
    public Response<BidListResponseDTO> getProjectBids(@PathVariable("projectId") String projectId,
                                                       @RequestParam(value = "status", required = false) String status,
                                                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                       HttpServletRequest request) {
        ensureCompany(UserContextDynamic.getUserType());
        String operatorId = requireUserId();
        ProjectEntity project = projectService.getProjectDetail(projectId);
        if (!StrUtil.equals(project.getPublisherId(), operatorId)) {
            throw new AppException(ResponseCode.BID_PERMISSION_DENIED.getCode(), ResponseCode.BID_PERMISSION_DENIED.getInfo());
        }
        log.info("operator {} query bids, project {}", operatorId, projectId);

        BidStatusEnum statusEnum = null;
        if (StrUtil.isNotBlank(status)) {
            try {
                statusEnum = BidStatusEnum.valueOf(status.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ex) {
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
            }
        }

        List<BidEntity> entities = bidService.queryProjectBids(projectId, statusEnum, pageNum, pageSize);
        Long total = bidService.countProjectBids(projectId, statusEnum);

        List<BidResponseDTO> responses = new ArrayList<>();
        for (BidEntity entity : entities) {
            responses.add(BidResponseDTO.builder()
                    .bidId(entity.getBidId())
                    .projectId(entity.getProjectId())
                    .teamId(entity.getTeamId())
                    .status(entity.getStatus() != null ? entity.getStatus().getCode() : null)
                    .content(entity.getContent())
                    .attachmentUrl(entity.getAttachmentUrl())
                    .remark(entity.getRemark())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .build());
        }

        BidListResponseDTO responseDTO = BidListResponseDTO.builder()
                .bids(responses)
                .total(total)
                .pageNum(pageNum)
                .pageSize(pageSize)
                .build();

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), responseDTO);
    }

    @PostMapping("shortlist")
    @Override
    public Response<Boolean> shortlist(@RequestBody BidDecisionRequestDTO dto, HttpServletRequest request) {
        ensureCompany(UserContextDynamic.getUserType());
        String operatorId = requireUserId();
        log.info("operator {} shortlist bid {}", operatorId, dto.getBidId());

        boolean result = bidService.shortlist(dto.getBidId(), operatorId);
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    @PostMapping("confirm")
    @Override
    public Response<Boolean> confirm(@RequestBody BidDecisionRequestDTO dto, HttpServletRequest request) {
        ensureCompany(UserContextDynamic.getUserType());
        String operatorId = requireUserId();
        log.info("operator {} confirm bid {}", operatorId, dto.getBidId());

        boolean result = bidService.confirm(dto.getBidId(), operatorId);
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    @PostMapping("reject")
    @Override
    public Response<Boolean> reject(@RequestBody BidDecisionRequestDTO dto, HttpServletRequest request) {
        ensureCompany(UserContextDynamic.getUserType());
        String operatorId = requireUserId();
        log.info("operator {} reject bid {}", operatorId, dto.getBidId());

        boolean result = bidService.reject(dto.getBidId(), operatorId, dto.getRemark());
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    @PostMapping("cancel/{bidId}")
    @Override
    public Response<Boolean> cancel(@PathVariable("bidId") String bidId, HttpServletRequest request) {
        ensureStudent(UserContextDynamic.getUserType());
        String userId = requireUserId();
        TeamEntity team = teamRepository.queryByLeaderId(userId)
                .orElseThrow(() -> new AppException(ResponseCode.BID_TEAM_REQUIRED.getCode(), ResponseCode.BID_TEAM_REQUIRED.getInfo()));
        if (!StrUtil.equals("active", team.getStatus())) {
            throw new AppException(ResponseCode.BID_TEAM_NOT_FOUND.getCode(), ResponseCode.BID_TEAM_NOT_FOUND.getInfo());
        }
        log.info("team {} cancel bid {}", team.getTeamId(), bidId);

        boolean result = bidService.cancel(bidId, team.getTeamId());
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    @GetMapping("public/project/{projectId}")
    public Response<List<cn.hgy.api.dto.BidPublicListItemResponseDTO>> getPublicBidsByProject(@PathVariable("projectId") String projectId) {
        log.info("query public bids for project {}", projectId);
        List<cn.hgy.api.dto.BidPublicListItemResponseDTO> result = bidService.queryPublicBidsByProject(projectId);
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    @GetMapping("student/my-bids")
    public Response<List<cn.hgy.api.dto.StudentBidListResponseDTO>> getMyBids(HttpServletRequest request) {
        ensureStudent(UserContextDynamic.getUserType());
        String userId = requireUserId();
        log.info("student {} query my bids", userId);
        List<cn.hgy.api.dto.StudentBidListResponseDTO> result = bidService.queryStudentBids(userId);
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    @GetMapping("detail/{bidId}")
    public Response<cn.hgy.api.dto.BidDetailResponseDTO> getBidDetail(@PathVariable("bidId") String bidId, HttpServletRequest request) {
        String userId = requireUserId();
        log.info("user {} query bid detail {}", userId, bidId);
        cn.hgy.api.dto.BidDetailResponseDTO result = bidService.getBidDetail(bidId);
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    private void ensureStudent(Integer userType) {
        if (!Integer.valueOf(STUDENT_TYPE).equals(userType)) {
            throw new AppException(ResponseCode.BID_PERMISSION_DENIED.getCode(), ResponseCode.BID_PERMISSION_DENIED.getInfo());
        }
    }

    private void ensureCompany(Integer userType) {
        if (!Integer.valueOf(COMPANY_TYPE).equals(userType)) {
            throw new AppException(ResponseCode.BID_PERMISSION_DENIED.getCode(), ResponseCode.BID_PERMISSION_DENIED.getInfo());
        }
    }

    private String requireUserId() {
        String userId = UserContextDynamic.getUserContextDynamic();
        if (StrUtil.isBlank(userId)) {
            throw new AppException(ResponseCode.BID_PERMISSION_DENIED.getCode(), ResponseCode.BID_PERMISSION_DENIED.getInfo());
        }
        return userId;
    }
}

