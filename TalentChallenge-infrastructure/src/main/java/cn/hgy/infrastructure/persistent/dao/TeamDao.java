package cn.hgy.infrastructure.persistent.dao;

import cn.hgy.infrastructure.persistent.po.Team;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TeamDao {

    int insert(Team team);

    int update(Team team);

    Team selectByLeaderId(@Param("leaderId") String leaderId);

    Team selectByTeamId(@Param("teamId") String teamId);

    List<Team> selectByLeaderIdList(@Param("leaderId") String leaderId,
                                    @Param("offset") Integer offset,
                                    @Param("limit") Integer limit);

    long countByLeaderId(@Param("leaderId") String leaderId);

    int existsByLeaderIdAndStatus(@Param("leaderId") String leaderId, @Param("status") String status);
}

