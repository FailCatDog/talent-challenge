package cn.hgy.domain.team.repository;

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
}

