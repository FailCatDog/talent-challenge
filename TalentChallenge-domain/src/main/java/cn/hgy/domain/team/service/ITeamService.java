package cn.hgy.domain.team.service;

import cn.hgy.api.dto.TeamInviteItemDTO;
import cn.hgy.api.dto.TeamMemberItemDTO;
import cn.hgy.domain.team.model.entity.TeamEntity;
import cn.hgy.types.common.TeamCommand;

import java.util.List;

public interface ITeamService {

    String createTeam(TeamCommand command);

    boolean updateTeam(TeamCommand command, String leaderId);

    boolean disbandTeam(String teamId, String leaderId);

    TeamEntity getTeamDetail(String teamId);

    TeamEntity getTeamByLeaderId(String leaderId);

    TeamEntity getActiveTeamByUserId(String userId);

    List<TeamEntity> getTeamsByLeaderId(String leaderId, Integer pageNum, Integer pageSize);

    Long countTeamsByLeaderId(String leaderId);

    /** 查询团队成员列表 */
    List<TeamMemberItemDTO> getTeamMembers(String teamId);

    /** 判断用户是否为团队成员（不含队长，仅普通成员） */
    boolean isTeamMember(String teamId, String userId);

    boolean inviteMember(String teamId, String leaderId, String invitedUserId);

    List<TeamInviteItemDTO> getMyInvites(String userId, String status);

    boolean handleInvite(String inviteId, String userId, Boolean accept);
}

