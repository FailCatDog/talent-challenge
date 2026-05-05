package cn.hgy.domain.team.repository;

import cn.hgy.api.dto.TeamInviteItemDTO;
import cn.hgy.api.dto.TeamMemberItemDTO;
import cn.hgy.domain.team.model.entity.TeamEntity;
import cn.hgy.domain.team.model.entity.TeamInviteEntity;

import java.util.List;
import java.util.Optional;

public interface ITeamRepository {

    boolean save(TeamEntity teamEntity);

    boolean update(TeamEntity teamEntity);

    Optional<TeamEntity> queryByLeaderId(String leaderId);

    Optional<TeamEntity> queryByTeamId(String teamId);

    Optional<TeamEntity> queryActiveTeamByUserId(String userId);

    List<TeamEntity> queryByLeaderIdList(String leaderId, Integer offset, Integer limit);

    List<TeamEntity> queryJoinedTeamsByUserId(String userId);

    Optional<TeamEntity> queryJoinedTeamByUserIdAndTeamId(String userId, String teamId);

    Long countByLeaderId(String leaderId);

    boolean existsByLeaderIdAndStatus(String leaderId, String status);

    /** 查询团队成员列表（含昵称、头像、角色） */
    List<TeamMemberItemDTO> queryMembersByTeamId(String teamId);

    /** 判断用户是否为团队成员（不含队长） */
    boolean existsMember(String teamId, String userId);

    boolean saveInvite(TeamInviteEntity inviteEntity);

    boolean updateInvite(TeamInviteEntity inviteEntity);

    Optional<TeamInviteEntity> queryInviteById(String inviteId);

    boolean existsPendingInvite(String teamId, String invitedUserId);

    List<TeamInviteItemDTO> queryInvitesByInvitedUserId(String invitedUserId, String status);

    boolean addMember(String teamId, String userId, String role);

    boolean removeMember(String teamId, String userId);
}

