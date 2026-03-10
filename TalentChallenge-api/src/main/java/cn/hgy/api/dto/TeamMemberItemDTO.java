package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 团队成员信息（用于团队详情返回）
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamMemberItemDTO {

    /** 用户ID */
    private String userId;

    /** 昵称 */
    private String nickname;

    /** 头像URL */
    private String avatarUrl;

    /** 角色：leader / member */
    private String role;
}
