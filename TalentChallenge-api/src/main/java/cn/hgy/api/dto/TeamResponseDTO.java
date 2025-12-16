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
public class TeamResponseDTO {

    private String teamId;
    private String teamName;
    private String leaderId;
    private String description;
    private String avatarUrl;
    private String status;
    private Integer memberCount;
    private Integer maxMemberCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

