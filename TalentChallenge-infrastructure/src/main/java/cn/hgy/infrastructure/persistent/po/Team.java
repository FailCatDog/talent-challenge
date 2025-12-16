package cn.hgy.infrastructure.persistent.po;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Team {

    private Long id;
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

