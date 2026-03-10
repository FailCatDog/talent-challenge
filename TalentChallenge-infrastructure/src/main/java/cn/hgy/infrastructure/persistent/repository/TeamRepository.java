package cn.hgy.infrastructure.persistent.repository;

import cn.hgy.api.dto.TeamMemberItemDTO;
import cn.hgy.domain.team.model.entity.TeamEntity;
import cn.hgy.domain.team.repository.ITeamRepository;
import cn.hgy.infrastructure.persistent.dao.TeamDao;
import cn.hgy.infrastructure.persistent.dao.TeamMemberDao;
import cn.hgy.infrastructure.persistent.po.Team;
import cn.hgy.infrastructure.persistent.po.TeamMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TeamRepository implements ITeamRepository {

    private final TeamDao teamDao;
    private final TeamMemberDao teamMemberDao;

    @Override
    public boolean save(TeamEntity teamEntity) {
        Team team = convertToPo(teamEntity);
        LocalDateTime now = LocalDateTime.now();
        team.setCreateTime(now);
        team.setUpdateTime(now);
        boolean saved = teamDao.insert(team) > 0;
        if (saved) {
            // 创建团队时同步写入队长的成员记录
            TeamMember leaderMember = new TeamMember();
            leaderMember.setTeamId(teamEntity.getTeamId());
            leaderMember.setUserId(teamEntity.getLeaderId());
            leaderMember.setRole("leader");
            leaderMember.setJoinTime(now);
            teamMemberDao.insert(leaderMember);
        }
        return saved;
    }

    @Override
    public boolean update(TeamEntity teamEntity) {
        Team team = convertToPo(teamEntity);
        team.setUpdateTime(LocalDateTime.now());
        return teamDao.update(team) > 0;
    }

    @Override
    public Optional<TeamEntity> queryByLeaderId(String leaderId) {
        Team team = teamDao.selectByLeaderId(leaderId);
        if (team == null) {
            return Optional.empty();
        }
        return Optional.of(convertToEntity(team));
    }

    @Override
    public Optional<TeamEntity> queryByTeamId(String teamId) {
        Team team = teamDao.selectByTeamId(teamId);
        if (team == null) {
            return Optional.empty();
        }
        return Optional.of(convertToEntity(team));
    }

    @Override
    public List<TeamEntity> queryByLeaderIdList(String leaderId, Integer offset, Integer limit) {
        List<Team> teams = teamDao.selectByLeaderIdList(leaderId, offset, limit);
        if (teams == null || teams.isEmpty()) {
            return new ArrayList<>();
        }
        List<TeamEntity> entities = new ArrayList<>();
        for (Team team : teams) {
            entities.add(convertToEntity(team));
        }
        return entities;
    }

    @Override
    public Long countByLeaderId(String leaderId) {
        return teamDao.countByLeaderId(leaderId);
    }

    @Override
    public boolean existsByLeaderIdAndStatus(String leaderId, String status) {
        return teamDao.existsByLeaderIdAndStatus(leaderId, status) > 0;
    }

    @Override
    public List<TeamMemberItemDTO> queryMembersByTeamId(String teamId) {
        return teamMemberDao.selectMembersByTeamId(teamId);
    }

    private Team convertToPo(TeamEntity entity) {
        Team team = new Team();
        team.setTeamId(entity.getTeamId());
        team.setTeamName(entity.getTeamName());
        team.setLeaderId(entity.getLeaderId());
        team.setDescription(entity.getDescription());
        team.setAvatarUrl(entity.getAvatarUrl());
        team.setStatus(entity.getStatus());
        team.setMemberCount(entity.getMemberCount());
        team.setMaxMemberCount(entity.getMaxMemberCount());
        team.setCreateTime(entity.getCreateTime());
        team.setUpdateTime(entity.getUpdateTime());
        return team;
    }

    private TeamEntity convertToEntity(Team team) {
        return TeamEntity.builder()
                .teamId(team.getTeamId())
                .teamName(team.getTeamName())
                .leaderId(team.getLeaderId())
                .description(team.getDescription())
                .avatarUrl(team.getAvatarUrl())
                .status(team.getStatus())
                .memberCount(team.getMemberCount())
                .maxMemberCount(team.getMaxMemberCount())
                .createTime(team.getCreateTime())
                .updateTime(team.getUpdateTime())
                .build();
    }
}

