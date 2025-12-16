package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamCreateRequestDTO {

    private String teamName;
    private String description;
    private String avatarUrl;
    private Integer maxMemberCount;
}

