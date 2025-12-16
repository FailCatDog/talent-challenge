package cn.hgy.trigger.http;

import cn.hgy.api.ITeamHttpService;
import cn.hgy.api.dto.TeamCreateRequestDTO;
import cn.hgy.api.dto.TeamListResponseDTO;
import cn.hgy.api.dto.TeamResponseDTO;
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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/team/")
public class TeamHttpController implements ITeamHttpService {

    private static final int STUDENT_TYPE = 1;

    private final ITeamService teamService;

    @PostMapping("create")
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

    @GetMapping("my")
    @Override
    public Response<TeamResponseDTO> getMyTeam(HttpServletRequest request) {
        ensureStudent(UserContextDynamic.getUserType());
        String userId = requireUserId();
        log.info("student {} query my team", userId);

        TeamEntity teamEntity = teamService.getTeamByLeaderId(userId);
        if (teamEntity == null) {
            return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), null);
        }
        TeamResponseDTO responseDTO = convertToResponseDTO(teamEntity);
        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), responseDTO);
    }

    @GetMapping("my/list")
    @Override
    public Response<TeamListResponseDTO> getMyTeams(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest request) {
        ensureStudent(UserContextDynamic.getUserType());
        String userId = requireUserId();
        log.info("student {} query my teams, pageNum {}, pageSize {}", userId, pageNum, pageSize);

        List<TeamEntity> entities = teamService.getTeamsByLeaderId(userId, pageNum, pageSize);
        Long total = teamService.countTeamsByLeaderId(userId);

        List<TeamResponseDTO> responses = new ArrayList<>();
        for (TeamEntity entity : entities) {
            responses.add(convertToResponseDTO(entity));
        }

        TeamListResponseDTO responseDTO = TeamListResponseDTO.builder()
                .teams(responses)
                .total(total)
                .pageNum(pageNum)
                .pageSize(pageSize)
                .build();

        return new Response<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getInfo(), responseDTO);
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
                .build();
    }
}

