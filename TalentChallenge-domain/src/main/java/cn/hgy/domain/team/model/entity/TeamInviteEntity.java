package cn.hgy.domain.team.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamInviteEntity {

    private String inviteId;
    private String teamId;
    private String inviterId;
    private String invitedUserId;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime handleTime;
}
