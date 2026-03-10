package cn.hgy.infrastructure.persistent.po;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 团队成员持久化对象
 */
@Data
public class TeamMember {

    /** 自增主键 */
    private Long id;

    /** 团队ID */
    private String teamId;

    /** 成员用户ID */
    private String userId;

    /** 角色：leader（队长）/ member（普通成员） */
    private String role;

    /** 加入时间 */
    private LocalDateTime joinTime;
}
