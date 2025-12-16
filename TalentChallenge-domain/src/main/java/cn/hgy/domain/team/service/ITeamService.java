package cn.hgy.domain.team.service;

import cn.hgy.domain.team.model.entity.TeamEntity;
import cn.hgy.types.common.TeamCommand;

import java.util.List;

public interface ITeamService {

    String createTeam(TeamCommand command);

    boolean updateTeam(TeamCommand command, String leaderId);

    boolean disbandTeam(String teamId, String leaderId);

    TeamEntity getTeamDetail(String teamId);

    TeamEntity getTeamByLeaderId(String leaderId);

    List<TeamEntity> getTeamsByLeaderId(String leaderId, Integer pageNum, Integer pageSize);

    Long countTeamsByLeaderId(String leaderId);
}

