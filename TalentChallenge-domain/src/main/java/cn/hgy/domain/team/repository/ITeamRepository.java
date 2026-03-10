package cn.hgy.domain.team.repository;

import cn.hgy.api.dto.TeamMemberItemDTO;
import cn.hgy.domain.team.model.entity.TeamEntity;

import java.util.List;
import java.util.Optional;

public interface ITeamRepository {

    boolean save(TeamEntity teamEntity);

    boolean update(TeamEntity teamEntity);

    Optional<TeamEntity> queryByLeaderId(String leaderId);

    Optional<TeamEntity> queryByTeamId(String teamId);

    List<TeamEntity> queryByLeaderIdList(String leaderId, Integer offset, Integer limit);

    Long countByLeaderId(String leaderId);

    boolean existsByLeaderIdAndStatus(String leaderId, String status);

    /** 查询团队成员列表（含昵称、头像、角色） */
    List<TeamMemberItemDTO> queryMembersByTeamId(String teamId);

    /** 判断用户是否为团队成员（不含队长） */
    boolean existsMember(String teamId, String userId);
}

