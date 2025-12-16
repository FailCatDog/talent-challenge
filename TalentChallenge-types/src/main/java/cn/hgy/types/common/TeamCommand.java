package cn.hgy.types.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamCommand {

    private String teamId;
    private String teamName;
    private String leaderId;
    private String description;
    private String avatarUrl;
    private Integer maxMemberCount;
}

