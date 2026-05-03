package cn.hgy.domain.team.service;

import cn.hgy.api.dto.TeamInviteItemDTO;
import cn.hgy.api.dto.TeamMemberItemDTO;
import cn.hgy.domain.team.model.entity.TeamEntity;
import cn.hgy.domain.team.model.entity.TeamInviteEntity;
import cn.hgy.domain.team.model.valobj.TeamInviteStatusEnum;
import cn.hgy.domain.team.model.valobj.TeamStatusEnum;
import cn.hgy.domain.team.repository.ITeamRepository;
import cn.hgy.domain.user.model.entity.UserEntity;
import cn.hgy.domain.user.model.valobj.UserTypeEnum;
import cn.hgy.domain.user.repository.IUserManagementRepository;
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
    private final IUserManagementRepository userManagementRepository;

    @Override
    public String createTeam(TeamCommand command) {
        if (command == null || StrUtil.isBlank(command.getTeamName()) || StrUtil.isBlank(command.getLeaderId())) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        if (command.getTeamName().length() > 100) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "团队名称长度不能超过100个字符");
        }
        if (command.getMaxMemberCount() != null && (command.getMaxMemberCount() < 1 || command.getMaxMemberCount() > 50)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "最大成员数量必须在1-50之间");
        }
        if (teamRepository.countByLeaderId(command.getLeaderId()) > 0) {
            throw new AppException(ResponseCode.TEAM_LEADER_LIMIT.getCode(), ResponseCode.TEAM_LEADER_LIMIT.getInfo());
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
        return teamRepository.queryByLeaderId(leaderId).orElse(null);
    }

    @Override
    public TeamEntity getActiveTeamByUserId(String userId) {
        if (StrUtil.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        return teamRepository.queryActiveTeamByUserId(userId).orElse(null);
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
    public List<TeamEntity> getJoinedTeams(String userId) {
        if (StrUtil.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        return teamRepository.queryJoinedTeamsByUserId(userId);
    }

    @Override
    public TeamEntity getJoinedTeamDetail(String userId, String teamId) {
        if (StrUtil.isBlank(userId) || StrUtil.isBlank(teamId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        return teamRepository.queryJoinedTeamByUserIdAndTeamId(userId, teamId)
                .orElseThrow(() -> new AppException(ResponseCode.TEAM_PERMISSION_DENIED.getCode(),
                        ResponseCode.TEAM_PERMISSION_DENIED.getInfo()));
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

    @Override
    public boolean isTeamMember(String teamId, String userId) {
        if (StrUtil.isBlank(teamId) || StrUtil.isBlank(userId)) {
            return false;
        }
        return teamRepository.existsMember(teamId, userId);
    }

    @Override
    public boolean removeMember(String teamId, String leaderId, String memberUserId) {
        if (StrUtil.isBlank(teamId) || StrUtil.isBlank(leaderId) || StrUtil.isBlank(memberUserId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        if (StrUtil.equals(leaderId, memberUserId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "不能移除队长");
        }

        TeamEntity teamEntity = loadTeam(teamId);
        if (!StrUtil.equals(teamEntity.getLeaderId(), leaderId)) {
            throw new AppException(ResponseCode.TEAM_PERMISSION_DENIED.getCode(), ResponseCode.TEAM_PERMISSION_DENIED.getInfo());
        }
        ensureTeamActive(teamEntity);

        boolean removed = teamRepository.removeMember(teamId, memberUserId);
        if (!removed) {
            throw new AppException(ResponseCode.TEAM_NOT_FOUND.getCode(), "团队成员不存在");
        }

        int currentCount = teamEntity.getMemberCount() == null ? 1 : teamEntity.getMemberCount();
        teamEntity.setMemberCount(Math.max(1, currentCount - 1));
        teamEntity.setUpdateTime(LocalDateTime.now());
        boolean teamUpdated = teamRepository.update(teamEntity);
        if (!teamUpdated) {
            throw new AppException(ResponseCode.TEAM_UPDATE_FAIL.getCode(), ResponseCode.TEAM_UPDATE_FAIL.getInfo());
        }
        return true;
    }

    @Override
    public boolean inviteMember(String teamId, String leaderId, String invitedUserId) {
        if (StrUtil.isBlank(teamId) || StrUtil.isBlank(leaderId) || StrUtil.isBlank(invitedUserId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        if (StrUtil.equals(leaderId, invitedUserId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "不能邀请自己加入队伍");
        }

        TeamEntity teamEntity = loadTeam(teamId);
        if (!StrUtil.equals(teamEntity.getLeaderId(), leaderId)) {
            throw new AppException(ResponseCode.TEAM_PERMISSION_DENIED.getCode(), ResponseCode.TEAM_PERMISSION_DENIED.getInfo());
        }
        ensureTeamActive(teamEntity);
        ensureNotFull(teamEntity);

        UserEntity invitedUser = userManagementRepository.queryUserBaseByUserId(invitedUserId);
        if (invitedUser == null) {
            throw new AppException(ResponseCode.USER_IS_NOT_EXIST.getCode(), ResponseCode.USER_IS_NOT_EXIST.getInfo());
        }
        if (!Integer.valueOf(UserTypeEnum.STUDENT.getCode()).equals(invitedUser.getType())) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "只能邀请学生用户加入队伍");
        }
        if (teamRepository.existsMember(teamId, invitedUserId)) {
            throw new AppException(ResponseCode.TEAM_MEMBER_CONFLICT.getCode(), "该用户已在当前团队中");
        }
        if (teamRepository.existsPendingInvite(teamId, invitedUserId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "该用户已有待处理的邀请");
        }

        TeamInviteEntity inviteEntity = TeamInviteEntity.builder()
                .inviteId(snowflake.nextIdStr())
                .teamId(teamId)
                .inviterId(leaderId)
                .invitedUserId(invitedUserId)
                .status(TeamInviteStatusEnum.PENDING.getCode())
                .createTime(LocalDateTime.now())
                .build();
        return teamRepository.saveInvite(inviteEntity);
    }

    @Override
    public List<TeamInviteItemDTO> getMyPendingInvites(String userId) {
        if (StrUtil.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        return teamRepository.queryInvitesByInvitedUserId(userId, TeamInviteStatusEnum.PENDING.getCode());
    }

    @Override
    public boolean agreeInvite(String inviteId, String userId) {
        return handleInvite(inviteId, userId, true);
    }

    @Override
    public boolean rejectInvite(String inviteId, String userId) {
        return handleInvite(inviteId, userId, false);
    }

    private boolean handleInvite(String inviteId, String userId, Boolean accept) {
        if (StrUtil.isBlank(inviteId) || StrUtil.isBlank(userId) || accept == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        TeamInviteEntity inviteEntity = teamRepository.queryInviteById(inviteId)
                .orElseThrow(() -> new AppException(ResponseCode.TEAM_INVITE_NOT_FOUND.getCode(), ResponseCode.TEAM_INVITE_NOT_FOUND.getInfo()));
        if (!StrUtil.equals(inviteEntity.getInvitedUserId(), userId)) {
            throw new AppException(ResponseCode.TEAM_PERMISSION_DENIED.getCode(), ResponseCode.TEAM_PERMISSION_DENIED.getInfo());
        }
        if (!StrUtil.equals(inviteEntity.getStatus(), TeamInviteStatusEnum.PENDING.getCode())) {
            throw new AppException(ResponseCode.TEAM_STATUS_INVALID.getCode(), "该邀请已处理");
        }

        TeamEntity teamEntity = loadTeam(inviteEntity.getTeamId());
        ensureTeamActive(teamEntity);

        if (accept) {
            if (teamRepository.existsMember(teamEntity.getTeamId(), userId)) {
                throw new AppException(ResponseCode.TEAM_MEMBER_CONFLICT.getCode(), "该用户已在当前团队中");
            }
            ensureNotFull(teamEntity);
            boolean added = teamRepository.addMember(teamEntity.getTeamId(), userId, "member");
            if (!added) {
                throw new AppException(ResponseCode.TEAM_UPDATE_FAIL.getCode(), "添加队员失败");
            }
            teamEntity.setMemberCount((teamEntity.getMemberCount() == null ? 0 : teamEntity.getMemberCount()) + 1);
            teamEntity.setUpdateTime(LocalDateTime.now());
            boolean teamUpdated = teamRepository.update(teamEntity);
            if (!teamUpdated) {
                throw new AppException(ResponseCode.TEAM_UPDATE_FAIL.getCode(), ResponseCode.TEAM_UPDATE_FAIL.getInfo());
            }
            inviteEntity.setStatus(TeamInviteStatusEnum.ACCEPTED.getCode());
        } else {
            inviteEntity.setStatus(TeamInviteStatusEnum.REJECTED.getCode());
        }

        inviteEntity.setHandleTime(LocalDateTime.now());
        boolean inviteUpdated = teamRepository.updateInvite(inviteEntity);
        if (!inviteUpdated) {
            throw new AppException(ResponseCode.TEAM_UPDATE_FAIL.getCode(), "更新邀请状态失败");
        }
        return true;
    }

    private TeamEntity loadTeam(String teamId) {
        if (StrUtil.isBlank(teamId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }
        return teamRepository.queryByTeamId(teamId).orElseThrow(() ->
                new AppException(ResponseCode.TEAM_NOT_FOUND.getCode(), ResponseCode.TEAM_NOT_FOUND.getInfo()));
    }

    private void ensureTeamActive(TeamEntity teamEntity) {
        if (!StrUtil.equals(teamEntity.getStatus(), TeamStatusEnum.ACTIVE.getCode())) {
            throw new AppException(ResponseCode.TEAM_STATUS_INVALID.getCode(), ResponseCode.TEAM_STATUS_INVALID.getInfo());
        }
    }

    private void ensureNotFull(TeamEntity teamEntity) {
        if (teamEntity.getMemberCount() != null
                && teamEntity.getMaxMemberCount() != null
                && teamEntity.getMemberCount() >= teamEntity.getMaxMemberCount()) {
            throw new AppException(ResponseCode.TEAM_STATUS_INVALID.getCode(), "队伍人数已满");
        }
    }
}
