package cn.hgy.trigger.http;

import cn.hgy.api.ITeamHttpService;
import cn.hgy.api.dto.TeamCreateRequestDTO;
import cn.hgy.api.dto.TeamInviteItemDTO;
import cn.hgy.api.dto.TeamInviteRequestDTO;
import cn.hgy.api.dto.TeamMemberItemDTO;
import cn.hgy.api.dto.TeamMemberRemoveRequestDTO;
import cn.hgy.api.dto.TeamResponseDTO;
import cn.hgy.api.dto.TeamSummaryResponseDTO;
import cn.hgy.api.dto.TeamUpdateRequestDTO;
import cn.hgy.api.response.Response;
import cn.hgy.domain.team.model.entity.TeamEntity;
import cn.hgy.domain.team.service.ITeamService;
import cn.hgy.trigger.interceptor.UserContextDynamic;
import cn.hgy.types.common.TeamCommand;
import cn.hgy.types.enums.ResponseCode;
import cn.hgy.types.exception.AppException;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/team")
public class TeamHttpController implements ITeamHttpService {

    private static final int STUDENT_TYPE = 1;

    private final ITeamService teamService;

    @PostMapping("create")
    @Transactional
    @Override
    public Response<String> createTeam(@RequestBody TeamCreateRequestDTO dto, HttpServletRequest request) {
        ensureStudent(UserContextDynamic.getUserType());
        String userId = requireUserId();
        log.info("student {} create team, teamName {}", userId, dto.getTeamName());

        TeamCommand command = TeamCommand.builder()
                .teamName(dto.getTeamName())
                .leaderId(userId)
                .description(dto.getDescription())
                .avatarUrl(dto.getAvatarUrl())
                .maxMemberCount(dto.getMaxMemberCount())
                .build();

        String teamId = teamService.createTeam(command);
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), teamId);
    }

    @PostMapping("update")
    @Override
    public Response<Boolean> updateTeam(@RequestBody TeamUpdateRequestDTO dto, HttpServletRequest request) {
        ensureStudent(UserContextDynamic.getUserType());
        String userId = requireUserId();
        log.info("student {} update team, teamId {}", userId, dto.getTeamId());

        TeamCommand command = TeamCommand.builder()
                .teamId(dto.getTeamId())
                .teamName(dto.getTeamName())
                .description(dto.getDescription())
                .avatarUrl(dto.getAvatarUrl())
                .maxMemberCount(dto.getMaxMemberCount())
                .build();

        boolean result = teamService.updateTeam(command, userId);
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    @PostMapping("disband/{teamId}")
    @Override
    public Response<Boolean> disbandTeam(@PathVariable("teamId") String teamId, HttpServletRequest request) {
        ensureStudent(UserContextDynamic.getUserType());
        String userId = requireUserId();
        log.info("student {} disband team, teamId {}", userId, teamId);

        boolean result = teamService.disbandTeam(teamId, userId);
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    @GetMapping("detail/{teamId}")
    @Override
    public Response<TeamResponseDTO> getTeamDetail(@PathVariable("teamId") String teamId, HttpServletRequest request) {
        String userId = requireUserId();
        log.info("user {} query team detail, teamId {}", userId, teamId);

        TeamEntity teamEntity = teamService.getTeamDetail(teamId);
        TeamResponseDTO responseDTO = convertToResponseDTO(teamEntity);
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), responseDTO);
    }

    @GetMapping("my/owned")
    @Override
    public Response<TeamResponseDTO> getMyOwnedTeam(HttpServletRequest request) {
        ensureStudent(UserContextDynamic.getUserType());
        String userId = requireUserId();
        log.info("student {} query my owned team", userId);

        TeamEntity teamEntity = teamService.getTeamByLeaderId(userId);
        if (teamEntity == null) {
            return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), null);
        }
        TeamResponseDTO responseDTO = convertToResponseDTO(teamEntity);
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), responseDTO);
    }

    @GetMapping("my/joined")
    @Override
    public Response<List<TeamSummaryResponseDTO>> getMyJoinedTeams(HttpServletRequest request) {
        ensureStudent(UserContextDynamic.getUserType());
        String userId = requireUserId();
        log.info("student {} query my joined teams", userId);

        List<TeamSummaryResponseDTO> responses = teamService.getJoinedTeams(userId).stream()
                .map(this::convertToSummaryDTO)
                .toList();
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), responses);
    }

    @GetMapping("my/joined/{teamId}")
    @Override
    public Response<TeamResponseDTO> getMyJoinedTeamDetail(@PathVariable("teamId") String teamId, HttpServletRequest request) {
        ensureStudent(UserContextDynamic.getUserType());
        String userId = requireUserId();
        log.info("student {} query joined team detail, teamId {}", userId, teamId);

        TeamEntity teamEntity = teamService.getJoinedTeamDetail(userId, teamId);
        TeamResponseDTO responseDTO = convertToResponseDTO(teamEntity);
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), responseDTO);
    }

    @PostMapping("member/remove")
    @Transactional
    @Override
    public Response<Boolean> removeMember(@RequestBody TeamMemberRemoveRequestDTO dto, HttpServletRequest request) {
        ensureStudent(UserContextDynamic.getUserType());
        String userId = requireUserId();
        log.info("student {} remove member {} from team {}", userId, dto.getUserId(), dto.getTeamId());

        boolean result = teamService.removeMember(dto.getTeamId(), userId, dto.getUserId());
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    @PostMapping("invite")
    @Transactional
    @Override
    public Response<Boolean> inviteMember(@RequestBody TeamInviteRequestDTO dto, HttpServletRequest request) {
        ensureStudent(UserContextDynamic.getUserType());
        String userId = requireUserId();
        log.info("student {} invite member {} to team {}", userId, dto.getInvitedUserId(), dto.getTeamId());

        boolean result = teamService.inviteMember(dto.getTeamId(), userId, dto.getInvitedUserId());
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    @GetMapping("invite/my")
    @Override
    public Response<List<TeamInviteItemDTO>> getMyPendingInvites(HttpServletRequest request) {
        ensureStudent(UserContextDynamic.getUserType());
        String userId = requireUserId();
        log.info("student {} query my pending team invites", userId);

        List<TeamInviteItemDTO> invites = teamService.getMyPendingInvites(userId);
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), invites);
    }

    @PostMapping("invite/agree/{inviteId}")
    @Transactional
    @Override
    public Response<Boolean> agreeInvite(@PathVariable("inviteId") String inviteId, HttpServletRequest request) {
        ensureStudent(UserContextDynamic.getUserType());
        String userId = requireUserId();
        log.info("student {} agree team invite {}", userId, inviteId);

        boolean result = teamService.agreeInvite(inviteId, userId);
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    @PostMapping("invite/reject/{inviteId}")
    @Transactional
    @Override
    public Response<Boolean> rejectInvite(@PathVariable("inviteId") String inviteId, HttpServletRequest request) {
        ensureStudent(UserContextDynamic.getUserType());
        String userId = requireUserId();
        log.info("student {} reject team invite {}", userId, inviteId);

        boolean result = teamService.rejectInvite(inviteId, userId);
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), result);
    }

    private void ensureStudent(Integer userType) {
        if (!Integer.valueOf(STUDENT_TYPE).equals(userType)) {
            throw new AppException(ResponseCode.TEAM_PERMISSION_DENIED.getCode(), ResponseCode.TEAM_PERMISSION_DENIED.getInfo());
        }
    }

    private String requireUserId() {
        String userId = UserContextDynamic.getUserContextDynamic();
        if (StrUtil.isBlank(userId)) {
            throw new AppException(ResponseCode.TEAM_PERMISSION_DENIED.getCode(), ResponseCode.TEAM_PERMISSION_DENIED.getInfo());
        }
        return userId;
    }

    private TeamResponseDTO convertToResponseDTO(TeamEntity entity) {
        List<TeamMemberItemDTO> members = teamService.getTeamMembers(entity.getTeamId());
        return TeamResponseDTO.builder()
                .teamId(entity.getTeamId())
                .teamName(entity.getTeamName())
                .leaderId(entity.getLeaderId())
                .description(entity.getDescription())
                .avatarUrl(entity.getAvatarUrl())
                .status(entity.getStatus())
                .memberCount(entity.getMemberCount())
                .maxMemberCount(entity.getMaxMemberCount())
                .createTime(entity.getCreateTime())
                .updateTime(entity.getUpdateTime())
                .members(members)
                .build();
    }

    private TeamSummaryResponseDTO convertToSummaryDTO(TeamEntity entity) {
        return TeamSummaryResponseDTO.builder()
                .teamId(entity.getTeamId())
                .teamName(entity.getTeamName())
                .leaderId(entity.getLeaderId())
                .description(entity.getDescription())
                .avatarUrl(entity.getAvatarUrl())
                .status(entity.getStatus())
                .memberCount(entity.getMemberCount())
                .maxMemberCount(entity.getMaxMemberCount())
                .build();
    }
}
