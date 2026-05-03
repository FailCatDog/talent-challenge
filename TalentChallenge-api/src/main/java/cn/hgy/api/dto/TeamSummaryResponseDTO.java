package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamSummaryResponseDTO {

    private String teamId;
    private String teamName;
    private String leaderId;
    private String description;
    private String avatarUrl;
    private String status;
    private Integer memberCount;
    private Integer maxMemberCount;
}
