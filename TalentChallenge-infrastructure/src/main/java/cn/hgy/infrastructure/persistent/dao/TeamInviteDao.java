package cn.hgy.infrastructure.persistent.dao;

import cn.hgy.api.dto.TeamInviteItemDTO;
import cn.hgy.infrastructure.persistent.po.TeamInvite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TeamInviteDao {

    int insert(TeamInvite teamInvite);

    int update(TeamInvite teamInvite);

    TeamInvite selectByInviteId(@Param("inviteId") String inviteId);

    int existsPendingInvite(@Param("teamId") String teamId, @Param("invitedUserId") String invitedUserId);

    List<TeamInviteItemDTO> selectByInvitedUserId(@Param("invitedUserId") String invitedUserId,
                                                  @Param("status") String status);
}
