package cn.hgy.infrastructure.persistent.dao;

import cn.hgy.api.dto.TeamMemberItemDTO;
import cn.hgy.infrastructure.persistent.po.TeamMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 团队成员 DAO
 */
@Mapper
public interface TeamMemberDao {

    /** 插入成员记录 */
    int insert(TeamMember teamMember);

    /** 查询某用户是否在某团队中 */
    int existsByTeamIdAndUserId(@Param("teamId") String teamId, @Param("userId") String userId);

    /** 查询团队所有成员（含用户昵称、头像） */
    List<TeamMemberItemDTO> selectMembersByTeamId(@Param("teamId") String teamId);

    /** 删除普通成员记录 */
    int deleteMember(@Param("teamId") String teamId, @Param("userId") String userId);
}
