package cn.hgy.infrastructure.persistent.po;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TeamInvite {

    private Long id;
    private String inviteId;
    private String teamId;
    private String inviterId;
    private String invitedUserId;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime handleTime;
}
