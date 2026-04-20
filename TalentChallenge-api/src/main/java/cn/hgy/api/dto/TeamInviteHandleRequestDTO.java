package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamInviteHandleRequestDTO {

    private String inviteId;
    private Boolean accept;
}
