package cn.hgy.domain.team.service;

import cn.hgy.api.dto.TeamMemberItemDTO;
import cn.hgy.domain.team.model.entity.TeamEntity;
import cn.hgy.domain.team.model.valobj.TeamStatusEnum;
import cn.hgy.domain.team.repository.ITeamRepository;
import cn.hgy.types.common.TeamCommand;
import cn.hgy.types.enums.ResponseCode;
import cn.hgy.types.exception.AppException;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService implements ITeamService {

    private final Snowflake snowflake;
    private final ITeamRepository teamRepository;

    @Override
    public String createTeam(TeamCommand command) {
        if (command == null || StrUtil.isBlank(command.getTeamName()) || StrUtil.isBlank(command.getLeaderId())) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        if (StrUtil.isNotBlank(command.getTeamName()) && command.getTeamName().length() > 100) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "团队名称长度不能超过100个字符");
        }
        if (command.getMaxMemberCount() != null && (command.getMaxMemberCount() < 1 || command.getMaxMemberCount() > 50)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "最大成员数量必须在1-50之间");
        }
        if (teamRepository.existsByLeaderIdAndStatus(command.getLeaderId(), TeamStatusEnum.ACTIVE.getCode())) {
            throw new AppException(ResponseCode.TEAM_ALREADY_EXISTS.getCode(), ResponseCode.TEAM_ALREADY_EXISTS.getInfo());
        }
        LocalDateTime now = LocalDateTime.now();
        TeamEntity teamEntity = TeamEntity.builder()
                .teamId(snowflake.nextIdStr())
                .teamName(command.getTeamName())
                .leaderId(command.getLeaderId())
                .description(command.getDescription())
                .avatarUrl(command.getAvatarUrl())
                .status(TeamStatusEnum.ACTIVE.getCode())
                .memberCount(1)
                .maxMemberCount(command.getMaxMemberCount() == null ? 10 : command.getMaxMemberCount())
                .createTime(now)
                .updateTime(now)
                .build();
        boolean saved = teamRepository.save(teamEntity);
        if (!saved) {
            throw new AppException(ResponseCode.TEAM_CREATE_FAIL.getCode(), ResponseCode.TEAM_CREATE_FAIL.getInfo());
        }
        return teamEntity.getTeamId();
    }

    @Override
    public boolean updateTeam(TeamCommand command, String leaderId) {
        if (command == null || StrUtil.isBlank(command.getTeamId()) || StrUtil.isBlank(leaderId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        TeamEntity teamEntity = loadTeam(command.getTeamId());
        if (!StrUtil.equals(teamEntity.getLeaderId(), leaderId)) {
            throw new AppException(ResponseCode.TEAM_PERMISSION_DENIED.getCode(), ResponseCode.TEAM_PERMISSION_DENIED.getInfo());
        }
        if (TeamStatusEnum.DISBANDED.getCode().equals(teamEntity.getStatus())) {
            throw new AppException(ResponseCode.TEAM_STATUS_INVALID.getCode(), "已解散的团队无法更新");
        }
        if (StrUtil.isNotBlank(command.getTeamName()) && command.getTeamName().length() > 100) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "团队名称长度不能超过100个字符");
        }
        if (command.getMaxMemberCount() != null && (command.getMaxMemberCount() < 1 || command.getMaxMemberCount() > 50)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "最大成员数量必须在1-50之间");
        }
        if (command.getMaxMemberCount() != null && command.getMaxMemberCount() < teamEntity.getMemberCount()) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "最大成员数量不能小于当前成员数量");
        }
        if (StrUtil.isNotBlank(command.getTeamName())) {
            teamEntity.setTeamName(command.getTeamName());
        }
        if (command.getDescription() != null) {
            teamEntity.setDescription(command.getDescription());
        }
        if (command.getAvatarUrl() != null) {
            teamEntity.setAvatarUrl(command.getAvatarUrl());
        }
        if (command.getMaxMemberCount() != null) {
            teamEntity.setMaxMemberCount(command.getMaxMemberCount());
        }
        teamEntity.setUpdateTime(LocalDateTime.now());
        boolean updated = teamRepository.update(teamEntity);
        if (!updated) {
            throw new AppException(ResponseCode.TEAM_UPDATE_FAIL.getCode(), ResponseCode.TEAM_UPDATE_FAIL.getInfo());
        }
        return true;
    }

    @Override
    public boolean disbandTeam(String teamId, String leaderId) {
        if (StrUtil.isBlank(teamId) || StrUtil.isBlank(leaderId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        TeamEntity teamEntity = loadTeam(teamId);
        if (!StrUtil.equals(teamEntity.getLeaderId(), leaderId)) {
            throw new AppException(ResponseCode.TEAM_PERMISSION_DENIED.getCode(), ResponseCode.TEAM_PERMISSION_DENIED.getInfo());
        }
        if (TeamStatusEnum.DISBANDED.getCode().equals(teamEntity.getStatus())) {
            throw new AppException(ResponseCode.TEAM_STATUS_INVALID.getCode(), "团队已解散");
        }
        teamEntity.setStatus(TeamStatusEnum.DISBANDED.getCode());
        teamEntity.setUpdateTime(LocalDateTime.now());
        boolean updated = teamRepository.update(teamEntity);
        if (!updated) {
            throw new AppException(ResponseCode.TEAM_DISBAND_FAIL.getCode(), ResponseCode.TEAM_DISBAND_FAIL.getInfo());
        }
        return true;
    }

    @Override
    public TeamEntity getTeamDetail(String teamId) {
        return loadTeam(teamId);
    }

    @Override
    public TeamEntity getTeamByLeaderId(String leaderId) {
        if (StrUtil.isBlank(leaderId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        return teamRepository.queryByLeaderId(leaderId)
                .orElse(null);
    }

    @Override
    public List<TeamEntity> getTeamsByLeaderId(String leaderId, Integer pageNum, Integer pageSize) {
        if (StrUtil.isBlank(leaderId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        int currentPage = pageNum == null || pageNum <= 0 ? 1 : pageNum;
        int size = pageSize == null || pageSize <= 0 ? 10 : pageSize;
        int offset = (currentPage - 1) * size;
        return teamRepository.queryByLeaderIdList(leaderId, offset, size);
    }

    @Override
    public Long countTeamsByLeaderId(String leaderId) {
        if (StrUtil.isBlank(leaderId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        return teamRepository.countByLeaderId(leaderId);
    }

    @Override
    public List<TeamMemberItemDTO> getTeamMembers(String teamId) {
        if (StrUtil.isBlank(teamId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        return teamRepository.queryMembersByTeamId(teamId);
    }

    private TeamEntity loadTeam(String teamId) {
        if (StrUtil.isBlank(teamId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        return teamRepository.queryByTeamId(teamId).orElseThrow(() ->
                new AppException(ResponseCode.TEAM_NOT_FOUND.getCode(), ResponseCode.TEAM_NOT_FOUND.getInfo()));
    }
}

