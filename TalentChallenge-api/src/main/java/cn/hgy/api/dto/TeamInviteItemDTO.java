package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamInviteItemDTO {

    private String inviteId;
    private String teamId;
    private String teamName;
    private String inviterId;
    private String inviterName;
    private String invitedUserId;
    private String invitedUserName;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime handleTime;
}
